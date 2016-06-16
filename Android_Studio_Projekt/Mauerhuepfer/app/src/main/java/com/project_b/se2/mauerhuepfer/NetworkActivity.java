package com.project_b.se2.mauerhuepfer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.project_b.se2.mauerhuepfer.interfaces.INetworkManager;
import com.project_b.se2.mauerhuepfer.interfaces.IReceiveMessage;
import com.project_b.se2.mauerhuepfer.interfaces.IUpdateView;

/**
 * Created by rohrbe on 22.04.16.
 */
public class NetworkActivity extends AppCompatActivity implements
        View.OnClickListener,
        IUpdateView,
        IReceiveMessage {

    /* ------------------------------------------------------------------------------------------ */

    private static final String TAG = NetworkActivity.class.getSimpleName();
    private static NetworkManager mNetworkManager;
    private TextView mDebugInfo;
    private EditText mMessageText;

    /* ------------------------------------------------------------------------------------------ */

    public static INetworkManager getmNetworkManager() {
        return mNetworkManager;
    }

    /* ------------------------------------------------------------------------------------------ */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_network);

        findViewById(R.id.button_advertise).setOnClickListener(this);
        findViewById(R.id.button_discover).setOnClickListener(this);
        findViewById(R.id.button_send).setOnClickListener(this);
        findViewById(R.id.button_startGame).setOnClickListener(this);
        findViewById(R.id.button_startGame).setVisibility(View.GONE);

        mMessageText = (EditText) findViewById(R.id.edittext_message);

        mDebugInfo = (TextView) findViewById(R.id.debug_text);
        mDebugInfo.setMovementMethod(new ScrollingMovementMethod());

        initNetwork(this);
        mNetworkManager.addMessageReceiverListener(this);
    }

    private static synchronized void initNetwork(Context context) {
        mNetworkManager = new NetworkManager(context);
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
                startGame(null);

                UpdateState starterState = new UpdateState();
                starterState.setUsage(USAGE_STARTGAME);
                starterState.setIntValue(mNetworkManager.getNumberOfPlayers());
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
                    s.setPlayerName(mNetworkManager.getPlayerName());
                    mNetworkManager.sendMessage(s);
                    mDebugInfo.append("\n " + s.getPlayerName() + ": " + s.getMsg());
                    mMessageText.setText(null);
                }
                break;
            default:
                Log.e(TAG, "unknown Button");
        }
    }

    /* ------------------------------------------------------------------------------------------ */

    @Override
    public void updateView(@NetworkManager.NearbyConnectionState int newState) {
        switch (newState) {
            case NetworkManager.STATE_IDLE:
                findViewById(R.id.layout_nearby_buttons).setVisibility(View.GONE);
                findViewById(R.id.layout_message).setVisibility(View.GONE);
                findViewById(R.id.button_startGame).setVisibility(View.GONE);
                break;
            case NetworkManager.STATE_READY:
                findViewById(R.id.layout_nearby_buttons).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_message).setVisibility(View.GONE);
                findViewById(R.id.button_startGame).setVisibility(View.VISIBLE);
                mDebugInfo.append("\n >>>>> MAUERHÜPFER <<<<<");
                break;
            case NetworkManager.STATE_ADVERTISING:
                mDebugInfo.append("\n Hosting...");
                break;
            case NetworkManager.STATE_DISCOVERING:
                mDebugInfo.append("\n looking for Host....");
                break;
            case NetworkManager.STATE_NONETWORK:
                findViewById(R.id.layout_nearby_buttons).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_message).setVisibility(View.GONE);
                findViewById(R.id.button_startGame).setVisibility(View.VISIBLE);
                mDebugInfo.append("\n no Network available!");
                break;
            case NetworkManager.STATE_CONNECTED:
                if (mNetworkManager.getHostinfo()) {
                    findViewById(R.id.button_startGame).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.button_startGame).setVisibility(View.GONE);
                    mDebugInfo.append("\n CONNECTED");
                }
                findViewById(R.id.layout_nearby_buttons).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_message).setVisibility(View.VISIBLE);
                break;
            default:
                Log.e(TAG, "unknown STATE");
        }
    }

    /* ------------------------------------------------------------------------------------------ */

    @Override
    public void receiveMessage(UpdateState status) {
        if (status != null) {
            switch (status.getUsage()) {
                case USAGE_MSG:
                    mDebugInfo.append("\n " + status.getPlayerName() + ": " + status.getMsg());
                    break;
                case USAGE_STARTGAME:
                    startGame(status);
                    break;
                case USAGE_PLAYERID:
                    mNetworkManager.setPlayerID(status.getPlayerID());
                    break;
                case USAGE_JOIN:
                    mDebugInfo.append("\n " + status.getMsg());
                    break;
                default:
                    Log.e(TAG, "unknown USAGE_CODE");
            }
        } else {
            mDebugInfo.append("\n CONNECTION ERROR");
        }
    }

    private void startGame(UpdateState status) {
        Intent intent = new Intent(NetworkActivity.this, GameBoardActivity.class);
        Bundle b = new Bundle();
        b.putInt("playerID", mNetworkManager.getPlayerID());
        b.putString("playerName", mNetworkManager.getPlayerName());
        if (status != null) {
            b.putInt("numberOfPlayers", status.getIntValue());
        } else {
            b.putInt("numberOfPlayers", mNetworkManager.getNumberOfPlayers());
        }
        intent.putExtras(b);
        mDebugInfo.append("\n PlayerID: " + mNetworkManager.getPlayerID());
        startActivity(intent);
    }

    /* ------------------------------------------------------------------------------------------ */
}
