package com.project_b.se2.mauerhuepfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by rohrbe on 22.04.16.
 */
public class NetworkActivity extends AppCompatActivity implements
        View.OnClickListener,
        ICommunication {

    /* ------------------------------------------------------------------------------------------ */

    private static final String TAG = "TEMP"; //MainActivity.class.getSimpleName();
    private NetworkManager mNetworkManager;

    /* ------------------------------------------------------------------------------------------ */

    /**
     * Views and Dialogs
     **/
    private TextView mDebugInfo;
    private EditText mMessageText;

    /* ------------------------------------------------------------------------------------------ */

    /**
     * The current state of the application
     **/
    private int currentState = NetworkManager.STATE_IDLE;

    /* ------------------------------------------------------------------------------------------ */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        // Button listeners
        findViewById(R.id.button_advertise).setOnClickListener(this);
        findViewById(R.id.button_discover).setOnClickListener(this);
        findViewById(R.id.button_send).setOnClickListener(this);
        findViewById(R.id.button_startGame).setOnClickListener(this);

        // EditText
        mMessageText = (EditText) findViewById(R.id.edittext_message);

        // Debug text view
        mDebugInfo = (TextView) findViewById(R.id.debug_text);
        mDebugInfo.setMovementMethod(new ScrollingMovementMethod());

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
            case R.id.button_startGame:
                Intent myIntent = new Intent(NetworkActivity.this, GameBoardActivity.class);
                NetworkActivity.this.startActivity(myIntent);
                // ------------------------------------------- //
                //TODO: Game für Clients Starten
                UpdateState starterState = new UpdateState();
                starterState.setMsg("Start");
                starterState.setPosition(815);
                mNetworkManager.sendMessage(starterState);
                // ------------------------------------------- //
                break;
            case R.id.button_advertise:
                mNetworkManager.startAdvertising();
                break;
            case R.id.button_discover:
                mNetworkManager.startDiscovery();
                break;
            case R.id.button_send:
                String msg = mMessageText.getText().toString();
                UpdateState s = new UpdateState();
                s.setMsg(msg);
                mNetworkManager.sendMessage(s);
                mDebugInfo.append("\n" + s.toString()); // Damit auch der Sender die nachricht sieht... evtl zu entfernen
                mMessageText.setText(null);
                break;
        }
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
                findViewById(R.id.button_startGame).setVisibility(View.GONE);
                findViewById(R.id.layout_nearby_buttons).setVisibility(View.GONE);
                findViewById(R.id.layout_message).setVisibility(View.GONE);
                mDebugInfo.append("\n State: IDLE");
                break;
            case NetworkManager.STATE_READY:
                // The GoogleAPIClient is connected, we can begin advertising or discovery.
                findViewById(R.id.button_startGame).setVisibility(View.GONE);
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
                if (mNetworkManager.getHostinfo()) {
                    findViewById(R.id.button_startGame).setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    /* ------------------------------------------------------------------------------------------ */

    @Override
    public void receiveMessage(UpdateState status) {
        mDebugInfo.append("\n" + status.toString());

        //TODO: Client Start Game
        if (status.getMsg().equals("Start") && status.getPosition() == 815 && !mNetworkManager.getHostinfo()) {

        }
    }

    /* ------------------------------------------------------------------------------------------ */

}
