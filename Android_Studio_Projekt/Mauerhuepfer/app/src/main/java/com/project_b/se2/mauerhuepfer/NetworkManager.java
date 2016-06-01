package com.project_b.se2.mauerhuepfer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AppIdentifier;
import com.google.android.gms.nearby.connection.AppMetadata;
import com.google.android.gms.nearby.connection.Connections;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by rohrbe on 14.05.16.
 */
public class NetworkManager implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        Connections.ConnectionRequestListener,
        Connections.MessageListener,
        Connections.EndpointDiscoveryListener,
        INetworkManager {

    private static String TAG;
    private ArrayList MessageReceiverListeners = new ArrayList();

    /**
     * GoogleApiClient for connecting to the Nearby Connections API
     **/
    private GoogleApiClient mGoogleApiClient;

    private MyListDialog mMyListDialog;

    private String mHostId;
    private ArrayList<String> mClientIds = new ArrayList<>();

    private IUpdateView mContext;

    private boolean mIsHost;
    //private int playerCounter = 1;
    private final int maxPlayer = 3;
    private int playerID;
    private String playerName;

    private static int[] NETWORK_TYPES = {ConnectivityManager.TYPE_WIFI};

    /* ------------------------------------------------------------------------------------------ */

    public boolean getHostinfo() {
        return mIsHost;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int player) {
        if (0 < player && player <= maxPlayer) {
            playerID = player;
        }
    }

    public void sendPlayerIDs(UpdateState state) {
        ArrayList<Integer> playerOrder = new ArrayList<>();
        switch (mClientIds.size()) {
            case 3:
                playerOrder.add(4);
            case 2:
                playerOrder.add(3);
            case 1:
            case 0:
                playerOrder.add(2);
                playerOrder.add(1);
                break;
        }

        Collections.shuffle(playerOrder, new SecureRandom());

        for (String id : mClientIds) {
            state.setPlayerID(playerOrder.remove(0));
            Nearby.Connections.sendReliableMessage(mGoogleApiClient, id, ObjectSerializer.Serialize(state));
        }

        playerID = playerOrder.remove(0);
    }

    public String getPlayerName() {
        return playerName;
    }

    /* ------------------------------------------------------------------------------------------ */

    public NetworkManager(Context c) {
        mGoogleApiClient = new GoogleApiClient.Builder(c)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(com.google.android.gms.nearby.Nearby.CONNECTIONS_API)
                .build();

        TAG = NetworkManager.class.getSimpleName();

        mContext = (IUpdateView) c;

        SharedPreferences settings = ((Context) mContext).getSharedPreferences(((Context) mContext).getString(R.string.memory), 0);
        playerName = settings.getString("playerName", null);

        if (playerName == null || playerName.equals("")) {
            playerName = "" + ((int) (Math.random() * 1000000));
        }
    }

    /* ------------------------------------------------------------------------------------------ */

    public void connect() {
        mGoogleApiClient.connect();
    }

    @Override
    public void disconnect() {
        // Disconnect the Google API client and stop any ongoing discovery or advertising. When the
        // GoogleAPIClient is disconnected, any connected peers will get an onDisconnected callback.
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    /* ------------------------------------------------------------------------------------------ */

    /**
     * Checks connection to network - in this case checking wifi
     *
     * @return true if there is a wifi or ethernet connection
     */
    private boolean isConnectedToNetwork() {
        ConnectivityManager conManager = (ConnectivityManager) mGoogleApiClient.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info;

        for (int networkType : NETWORK_TYPES) {
            info = conManager.getNetworkInfo(networkType);
            if (info != null && info.isConnectedOrConnecting()) {
                return true;
            }
        }

        return false;
    }

    /* ------------------------------------------------------------------------------------------ */

    /**
     * Begin advertising (HOST) for Nearby Connections, if possible.
     */
    public void startAdvertising() {
        debugLog("startAdvertising");
        if (!isConnectedToNetwork()) {
            debugLog("startAdvertising: not connected to network.");
            mContext.updateView(STATE_NONETWORK);
            return;
        }

        List<AppIdentifier> appIdentifierList = new ArrayList<>();
        appIdentifierList.add(new AppIdentifier(mGoogleApiClient.getContext().getPackageName()));
        AppMetadata appMetadata = new AppMetadata(appIdentifierList);

        SharedPreferences settings = ((Context) mContext).getSharedPreferences(((Context) mContext).getString(R.string.memory), 0);
        String hostName = settings.getString("hostName", null);

        Nearby.Connections.startAdvertising(mGoogleApiClient, hostName, appMetadata, TIMEOUT_ADVERTISE, this)
                .setResultCallback(new ResultCallback<Connections.StartAdvertisingResult>() {

                    @Override
                    public void onResult(Connections.StartAdvertisingResult result) {
                        Log.d(TAG, "startAdvertising: onResult:" + result);
                        if (result.getStatus().isSuccess()) {
                            debugLog("startAdvertising: SUCCESS");
                            mContext.updateView(STATE_ADVERTISING);
                            mIsHost = true;
                        } else {
                            debugLog("startAdvertising: FAILURE ");
                            int statusCode = result.getStatus().getStatusCode();
                            if (statusCode == ConnectionsStatusCodes.STATUS_ALREADY_ADVERTISING) {
                                debugLog("STATUS_ALREADY_ADVERTISING");
                                mContext.updateView(STATE_ADVERTISING);
                            } else {
                                mContext.updateView(STATE_READY);
                            }
                        }
                    }
                });
    }

    public void stopAdvertising() {
        Nearby.Connections.stopAdvertising(mGoogleApiClient);
    }

    /* ------------------------------------------------------------------------------------------ */

    /**
     * Begin discovering (CLIENT) devices advertising Nearby Connections, if possible.
     */
    public void startDiscovery() {
        debugLog("startDiscovery");
        if (!isConnectedToNetwork()) {
            debugLog("startDiscovery: not connected to network.");
            mContext.updateView(STATE_NONETWORK);
            return;
        }
        mIsHost = false;

        String serviceId = ((Context) mContext).getString(R.string.service_id);
        Nearby.Connections.startDiscovery(mGoogleApiClient, serviceId, TIMEOUT_DISCOVER, this)
                .setResultCallback(new ResultCallback<Status>() {

                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            debugLog("startDiscovery: SUCCESS");
                            mContext.updateView(STATE_DISCOVERING);

                        } else {
                            debugLog("startDiscovery: FAILURE");
                            int statusCode = status.getStatusCode();
                            if (statusCode == ConnectionsStatusCodes.STATUS_ALREADY_DISCOVERING) {
                                debugLog("STATUS_ALREADY_DISCOVERING");
                            } else {
                                mContext.updateView(STATE_READY);
                            }
                        }
                    }
                });
    }

    public void stopDiscovery() {
        String serviceId = ((Context) mContext).getString(R.string.service_id);
        Nearby.Connections.stopDiscovery(mGoogleApiClient, serviceId);
    }

    /* ------------------------------------------------------------------------------------------ */

    @Override
    public void onConnected(Bundle bundle) {
        debugLog("onConnected");
        mContext.updateView(STATE_READY);
    }

    /* ------------------------------------------------------------------------------------------ */

    @Override
    public void onConnectionSuspended(int i) {
        debugLog("onConnectionSuspended: " + i);
        mContext.updateView(STATE_IDLE);

        // Try to re-connect
        mGoogleApiClient.reconnect();
    }

    /* ------------------------------------------------------------------------------------------ */

    /**
     * Once your host application is advertising, it will be able to receive connection requests from peers.
     * When a device attempts to connect, the following method will be called:
     * Using this method, you can either accept or reject the connection.
     *
     * @param endpointId   Client ID
     * @param deviceId     Client Device
     * @param endpointName Client Name
     * @param payload      Message
     */
    @Override
    public void onConnectionRequest(final String endpointId, String deviceId, String endpointName, byte[] payload) {
        debugLog("onConnectionRequest:" + endpointId + ":" + endpointName);
        if (mClientIds.size() < maxPlayer) {
            if (mIsHost) {
                Nearby.Connections.acceptConnectionRequest(mGoogleApiClient, endpointId, payload, this)
                        .setResultCallback(new ResultCallback<Status>() {

                            @Override
                            public void onResult(Status status) {
                                if (status.isSuccess()) {
                                    debugLog("acceptConnectionRequest: SUCCESS");
                                    mClientIds.add(endpointId);
                                    mContext.updateView(STATE_CONNECTED);
                                } else {
                                    debugLog("acceptConnectionRequest: FAILURE");
                                }
                            }
                        });
            } else {
                Nearby.Connections.rejectConnectionRequest(mGoogleApiClient, endpointId);
            }
        } else {
            stopAdvertising();
        }
    }

    /* ------------------------------------------------------------------------------------------ */

    /**
     * When the device detects a host that is currently advertising using the predefined service identifier,
     * the onEndpointFound callback will be triggered.
     * It should be noted that this method can be called multiple times if there are multiple hosts broadcasting.
     * In this situation, you can create a dialog for your users that displays all available hosts
     * so that they can select which they would like to be connected to.
     *
     * @param endpointId   Host ID
     * @param deviceId     Host Device
     * @param serviceId    Service ID
     * @param endpointName Host Name
     */
    @Override
    public void onEndpointFound(final String endpointId, String deviceId, String serviceId, final String endpointName) {
        Log.d(TAG, "onEndpointFound:" + endpointId + ":" + endpointName);

        if (mMyListDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder((Context) mContext)
                    .setTitle("Endpoint(s) Found")
                    .setCancelable(true)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mMyListDialog.dismiss();
                        }
                    });

            mMyListDialog = new MyListDialog((Context) mContext, builder, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String selectedEndpointName = mMyListDialog.getItemKey(which);
                    String selectedEndpointId = mMyListDialog.getItemValue(which);

                    NetworkManager.this.connectTo(selectedEndpointId, selectedEndpointName);
                    mMyListDialog.dismiss();
                }
            });
        }

        mMyListDialog.addItem(endpointName, endpointId);
        mMyListDialog.show();
    }

    /* ------------------------------------------------------------------------------------------ */

    /**
     * Send a connection request to a remote endpoint. By passing 'null' for the name,
     * the Nearby Connections API will construct a default name based on device model.
     *
     * @param endpointId   the endpointId to which you want to connect.
     * @param endpointName the name of the endpoint to which you want to connect.
     *                     Not required to make the connection, but used to display after success or failure.
     */
    private void connectTo(String endpointId, final String endpointName) {
        debugLog("connectTo:" + endpointId + ":" + endpointName);

        String myName = null;
        byte[] myPayload = null;
        Nearby.Connections.sendConnectionRequest(mGoogleApiClient, myName, endpointId, myPayload, new Connections.ConnectionResponseCallback() {
            @Override
            public void onConnectionResponse(String endpointId, Status status, byte[] bytes) {
                Log.d(TAG, "onConnectionResponse:" + endpointId + ":" + status);
                if (status.isSuccess()) {
                    debugLog("onConnectionResponse: " + endpointName + " SUCCESS");
                    mHostId = endpointId;
                    mContext.updateView(STATE_CONNECTED);
                    UpdateState msg = new UpdateState();
                    msg.setMsg(playerName + " joined Game");
                    msg.setUsage(IReceiveMessage.USAGE_JOIN);
                    sendMessage(msg);
                } else {
                    debugLog("onConnectionResponse: " + endpointName + " FAILURE");
                }
            }
        }, this);
    }

    /* ------------------------------------------------------------------------------------------ */

    /**
     * In a situation where you listen for multiple endpoints to present a choice for your users,
     * the onEndpointLost method will let you know if a host has stopped advertising before your user has attempted to connect to it.
     * The onDisconnected callback is also available for client devices and can be used for reconnecting to advertising hosts
     * in the event of an unexpected disconnect.
     *
     * @param endpointId Host that is no longer available
     */
    @Override
    public void onEndpointLost(String endpointId) {
        debugLog("onEndpointLost:" + endpointId);

        // An endpoint that was previously available for connection is no longer. It may have
        // stopped advertising, gone out of range, or lost connectivity. Dismiss any dialog that
        // was offering a connection.
        if (mMyListDialog != null) {
            mMyListDialog.removeItemByValue(endpointId);
        }
    }

   /* ------------------------------------------------------------------------------------------ */

    /**
     * Send a reliable message to the connected peer. Takes the contents of the EditText and
     * sends the message as a byte[].
     * --------------------------------------------------------------------------------------------
     * Sends a reliable message, which is guaranteed to be delivered eventually and to respect
     * message ordering from sender to receiver. Nearby.Connections.sendUnreliableMessage
     * should be used for high-frequency messages where guaranteed delivery is not required, such
     * as showing one player's cursor location to another. Unreliable messages are often
     * delivered faster than reliable messages.
     */
    @Override
    public void sendMessage(UpdateState s) {
        if (mIsHost) {
            for (String id : mClientIds) {
                Nearby.Connections.sendReliableMessage(mGoogleApiClient, id, ObjectSerializer.Serialize(s));
            }
        } else {
            Nearby.Connections.sendReliableMessage(mGoogleApiClient, mHostId, ObjectSerializer.Serialize(s));
        }
    }

    /* ------------------------------------------------------------------------------------------ */

    /**
     * A message has been received from a remote endpoint.
     *
     * @param endpointId Sender
     * @param payload    Message
     * @param isReliable is Reliable true/false
     */
    @Override
    public void onMessageReceived(String endpointId, byte[] payload, boolean isReliable) {
        UpdateState updateS = (UpdateState) ObjectSerializer.DeSerialize(payload);
        debugLog("onMessageReceived:" + endpointId);
        messageReciever(updateS);

        if (mIsHost) {
            for (String id : mClientIds) {
                if (!id.equals(endpointId)) {
                    Nearby.Connections.sendReliableMessage(mGoogleApiClient, id, payload);
                }
            }
        }
    }

    /* ------------------------------------------------------------------------------------------ */

    /**
     * The onDisconnected callback is also available for client devices and can be used for reconnecting to advertising hosts
     * in the event of an unexpected disconnect.
     *
     * @param endpointId Disconnected Device
     */
    @Override
    public void onDisconnected(String endpointId) {
        debugLog("onDisconnected:" + endpointId);
        if (mIsHost) {
            mClientIds.remove(mClientIds.indexOf(endpointId));
            //playerCounter--;
            if (mClientIds.size() < 1) {
                mGoogleApiClient.reconnect();
                restartApp();
            }
        } else {
            mContext.updateView(STATE_READY);
            mGoogleApiClient.reconnect();
            restartApp();
        }
    }

    private void restartApp(){
        UpdateState restart = new UpdateState();
        restart.setUsage(IReceiveMessage.USAGE_RESTART);
        messageReciever(restart);
    }

    /* ------------------------------------------------------------------------------------------ */

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        debugLog("onConnectionFailed: " + connectionResult);
        mContext.updateView(STATE_IDLE);
    }

    /* ------------------------------------------------------------------------------------------ */

    /**
     * Print a message to the DEBUG LogCat and to the on-screen debug panel (if needed).
     *
     * @param msg the message to print and display.
     */
    private void debugLog(String msg) {
        Log.d(TAG, msg);
        //mDebugInfo.append("\n" + msg);
    }

    /* ------------------------------------------------------------------------------------------ */

    /**
     * add Observer
     *
     * @param listener new Listener(Observer)
     */
    @Override
    public void addMessageReceiverListener(IReceiveMessage listener) {
        MessageReceiverListeners.add(listener);
    }

    /**
     * remove Observer
     *
     * @param listener listener to remove
     */
    @Override
    public void removeMessageReceiverListener(IReceiveMessage listener) {
        MessageReceiverListeners.remove(listener);
    }

    /**
     * loop through each listener and pass on the event if needed
     * pass the event to the listeners event dispatch method
     *
     * @param s UpdateState Object
     */
    protected void messageReciever(UpdateState s) {
        for (Object l : MessageReceiverListeners) {
            ((IReceiveMessage) l).receiveMessage(s);
        }
    }
}