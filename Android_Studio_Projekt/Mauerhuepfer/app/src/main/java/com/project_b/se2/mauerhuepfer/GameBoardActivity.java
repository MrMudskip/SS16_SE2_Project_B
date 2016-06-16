package com.project_b.se2.mauerhuepfer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project_b.se2.mauerhuepfer.interfaces.INetworkManager;
import com.project_b.se2.mauerhuepfer.interfaces.IReceiveMessage;
import com.project_b.se2.mauerhuepfer.listener.ShakeDetector;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameBoardActivity extends AppCompatActivity implements IReceiveMessage {

    private Game game;
    private Dice dice;
    private int numberOfPlayers;
    private int playerID;
    private String playerName;
    private INetworkManager mNetworkManager;
    private MyListDialog mMyListDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_game_board);
        mNetworkManager = NetworkActivity.getmNetworkManager();

        if (mNetworkManager != null) {
            mNetworkManager.addMessageReceiverListener(this);
            Bundle b = getIntent().getExtras();
            if (b != null) {
                playerID = b.getInt("playerID");
                playerName = b.getString("playerName");
                numberOfPlayers = b.getInt("numberOfPlayers");
            }

            // start a new game
            this.game = new Game(this, numberOfPlayers, mNetworkManager, playerID);
            dice = game.getDice();
            dice.printInfo(playerName + " du bist Spieler " + (playerID + 1));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        dice.getmSensorManager().registerListener(dice.getmSensorListener(),
                dice.getmSensorManager().getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        dice.getmSensorManager().unregisterListener(dice.getmSensorListener());
        super.onPause();
    }

    @Override
    public void receiveMessage(UpdateState status) {
        if (status != null) {
            if (status.getUsage() == USAGE_RESTART) {
                cancelGame();
            } else {
                game.handleUpdate(status);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("GAME", "onBackPressed");
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Spiel Beenden?")
                .setCancelable(false);

        mMyListDialog = new MyListDialog(this, builder, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mMyListDialog.getItemValue(which).equals("kill")) {
                    cancelGame();
                }
                mMyListDialog.dismiss();
            }
        });

        mMyListDialog.addItem("     JA ", "kill");
        mMyListDialog.addItem("    NEIN ", "doNothing");
        mMyListDialog.show();
    }

    private void cancelGame() {
        mNetworkManager.disconnect();
        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        mNetworkManager.disconnect();
        super.onDestroy();
    }
}
