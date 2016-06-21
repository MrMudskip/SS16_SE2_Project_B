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
    private static NetworkManager networkManager;
    private TextView debugInfo;
    private EditText messageText;

    /* ------------------------------------------------------------------------------------------ */

    public static INetworkManager getNetworkManager() {
        return networkManager;
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

        messageText = (EditText) findViewById(R.id.edittext_message);

        debugInfo = (TextView) findViewById(R.id.debug_text);
        debugInfo.setMovementMethod(new ScrollingMovementMethod());

        initNetwork(this);
        networkManager.addMessageReceiverListener(this);
    }

    private static synchronized void initNetwork(Context context) {
        networkManager = new NetworkManager(context);
    }

    /* ------------------------------------------------------------------------------------------ */

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        networkManager.connect();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed");
        networkManager.disconnect();
    }

    /* ------------------------------------------------------------------------------------------ */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_startGame:
                networkManager.stopAdvertising();
                UpdateState state = new UpdateState();
                state.setUsage(USAGE_PLAYER_ID);
                networkManager.sendPlayerIDs(state);
                startGame(null);

                UpdateState starterState = new UpdateState();
                starterState.setUsage(USAGE_START_GAME);
                starterState.setIntValue(networkManager.getNumberOfPlayers());
                networkManager.sendMessage(starterState);
                break;
            case R.id.button_advertise:
                networkManager.startAdvertising();
                break;
            case R.id.button_discover:
                networkManager.startDiscovery();
                break;
            case R.id.button_send:
                if (messageText.getText() != null && !"".equals(messageText.getText().toString())) {
                    String msg = messageText.getText().toString();
                    UpdateState s = new UpdateState();
                    s.setMsg(msg);
                    s.setUsage(USAGE_MSG);
                    s.setPlayerName(networkManager.getPlayerName());
                    networkManager.sendMessage(s);
                    debugInfo.append("\n " + s.getPlayerName() + ": " + s.getMsg());
                    messageText.setText(null);
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
                debugInfo.append("\n >>>>> MAUERHÜPFER <<<<<");
                break;
            case NetworkManager.STATE_ADVERTISING:
                debugInfo.append("\n Hosting...");
                break;
            case NetworkManager.STATE_DISCOVERING:
                debugInfo.append("\n looking for Host....");
                break;
            case NetworkManager.STATE_NONETWORK:
                findViewById(R.id.layout_nearby_buttons).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_message).setVisibility(View.GONE);
                findViewById(R.id.button_startGame).setVisibility(View.VISIBLE);
                debugInfo.append("\n no Network available!");
                break;
            case NetworkManager.STATE_CONNECTED:
                if (networkManager.getHostInfo()) {
                    findViewById(R.id.button_startGame).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.button_startGame).setVisibility(View.GONE);
                    debugInfo.append("\n Connected");
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
                    debugInfo.append("\n " + status.getPlayerName() + ": " + status.getMsg());
                    break;
                case USAGE_START_GAME:
                    startGame(status);
                    break;
                case USAGE_PLAYER_ID:
                    networkManager.setPlayerID(status.getPlayerID());
                    break;
                case USAGE_JOIN:
                    debugInfo.append("\n " + status.getMsg());
                    break;
                default:
                    Log.i(TAG, "unknown USAGE_CODE: " + status.getUsage());
            }
        } else {
            Log.e(TAG, "Connection Problem: receiveMessage");
        }
    }

    private void startGame(UpdateState status) {
        Intent intent = new Intent(NetworkActivity.this, GameBoardActivity.class);
        Bundle b = new Bundle();
        b.putInt("playerID", networkManager.getPlayerID());
        b.putString("playerName", networkManager.getPlayerName());
        if (status != null) {
            b.putInt("numberOfPlayers", status.getIntValue());
        } else {
            b.putInt("numberOfPlayers", networkManager.getNumberOfPlayers());
        }
        intent.putExtras(b);
        debugInfo.append("\n PlayerID: " + networkManager.getPlayerID());
        startActivity(intent);
    }

    /* ------------------------------------------------------------------------------------------ */
}
