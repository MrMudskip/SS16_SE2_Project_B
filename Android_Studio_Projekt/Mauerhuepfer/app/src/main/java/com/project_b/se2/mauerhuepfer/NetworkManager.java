package com.project_b.se2.mauerhuepfer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.IntDef;
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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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

    // Timeouts...
    private static final long TIMEOUT_ADVERTISE = 1000L * 30L;
    private static final long TIMEOUT_DISCOVER = 1000L * 10L;
    /* ------------------------------------------------------------------------------------------ */

    /**
     * Possible states for this application:
     * IDLE - GoogleApiClient not yet connected, can't do anything.
     * READY - GoogleApiClient connected, ready to use Nearby Connections API.
     * ADVERTISING - advertising for peers to connect.
     * DISCOVERING - looking for a peer that is advertising.
     * CONNECTED - found a peer.
     */
    @Retention(RetentionPolicy.CLASS)
    @IntDef({STATE_IDLE, STATE_READY, STATE_ADVERTISING, STATE_DISCOVERING, STATE_CONNECTED})
    public @interface NearbyConnectionState {
    }

    public static final int STATE_IDLE = 1023;
    public static final int STATE_READY = 1024;
    public static final int STATE_ADVERTISING = 1025;
    public static final int STATE_DISCOVERING = 1026;
    public static final int STATE_CONNECTED = 1027;
    public static final int USAGE_PLAYERID = 1028;

    /* ------------------------------------------------------------------------------------------ */

    /**
     * GoogleApiClient for connecting to the Nearby Connections API
     **/
    private GoogleApiClient mGoogleApiClient;

    /* ------------------------------------------------------------------------------------------ */

    /**
     * Views and Dialogs
     **/
    private MyListDialog mMyListDialog;

    /* ------------------------------------------------------------------------------------------ */

    /**
     * The endpoint ID of the connected peer, used for messaging
     **/
    private String mOtherEndpointId; // HOST
    private ArrayList<String> mOtherEndpointIds = new ArrayList<>();

    private IUpdateView mContext;

    private boolean mIsHost;
    private int playerCounter = 1;
    private final int maxPlayer = 4;
    private int playerID;
    private ArrayList<Integer> playerOrder;

    public boolean getHostinfo() {
        return mIsHost;
    }

    public int getPlayerID() {
        return playerID;
    }

    private void playerOrder() {
        playerOrder = new ArrayList<>();
        switch (playerCounter) {
            case 4:
                playerOrder.add(4);
            case 3:
                playerOrder.add(3);
            case 2:
            case 1:
                playerOrder.add(2);
                playerOrder.add(1);
                break;
        }
        Collections.shuffle(playerOrder, new SecureRandom());
    }

    private static int[] NETWORK_TYPES = {ConnectivityManager.TYPE_WIFI};

    /* ------------------------------------------------------------------------------------------ */

    public NetworkManager(Context c) {
        mGoogleApiClient = new GoogleApiClient.Builder(c)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(com.google.android.gms.nearby.Nearby.CONNECTIONS_API)
                .build();

        TAG = NetworkManager.class.getSimpleName();

        mContext = (IUpdateView) c;
    }

    /* ------------------------------------------------------------------------------------------ */

    public void connect() {
        mGoogleApiClient.connect();
    }

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
            mContext.updateView(STATE_READY);
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
        UpdateState s = new UpdateState();
        s.setUsage(USAGE_PLAYERID);
        playerOrder();

        for (String id : mOtherEndpointIds) {
            s.setPlayerID(playerOrder.remove(0));
            Nearby.Connections.sendReliableMessage(mGoogleApiClient, id, ObjectSerializer.Serialize(s));
        }

        playerID = playerOrder.remove(0);
    }

    /* ------------------------------------------------------------------------------------------ */

    /**
     * Begin discovering (CLIENT) devices advertising Nearby Connections, if possible.
     */
    public void startDiscovery() {
        debugLog("startDiscovery");
        if (!isConnectedToNetwork()) {
            debugLog("startDiscovery: not connected to network.");
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
        if (playerCounter < maxPlayer) {
            if (mIsHost) {
                Nearby.Connections.acceptConnectionRequest(mGoogleApiClient, endpointId, payload, this)
                        .setResultCallback(new ResultCallback<Status>() {

                            @Override
                            public void onResult(Status status) {
                                if (status.isSuccess()) {
                                    debugLog("acceptConnectionRequest: SUCCESS");
                                    mOtherEndpointIds.add(endpointId);
                                    playerCounter++;
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
                    mOtherEndpointId = endpointId;
                    mContext.updateView(STATE_CONNECTED);
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
            for (String id : mOtherEndpointIds) {
                Nearby.Connections.sendReliableMessage(mGoogleApiClient, id, ObjectSerializer.Serialize(s));
            }
        } else {
            Nearby.Connections.sendReliableMessage(mGoogleApiClient, mOtherEndpointId, ObjectSerializer.Serialize(s));
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
        debugLog("onMessageReceived:" + endpointId + ":" + updateS.toString());
        if (updateS.getUsage() != USAGE_PLAYERID) {
            messageReciever(updateS);
        } else {
            playerID = updateS.getPlayerID();
        }

        if (mIsHost) {
            for (String id : mOtherEndpointIds) {
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
            mOtherEndpointIds.remove(mOtherEndpointIds.indexOf(endpointId));
            playerCounter--;
            if (mOtherEndpointIds.size() < 1) {
                mContext.updateView(STATE_READY);
            }
        } else {
            mContext.updateView(STATE_READY);
        }
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