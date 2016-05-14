package com.project_b.se2.mauerhuepfer;

import android.app.AlertDialog;
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
import android.text.method.ScrollingMovementMethod;
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
        View.OnClickListener,
        INetworkManager {

    /* ------------------------------------------------------------------------------------------ */

    private static final String TAG = "TEMP"; //MainActivity.class.getSimpleName();
    private NetworkManager mNetworkManager;

    /*
    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusText;
    private Button mHostButton;
    private Button mJoinButton;
    private Button mSendButton;
    private Button mDiscoButton;
    private ListView mListView;
    private EditText mSendEditText;

    private ArrayAdapter<String> mMessageAdapter;

    //private boolean mIsHost;
    //private boolean mIsConnected;

    /* ------------------------------------------------------------------------------------------ */

    /**
     * Views and Dialogs
     **/
    private TextView mDebugInfo;
    private EditText mMessageText;
    //private AlertDialog mConnectionRequestDialog;
    //private MyListDialog mMyListDialog;
    /* ------------------------------------------------------------------------------------------ */

    /**
     * The current state of the application
     **/
    private int currentState = NetworkManager.STATE_IDLE;

    /* ------------------------------------------------------------------------------------------ */

    /**
     * The endpoint ID of the connected peer, used for messaging
     **//*
    private String mRemoteHostEndpoint;
    private List<String> mRemotePeerEndpoints = new ArrayList<String>();
    private List<String> adapterArr = new ArrayList<String>();

    /* ------------------------------------------------------------------------------------------ */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        // Button listeners
        findViewById(R.id.button_advertise).setOnClickListener(this);
        findViewById(R.id.button_discover).setOnClickListener(this);
        findViewById(R.id.button_send).setOnClickListener(this);

        // EditText
        mMessageText = (EditText) findViewById(R.id.edittext_message);

        // Debug text view
        mDebugInfo = (TextView) findViewById(R.id.debug_text);
        mDebugInfo.setMovementMethod(new ScrollingMovementMethod());

        // Initialize Google API Client for Nearby Connections. Note: if you are using Google+
        // sign-in with your project or any other API that requires Authentication you may want
        // to use a separate Google API Client for Nearby Connections.  This API does not
        // require the user to authenticate so it can be used even when the user does not want to
        // sign in or sign-in has failed.
        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Nearby.CONNECTIONS_API)
                .build();*/

        mNetworkManager = new NetworkManager(this);
        mNetworkManager.addMessageReceiverListener(this);
    }

    /* ------------------------------------------------------------------------------------------ */

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        mNetworkManager.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");

        mNetworkManager.disconnect();
    }

    /* ------------------------------------------------------------------------------------------ */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_advertise:
                mNetworkManager.startAdvertising();
                break;
            case R.id.button_discover:
                mNetworkManager.startDiscovery();
                break;
            case R.id.button_send:
                String msg = mMessageText.getText().toString();
                UpdateState s = new UpdateState();
                s.msg = msg;
                mNetworkManager.sendMessage(s);
                mMessageText.setText(null);
                break;
        }
    }

    /* ------------------------------------------------------------------------------------------ *

    @Override
    public void updateState(@NetworkManager.NearbyConnectionState int state) {
        updateViewVisibility(state);
    }

    /* ------------------------------------------------------------------------------------------ */

    /**
     * Change the application state and update the visibility on on-screen views '
     * based on the new state of the application.
     *
     * @param newState the state to move to (should be NearbyConnectionState)
     */
    public void updateState(@NetworkManager.NearbyConnectionState int newState) {
        currentState = newState;
        switch (currentState) {
            case NetworkManager.STATE_IDLE:
                // The GoogleAPIClient is not connected, we can't yet start advertising or
                // discovery so hide all buttons
                findViewById(R.id.layout_nearby_buttons).setVisibility(View.GONE);
                findViewById(R.id.layout_message).setVisibility(View.GONE);
                mDebugInfo.append("\n State: IDLE");
                break;
            case NetworkManager.STATE_READY:
                // The GoogleAPIClient is connected, we can begin advertising or discovery.
                findViewById(R.id.layout_nearby_buttons).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_message).setVisibility(View.GONE);
                mDebugInfo.append("\n State: READY");
                break;
            case NetworkManager.STATE_ADVERTISING:
                mDebugInfo.append("\n State: ADVERTISING");
                break;
            case NetworkManager.STATE_DISCOVERING:
                mDebugInfo.append("\n State: DISCOVERING");
                break;
            case NetworkManager.STATE_CONNECTED:
                mDebugInfo.append("\n State: CONNECTED");
                // We are connected to another device via the Connections API, so we can
                // show the message UI.
                findViewById(R.id.layout_nearby_buttons).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_message).setVisibility(View.VISIBLE);
                break;
        }
    }

    /* ------------------------------------------------------------------------------------------ */

    @Override
    public void receiveMessage(UpdateState status) {
        mDebugInfo.append("\n" + status.toString());
    }

    /* ------------------------------------------------------------------------------------------ */


















    /*
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
    */
}
