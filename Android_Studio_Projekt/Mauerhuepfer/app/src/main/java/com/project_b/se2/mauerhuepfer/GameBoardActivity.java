package com.project_b.se2.mauerhuepfer;

import android.content.Context;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project_b.se2.mauerhuepfer.listener.ShakeDetector;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameBoardActivity extends AppCompatActivity {

    private Game game;
    private Dice dice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_game_board);

        // start a new game
        int numberOfPlayers = 4; // TODO decide this dynamically (@Bernhard).
        this.game = new Game(this, numberOfPlayers);
        dice = game.getDice();
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


}


