package com.project_b.se2.mauerhuepfer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohrbe on 14.05.16.
 */
public class NetworkManager implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        Connections.ConnectionRequestListener,
        Connections.MessageListener,
        Connections.EndpointDiscoveryListener {

    private static String TAG;
    private ArrayList MessageReceiverListeners = new ArrayList();

    // Timeouts...
    private static final long TIMEOUT_ADVERTISE = 1000L * 30L;
    private static final long TIMEOUT_DISCOVER = 1000L * 30L;
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
    private String mOtherEndpointId;

    private ICommunication mContext;

    private boolean mIsHost;

    public boolean getHostinfo(){
        return mIsHost;
    }

    /**
     * Network Types...
     */
    private static int[] NETWORK_TYPES = {ConnectivityManager.TYPE_WIFI};

    /* ------------------------------------------------------------------------------------------ */

    /**
     * Constructor...
     */
    public NetworkManager(Context c) {
        mGoogleApiClient = new GoogleApiClient.Builder(c)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(com.google.android.gms.nearby.Nearby.CONNECTIONS_API)
                .build();

        TAG = NetworkManager.class.getSimpleName();

        mContext = (ICommunication) c;
    }

    /* ------------------------------------------------------------------------------------------ */

    /**
     * onStart, onStop...
     */
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
            mContext.updateState(STATE_READY);
            return;
        }

        // Advertising with an AppIdentifer lets other devices on the network discover
        // this application and prompt the user to install the application.
        List<AppIdentifier> appIdentifierList = new ArrayList<>();
        appIdentifierList.add(new AppIdentifier(mGoogleApiClient.getContext().getPackageName()));
        AppMetadata appMetadata = new AppMetadata(appIdentifierList);

        // Advertise for Nearby Connections. This will broadcast the service id defined in
        // AndroidManifest.xml. By passing 'null' for the name, the Nearby Connections API
        // will construct a default name based on device model such as 'LGE Nexus 5'.
        Nearby.Connections.startAdvertising(mGoogleApiClient, null, appMetadata, TIMEOUT_ADVERTISE, this)
                .setResultCallback(new ResultCallback<Connections.StartAdvertisingResult>() {

                    @Override
                    public void onResult(Connections.StartAdvertisingResult result) {
                        Log.d(TAG, "startAdvertising: onResult:" + result);
                        if (result.getStatus().isSuccess()) {
                            debugLog("startAdvertising: SUCCESS");
                            mContext.updateState(STATE_ADVERTISING);
                            mIsHost = true;
                        } else {
                            debugLog("startAdvertising: FAILURE ");

                            // If the user hits 'Advertise' multiple times in the timeout window,
                            // the error will be STATUS_ALREADY_ADVERTISING
                            int statusCode = result.getStatus().getStatusCode();
                            if (statusCode == ConnectionsStatusCodes.STATUS_ALREADY_ADVERTISING) {
                                debugLog("STATUS_ALREADY_ADVERTISING");
                                mContext.updateState(STATE_ADVERTISING);
                            } else {
                                mContext.updateState(STATE_READY);
                            }
                        }
                    }
                });
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

        // Discover nearby apps that are advertising with the required service ID.
        String serviceId = ((Context) mContext).getString(R.string.service_id);
        Nearby.Connections.startDiscovery(mGoogleApiClient, serviceId, TIMEOUT_DISCOVER, this)
                .setResultCallback(new ResultCallback<Status>() {

                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            debugLog("startDiscovery: SUCCESS");
                            mContext.updateState(STATE_DISCOVERING);

                        } else {
                            debugLog("startDiscovery: FAILURE");

                            // If the user hits 'Discover' multiple times in the timeout window,
                            // the error will be STATUS_ALREADY_DISCOVERING
                            int statusCode = status.getStatusCode();
                            if (statusCode == ConnectionsStatusCodes.STATUS_ALREADY_DISCOVERING) {
                                debugLog("STATUS_ALREADY_DISCOVERING");
                            } else {
                                mContext.updateState(STATE_READY);
                            }
                        }
                    }
                });
    }

    /* ------------------------------------------------------------------------------------------ */

    @Override
    public void onConnected(Bundle bundle) {
        debugLog("onConnected");
        mContext.updateState(STATE_READY);
    }

    /* ------------------------------------------------------------------------------------------ */

    @Override
    public void onConnectionSuspended(int i) {
        debugLog("onConnectionSuspended: " + i);
        mContext.updateState(STATE_IDLE);

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
     * @param payload
     */
    @Override
    public void onConnectionRequest(final String endpointId, String deviceId, String endpointName, byte[] payload) {
        debugLog("onConnectionRequest:" + endpointId + ":" + endpointName);

        if (mIsHost) {
            Nearby.Connections.acceptConnectionRequest(mGoogleApiClient, endpointId, payload, this)
                    .setResultCallback(new ResultCallback<Status>() {

                        @Override
                        public void onResult(Status status) {
                            if (status.isSuccess()) {
                                debugLog("acceptConnectionRequest: SUCCESS");

                                mOtherEndpointId = endpointId;
                                mContext.updateState(STATE_CONNECTED);
                            } else {
                                debugLog("acceptConnectionRequest: FAILURE");
                            }
                        }
                    });
        } else {
            Nearby.Connections.rejectConnectionRequest(mGoogleApiClient, endpointId);
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
     * @param endpointName
     */
    @Override
    public void onEndpointFound(final String endpointId, String deviceId, String serviceId, final String endpointName) {
        Log.d(TAG, "onEndpointFound:" + endpointId + ":" + endpointName);

        // This device is discovering endpoints and has located an advertiser. Display a dialog to
        // the user asking if they want to connect, and send a connection request if they do.
        if (mMyListDialog == null) {
            // Configure the AlertDialog that the MyListDialog wraps
            AlertDialog.Builder builder = new AlertDialog.Builder((Context) mContext)
                    .setTitle("Endpoint(s) Found")
                    .setCancelable(true)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mMyListDialog.dismiss();
                        }
                    });

            // Create the MyListDialog with a listener
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
                    mContext.updateState(STATE_CONNECTED);
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
     * @param endpointId
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
     * <p/>
     * Sends a reliable message, which is guaranteed to be delivered eventually and to respect
     * message ordering from sender to receiver. Nearby.Connections.sendUnreliableMessage
     * should be used for high-frequency messages where guaranteed delivery is not required, such
     * as showing one player's cursor location to another. Unreliable messages are often
     * delivered faster than reliable messages.
     */
    public void sendMessage(UpdateState s) {
        Nearby.Connections.sendReliableMessage(mGoogleApiClient, mOtherEndpointId, ObjectSerializer.Serialize(s));
    }

    /* ------------------------------------------------------------------------------------------ */

    /**
     * A message has been received from a remote endpoint.
     *
     * @param endpointId
     * @param payload
     * @param isReliable
     */
    @Override
    public void onMessageReceived(String endpointId, byte[] payload, boolean isReliable) {
        UpdateState updateS = (UpdateState) ObjectSerializer.DeSerialize(payload);
        debugLog("onMessageReceived:" + endpointId + ":" + updateS.toString());
        messageReciever(updateS);
    }

    /* ------------------------------------------------------------------------------------------ */

    /**
     * The onDisconnected callback is also available for client devices and can be used for reconnecting to advertising hosts
     * in the event of an unexpected disconnect.
     *
     * @param endpointId
     */
    @Override
    public void onDisconnected(String endpointId) {
        debugLog("onDisconnected:" + endpointId);
        mContext.updateState(STATE_READY);
    }

    /* ------------------------------------------------------------------------------------------ */

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        debugLog("onConnectionFailed: " + connectionResult);
        mContext.updateState(STATE_IDLE);
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

    public void addMessageReceiverListener(ICommunication listener) {
        MessageReceiverListeners.add(listener);
    }

    public void removeMessageReceiverListener(ICommunication listener) {
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
            ((ICommunication) l).receiveMessage(s);
        }
    }
}