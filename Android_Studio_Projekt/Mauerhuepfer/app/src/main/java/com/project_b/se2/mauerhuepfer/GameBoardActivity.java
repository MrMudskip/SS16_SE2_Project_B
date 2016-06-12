package com.project_b.se2.mauerhuepfer;

import android.content.Context;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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



    private ImageView diceImage1;
    private ImageView diceImage2;
    private TextView infoText;
    private Button diceButton;
    private Dice dice;

    private SensorManager mSensorManager;
    private ShakeDetector mSensorListener;

    MediaPlayer but_sound;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_game_board);

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        // findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
        dice = new Dice(this);
        diceImage1 = (ImageView) findViewById(R.id.wuerfel);
        diceImage2 = (ImageView) findViewById(R.id.wuerfel2);
        infoText = (TextView) findViewById(R.id.textView);
        diceButton = (Button) findViewById(R.id.button1);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeDetector();

        mSensorListener.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                if (dice.checkDicePermission()) {
                    diceButton();
                    but_sound = MediaPlayer.create(GameBoardActivity.this, R.raw.klack);
                    but_sound.setVolume(1.0f, 1.0f);
                    but_sound.start();
                }else {
                    Toast.makeText(getApplicationContext(), "Nicht möglich!! ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        diceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diceButton();
            }
        });
        diceImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnDice1();
            }
        });
        diceImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnDice2();
            }
        });
    }

    private void clickOnDice1() {

        if (dice.checkDicePermission()) {
            Toast.makeText(getApplicationContext(), "Bitte würfeln!! ", Toast.LENGTH_SHORT).show();
            infoText.setText("Bitte würfeln");
        }
        if (!dice.isDice2ValueMoved() && !dice.isDice1ValueMoved()) {
            diceImage2.clearColorFilter();
            dice.deselectDice2Value();
        }
        if (!dice.isDice1ValueSelected()&& !dice.isDice1ValueMoved()) {
            int diceImage1Background = dice.getBackgroundColor();
            diceImage1.setColorFilter(diceImage1Background, PorterDuff.Mode.MULTIPLY);
            infoText.setText("Mögliche Züge mit " + dice.getDice1Value());
            dice.selectDice1Value();
        }
        else if (dice.isDice1ValueSelected() && !dice.isDice1ValueMoved()){
            moveDiceValue();
        }
    }

    private void clickOnDice2() {

        if (dice.checkDicePermission()) {
            Toast.makeText(getApplicationContext(), "Bitte würfeln!! ", Toast.LENGTH_SHORT).show();
            infoText.setText("Bitte würfeln");
        }
        if (!dice.isDice1ValueMoved() && !dice.isDice2ValueMoved()) {
            diceImage1.clearColorFilter();
            dice.deselectDice1Value();
        }
        if (!dice.isDice2ValueSelected() && !dice.isDice2ValueMoved()) {
            int diceImage2Background = dice.getBackgroundColor();
            diceImage2.setColorFilter(diceImage2Background, PorterDuff.Mode.MULTIPLY);
            infoText.setText("Mögliche Züge mit " + dice.getDice2Value());
            dice.selectDice2Value();
        }
        else if(dice.isDice2ValueSelected() && !dice.isDice2ValueMoved()){
            moveDiceValue();
        }
    }

    private void moveDiceValue() {
        int diceImageBackground = dice.getBackgroundColor();

        if(dice.isDice1ValueSelected()){
            diceImage1.setColorFilter(diceImageBackground);
            dice.moveDice1Value();
        }
        if(dice.isDice2ValueSelected()){
            diceImage2.setColorFilter(diceImageBackground);
            dice.moveDive2Value();
        }


    }


    private void diceButton() {
        if (dice.checkDicePermission()) {
            dice.throwDice();
            ImageView image1 = (ImageView) findViewById(R.id.wuerfel);
            image1.clearColorFilter();
            image1.setImageDrawable(dice.getDiceImage(dice.getDice1Value()));

            ImageView image2 = (ImageView) findViewById(R.id.wuerfel2);
            image2.clearColorFilter();
            image2.setImageDrawable(dice.getDiceImage(dice.getDice2Value()));

            infoText.setText(" ");
        } else {
            Toast.makeText(getApplicationContext(), "Nicht möglich!! ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }


}


