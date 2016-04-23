package com.project_b.se2.mauerhuepfer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.Connections;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohrbe on 22.04.16.
 */
public class NetworkActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        Connections.ConnectionRequestListener,
        Connections.MessageListener,
        Connections.EndpointDiscoveryListener,
        View.OnClickListener {

    private static final int STATE_DISCONNECTED = 1023;
    private static final int STATE_CONNECTED = 1024;
    private int currentState = STATE_CONNECTED;

    /**
     * Diverse Variablen
     */
    private GoogleApiClient mGoogleApiClient;

    private TextView mStatusText;
    private Button mHostButton;
    private Button mJoinButton;
    private Button mSendButton;
    private Button mDiscoButton;
    private ListView mListView;
    private EditText mSendEditText;

    private ArrayAdapter<String> mMessageAdapter;

    private boolean mIsHost;
    private boolean mIsConnected;

    private String mRemoteHostEndpoint;
    private List<String> mRemotePeerEndpoints = new ArrayList<String>();
    private List<String> adapterArr = new ArrayList<String>();

    private static final long CONNECTION_TIME_OUT = 10000L;

    private static int[] NETWORK_TYPES = {ConnectivityManager.TYPE_WIFI,
            ConnectivityManager.TYPE_ETHERNET};

    /**
     * ------------------------------------------------------------------------
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        updateView();
        Intent intent = getIntent();

        // Buttons
        mHostButton = (Button) findViewById(R.id.buttonHost);
        mJoinButton = (Button) findViewById(R.id.buttonJoin);
        mSendButton = (Button) findViewById(R.id.buttonSend);
        mDiscoButton = (Button) findViewById(R.id.buttonDisco);
        findViewById(R.id.buttonHost).setOnClickListener(this);
        findViewById(R.id.buttonJoin).setOnClickListener(this);
        findViewById(R.id.buttonSend).setOnClickListener(this);
        findViewById(R.id.buttonDisco).setOnClickListener(this);

        mMessageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, adapterArr);
        mStatusText = (TextView) findViewById(R.id.statusText);
        mListView = (ListView) findViewById(R.id.activityList);
        if (mListView != null) {
            mListView.setAdapter(mMessageAdapter);
        }
        mSendEditText = (EditText) findViewById(R.id.editText);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Nearby.CONNECTIONS_API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Host Methode
     * fist check if the device is connected to a network
     */
    private void advertise() {
        if (!isConnectedToNetwork()) {
            mStatusText.setText("No Network Connection");
            return;
        }

        String name = "NearbyHost";

        Nearby.Connections.startAdvertising(mGoogleApiClient, name, null, CONNECTION_TIME_OUT, this)
                .setResultCallback(new ResultCallback<Connections.StartAdvertisingResult>() {

                    @Override
                    public void onResult(Connections.StartAdvertisingResult result) {
                        if (result.getStatus().isSuccess()) {
                            mStatusText.setText("Hosting...");
                        } else {
                            mStatusText.setText("Hosting Failure");
                        }
                    }
                });
        mIsHost = true;

    }

    /**
     * Checks connection to network - in this case checking wifi and ethernet
     *
     * @return true if there is a wifi or ethernet connection
     */
    private boolean isConnectedToNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        for (int networkType : NETWORK_TYPES) {
            NetworkInfo info = connManager.getNetworkInfo(networkType);
            if (info != null && info.isConnectedOrConnecting()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Once your host application is advertising, it will be able to receive connection requests from peers.
     * When a device attempts to connect, the following method will be called:
     * Using this method, you can either accept or reject the connection.
     *
     * @param remoteEndpointId
     * @param remoteDeviceId
     * @param remoteEndpointName
     * @param payload
     */
    @Override
    public void onConnectionRequest(final String remoteEndpointId, final String remoteDeviceId, final String remoteEndpointName, byte[] payload) {
        if (mIsHost) {
            Nearby.Connections.acceptConnectionRequest(mGoogleApiClient, remoteEndpointId, payload, this)
                    .setResultCallback(new ResultCallback<Status>() {

                        @Override
                        public void onResult(Status status) {
                            if (status.isSuccess()) {
                                if (!mRemotePeerEndpoints.contains(remoteEndpointId)) {
                                    mRemotePeerEndpoints.add(remoteEndpointId);
                                }
                                sendMessage(remoteDeviceId + " connected!");
                            } else {
                                mStatusText.setText("onConnectionRequest Failure");
                            }
                        }
                    });

        } else {
            Nearby.Connections.rejectConnectionRequest(mGoogleApiClient, remoteEndpointId);
        }
    }


    /**
     * Client Methode
     */
    private void discover() {
        if (!isConnectedToNetwork()) {
            mStatusText.setText("No Network Connection");
            return;
        }

        String serviceId = getString(R.string.service_id);
        Nearby.Connections.startDiscovery(mGoogleApiClient, serviceId, 10000L, this)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            mStatusText.setText("Discovering");
                        } else {
                            Log.e("TutsPlus", "Discovering failed: " + status.getStatusMessage());
                        }
                    }
                });
        mIsHost = false;
    }

    /**
     * When the device detects a host that is currently advertising using the predefined service identifier,
     * the onEndpointFound callback will be triggered.
     * It should be noted that this method can be called multiple times if there are multiple hosts broadcasting.
     * In this situation, you can create a dialog for your users that displays all available hosts
     * so that they can select which they would like to be connected to.
     *
     * @param endpointId
     * @param deviceId
     * @param serviceId
     * @param endpointName
     */
    @Override
    public void onEndpointFound(String endpointId, String deviceId,
                                final String serviceId, String endpointName) {
        byte[] payload = null;

        Nearby.Connections.sendConnectionRequest(mGoogleApiClient, deviceId,
                endpointId, payload, new Connections.ConnectionResponseCallback() {

                    @Override
                    public void onConnectionResponse(String endpointId, Status status, byte[] bytes) {
                        if (status.isSuccess()) {
                            mStatusText.setText("Connected to: " + endpointId);
                            Nearby.Connections.stopDiscovery(mGoogleApiClient, serviceId);
                            mRemoteHostEndpoint = endpointId;

                            if (!mIsHost) {
                                mIsConnected = true;
                            }
                        } else {
                            mStatusText.setText("Connection to " + endpointId + " failed");
                            if (!mIsHost) {
                                mIsConnected = false;
                            }
                        }
                    }
                }, this);
    }

    @Override
    public void onMessageReceived(String endpointId, byte[] payload, boolean isReliable) {
        if (mIsHost) {
            sendMessage(new String(payload));
        } else {
            mMessageAdapter.add(new String(payload));
            mMessageAdapter.notifyDataSetChanged();
        }
    }

    private void sendMessage(String message) {
        if (mIsHost) {
            Nearby.Connections.sendReliableMessage(mGoogleApiClient, mRemotePeerEndpoints, message.getBytes());

            mMessageAdapter.add(message);
            mMessageAdapter.notifyDataSetChanged();
        } else {
            Nearby.Connections.sendReliableMessage(mGoogleApiClient, mRemoteHostEndpoint,
                    (Nearby.Connections.getLocalDeviceId(mGoogleApiClient) + " says: " + message).getBytes());
        }
    }

    /**
     * Disconnect Method
     */
    private void disconnect() {
        if (!isConnectedToNetwork())
            return;

        if (mIsHost) {
            sendMessage("Shutting down host");
            Nearby.Connections.stopAdvertising(mGoogleApiClient);
            Nearby.Connections.stopAllEndpoints(mGoogleApiClient);
            mIsHost = false;
            mStatusText.setText("Not connected");
            mRemotePeerEndpoints.clear();
        } else {
            if (!mIsConnected || TextUtils.isEmpty(mRemoteHostEndpoint)) {
                Nearby.Connections.stopDiscovery(mGoogleApiClient, getString(R.string.service_id));
                return;
            }

            sendMessage("Disconnecting");
            Nearby.Connections.disconnectFromEndpoint(mGoogleApiClient, mRemoteHostEndpoint);
            mRemoteHostEndpoint = null;
            mStatusText.setText("Disconnected");
        }

        mIsConnected = false;
    }

    /**
     * In a situation where you listen for multiple endpoints to present a choice for your users,
     * the onEndpointLost method will let you know if a host has stopped advertising before your user has attempted to connect to it.
     * The onDisconnected callback is also available for client devices and can be used for reconnecting to advertising hosts
     * in the event of an unexpected disconnect.
     *
     * @param s
     */
    @Override
    public void onEndpointLost(String s) {

    }

    /**
     * The onDisconnected callback is also available for client devices and can be used for reconnecting to advertising hosts
     * in the event of an unexpected disconnect.
     *
     * @param s
     */
    @Override
    public void onDisconnected(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonHost:
                advertise();
                break;
            case R.id.buttonJoin:
                discover();
                break;
            case R.id.buttonDisco:
                disconnect();
                break;
            case R.id.buttonSend:
                sendMessage(mSendEditText.getText().toString());
                break;
        }
    }

    private void updateView() {
        switch (currentState) {
            case STATE_CONNECTED:
                findViewById(R.id.buttonSend).setVisibility(View.VISIBLE);
                findViewById(R.id.editText).setVisibility(View.VISIBLE);
                break;
            case STATE_DISCONNECTED:
                findViewById(R.id.buttonSend).setVisibility(View.GONE);
                findViewById(R.id.editText).setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
