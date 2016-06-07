package com.project_b.se2.mauerhuepfer;

import android.content.Context;
import android.graphics.drawable.Drawable;
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


    // Dice attributes
    private int randomDice1;
    private int randomDice2;
    private ImageView diceImage1;
    private ImageView diceImage2;
    private boolean drag1 = true;
    private boolean drag2 = true;
    private TextView infoText;
    private Button diceButton;
    private SensorManager mgr;
    private float max = 0;

    private SensorManager mSensorManager;
    private ShakeDetector mSensorListener;

    Drawable tempImage;
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

        diceImage1 = (ImageView) findViewById(R.id.wuerfel);
        diceImage2 = (ImageView) findViewById(R.id.wuerfel2);
        infoText = (TextView) findViewById(R.id.textView);
        diceButton = (Button) findViewById(R.id.button1);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeDetector();

        mSensorListener.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                if (drag1 && drag2) {
                    diceButton();
                    but_sound = MediaPlayer.create(GameBoardActivity.this, R.raw.klack);
                    but_sound.setVolume(1.0f, 1.0f);
                    but_sound.start();
                } else {
                    Toast.makeText(getApplicationContext(), "Achtung, bereits gewürfelt!! ", Toast.LENGTH_SHORT).show();
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


        // start a new game
        int numberOfPlayers = 4; // TODO decide this dynamically.
        this.game = new Game(this, numberOfPlayers);
    }


    private void clickOnDice1() {
        if (drag1 && !drag2) {
            Toast.makeText(getApplicationContext(), "Achtung, bereits gezogen!! ", Toast.LENGTH_SHORT).show();
        } else if (!drag1) {
            Toast.makeText(getApplicationContext(), "Ich ziehe " + randomDice1, Toast.LENGTH_SHORT).show();
            game.setSelectedDiceNumber(randomDice1); // TODO check if this is the right playce to call this.
        } else if (drag1 && drag2) {
            Toast.makeText(getApplicationContext(), "Bitte würfeln!! ", Toast.LENGTH_SHORT).show();
        }
        if (!drag1) {
            infoText.setText("Zu ziehen : " + randomDice2);
            drag1 = true;
        }
        if (drag1 && drag2)
            infoText.setText("Bitte würfeln");
    }

    private void clickOnDice2() {
        if (drag2 && !drag1) {
            Toast.makeText(getApplicationContext(), "Achtung, bereits gezogen!! ", Toast.LENGTH_SHORT).show();
        } else if (!drag2) {
            Toast.makeText(getApplicationContext(), "Ich ziehe " + randomDice2, Toast.LENGTH_SHORT).show();
            game.setSelectedDiceNumber(randomDice2); // TODO check if this is the right playce to call this.
        } else if (drag1 && drag2) {
            Toast.makeText(getApplicationContext(), "Bitte würfeln!! ", Toast.LENGTH_SHORT).show();
        }
        if (!drag2) {
            infoText.setText("Zu ziehen : " + randomDice1);
            drag2 = true;
        }
        if (drag1 && drag2)
            infoText.setText("Bitte würfeln");
    }


    private void diceButton() {
        if (drag1 && drag2) {
            ImageView image1 = (ImageView) findViewById(R.id.wuerfel);
            randomDice1 = (int) (Math.random() * (6)) + 1;
            rollDice(randomDice1);
            image1.setImageDrawable(tempImage);

            ImageView image2 = (ImageView) findViewById(R.id.wuerfel2);
            randomDice2 = (int) (Math.random() * (6)) + 1;
            rollDice(randomDice2);
            image2.setImageDrawable(tempImage);
            infoText.setText("Zu ziehen : " + randomDice1 + " und: " + randomDice2);
            drag1 = false;
            drag2 = false;
        } else {
            Toast.makeText(getApplicationContext(), "Achtung, bereits gewürfelt!! ", Toast.LENGTH_SHORT).show();
        }
    }


    private void rollDice(int zu) {
        switch (zu) {

            case 1:
                tempImage = getResources().getDrawable(R.drawable.w1n);
                break;
            case 2:
                tempImage = getResources().getDrawable(R.drawable.w2n);
                break;
            case 3:
                tempImage = getResources().getDrawable(R.drawable.w3n);
                break;
            case 4:
                tempImage = getResources().getDrawable(R.drawable.w4n);
                break;
            case 5:
                tempImage = getResources().getDrawable(R.drawable.w5n);
                break;
            case 6:
                tempImage = getResources().getDrawable(R.drawable.w6n);
                break;
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


