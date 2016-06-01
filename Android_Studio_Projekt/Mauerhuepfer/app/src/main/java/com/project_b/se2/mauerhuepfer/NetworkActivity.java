package com.project_b.se2.mauerhuepfer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.nearby.Nearby;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by rohrbe on 22.04.16.
 */
public class NetworkActivity extends AppCompatActivity implements
        View.OnClickListener,
        IUpdateView,
        IReceiveMessage {

    /* ------------------------------------------------------------------------------------------ */

    private static final String TAG = "NetworkActivity";
    public static NetworkManager mNetworkManager;
    private TextView mDebugInfo;
    private EditText mMessageText;
    private int currentState = NetworkManager.STATE_IDLE;

    /* ------------------------------------------------------------------------------------------ */

    public static INetworkManager getmNetworkManager() {
        return mNetworkManager;
    }

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
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed");
        mNetworkManager.disconnect();
    }

    /* ------------------------------------------------------------------------------------------ */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_startGame:
                mNetworkManager.stopAdvertising();

                UpdateState state = new UpdateState();
                state.setUsage(USAGE_PLAYERID);
                mNetworkManager.sendPlayerIDs(state);

                Intent intent = new Intent(NetworkActivity.this, GameBoardActivity.class);
                Bundle b = new Bundle();
                b.putInt("playerID", mNetworkManager.getPlayerID());
                b.putString("playerName", mNetworkManager.getPlayerName());
                intent.putExtras(b);
                mDebugInfo.append("\n PlayerID: " + mNetworkManager.getPlayerID());
                startActivity(intent);

                UpdateState starterState = new UpdateState();
                starterState.setUsage(USAGE_STARTGAME);
                mNetworkManager.sendMessage(starterState);
                break;
            case R.id.button_advertise:
                mNetworkManager.startAdvertising();
                break;
            case R.id.button_discover:
                mNetworkManager.startDiscovery();
                break;
            case R.id.button_send:
                if (mMessageText.getText() != null && !mMessageText.getText().toString().equals("")) {
                    String msg = mMessageText.getText().toString();
                    UpdateState s = new UpdateState();
                    s.setMsg(msg);
                    s.setUsage(USAGE_MSG);
                    s.setPlayer(mNetworkManager.getPlayerName());
                    mNetworkManager.sendMessage(s);
                    mDebugInfo.append("\n " + s.getPlayer() + ": " + s.getMsg());
                    mMessageText.setText(null);
                }
                break;
        }
    }

    /* ------------------------------------------------------------------------------------------ */

    @Override
    public void updateView(@NetworkManager.NearbyConnectionState int newState) {
        currentState = newState;
        switch (currentState) {
            case NetworkManager.STATE_IDLE:
                // The GoogleAPIClient is not connected, we can't yet start advertising or
                // discovery so hide all buttons
                findViewById(R.id.layout_nearby_buttons).setVisibility(View.GONE);
                findViewById(R.id.layout_message).setVisibility(View.GONE);
                findViewById(R.id.button_startGame).setVisibility(View.GONE);
                //mDebugInfo.append("\n State: IDLE");
                break;
            case NetworkManager.STATE_READY:
                // The GoogleAPIClient is connected, we can begin advertising or discovery.
                findViewById(R.id.layout_nearby_buttons).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_message).setVisibility(View.GONE);
                findViewById(R.id.button_startGame).setVisibility(View.GONE);
                mDebugInfo.append("\n >>>>> MAUERHÜPFER <<<<<");
                break;
            case NetworkManager.STATE_ADVERTISING:
                //mDebugInfo.append("\n State: ADVERTISING");
                mDebugInfo.append("\n Hosting...");
                break;
            case NetworkManager.STATE_DISCOVERING:
                //mDebugInfo.append("\n State: DISCOVERING");
                mDebugInfo.append("\n looking for Host....");
                break;
            case NetworkManager.STATE_NONETWORK:
                findViewById(R.id.layout_nearby_buttons).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_message).setVisibility(View.GONE);
                findViewById(R.id.button_startGame).setVisibility(View.GONE);
                mDebugInfo.append("\n no Network available!");
                break;
            case NetworkManager.STATE_CONNECTED:
                if (!mNetworkManager.getHostinfo()) {
                    mDebugInfo.append("\n CONNECTED");
                }
                findViewById(R.id.layout_nearby_buttons).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_message).setVisibility(View.VISIBLE);
                if (mNetworkManager.getHostinfo()) {
                    findViewById(R.id.button_startGame).setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void receiveMessage(UpdateState status) {
        if (status.getUsage() == USAGE_MSG) {
            mDebugInfo.append("\n " + status.getPlayer() + ": " + status.getMsg());
        }

        if (status.getUsage() == USAGE_STARTGAME) {
            Intent intent = new Intent(NetworkActivity.this, GameBoardActivity.class);
            Bundle b = new Bundle();
            b.putInt("playerID", mNetworkManager.getPlayerID());
            b.putString("playerName", mNetworkManager.getPlayerName());
            intent.putExtras(b);
            mDebugInfo.append("\n PlayerID: " + mNetworkManager.getPlayerID());
            startActivity(intent);
        }

        if (status.getUsage() == USAGE_PLAYERID) {
            mNetworkManager.setPlayerID(status.getPlayerID());
        }

        if (status.getUsage() == USAGE_JOIN) {
            mDebugInfo.append("\n " + status.getMsg());
        }
    }
}
