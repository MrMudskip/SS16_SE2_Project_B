package com.project_b.se2.mauerhuepfer;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
public class GameBoardActivity extends AppCompatActivity implements IReceiveMessage {

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
    private static INetworkManager mNetworkManager;

    Drawable tempImage;
    MediaPlayer but_sound;

    private int playerID;
    private String playerName;

    boolean diceOne = true;
    boolean moved = false;

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

        mNetworkManager = NetworkActivity.getmNetworkManager();

        if (mNetworkManager != null) {
            mNetworkManager.addMessageReceiverListener(this);

            Bundle b = getIntent().getExtras();
            if (b != null) {
                playerID = b.getInt("playerID");
                playerName = b.getString("playerName");
                infoText.setText(playerName + " du bist Spieler " + playerID);
            }
        }
    }

    private void clickOnDice1() {
        if (drag1 && !drag2) {
            Toast.makeText(getApplicationContext(), "Achtung, bereits gezogen!! ", Toast.LENGTH_SHORT).show();
        } else if (!drag1) {
            Toast.makeText(getApplicationContext(), "Ich ziehe " + randomDice1, Toast.LENGTH_SHORT).show();
        } else if (drag1 && drag2) {
            Toast.makeText(getApplicationContext(), "Bitte würfeln!! ", Toast.LENGTH_SHORT).show();
        }
        if (!drag1) {
            infoText.setText("Zu ziehen : " + randomDice2);
            moved = true;
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
        } else if (drag1 && drag2) {
            Toast.makeText(getApplicationContext(), "Bitte würfeln!! ", Toast.LENGTH_SHORT).show();
        }
        if (!drag2) {
            infoText.setText("Zu ziehen : " + randomDice1);
            drag2 = true;
            moved = true;
        }
        if (drag1 && drag2)
            infoText.setText("Bitte würfeln");
    }

    private void diceButton() {
        if (!moved) {
            if (!drag1 && !drag2) {
                Toast.makeText(getApplicationContext(), "Achtung, bereits gewürfelt du Schummler!! ", Toast.LENGTH_SHORT).show();
            }

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

            if (diceOne) {
                diceOne = false;
                UpdateState updateDice = new UpdateState();
                updateDice.setW1(randomDice1);
                updateDice.setW2(randomDice2);
                updateDice.setPlayer(playerName);
                updateDice.setUsage(USAGE_DICE);
                mNetworkManager.sendMessage(updateDice);
            }
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

    @Override
    public void receiveMessage(UpdateState status) {
        //TODO: UPDATE GAMESTATES

        if (status != null) {

            if (status.getUsage() == USAGE_DICE) {
                ImageView image1 = (ImageView) findViewById(R.id.wuerfel);
                rollDice(status.getW1());
                image1.setImageDrawable(tempImage);

                ImageView image2 = (ImageView) findViewById(R.id.wuerfel2);
                rollDice(status.getW2());
                image2.setImageDrawable(tempImage);
                infoText.setText(status.getPlayer() + " würfelt : " + status.getW1() + " und: " + status.getW2());
            }

            if (status.getUsage() == USAGE_RESTART) {
                Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }

            if (status.getUsage() == USAGE_NEXTPLAYER) {
                diceOne = true;
                moved = false;
            }
        }
    }

    /**
     * Disable Back bitton
     */
    @Override
    public void onBackPressed() {
        Log.d("GAME", "onBackPressed");
        // Do nothing
    }

    @Override
    public void onDestroy() {
        mNetworkManager.disconnect();
        super.onDestroy();
    }
}