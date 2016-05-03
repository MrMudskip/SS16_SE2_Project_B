package com.project_b.se2.mauerhuepfer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameBoardActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    // Dice attributes
    private int zufallWuerfel1;
    private int zufallWuerfel2;
    private ImageView image1;
    private ImageView image2;
    private int wurfCounter;
    private boolean drag1 = true;
    private boolean drag2 = true;
    private TextView text;
    private Button but;
    private SensorManager mgr;
    private float max = 0;

    Drawable neubild;
    MediaPlayer but_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_board);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        image1 = (ImageView) findViewById(R.id.wuerfel);
        image2 = (ImageView) findViewById(R.id.wuerfel2);
        text = (TextView) findViewById(R.id.textView);

        mgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mgr.registerListener(listener, mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        buttondruecken();
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!drag1) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Ich ziehe " + zufallWuerfel1, Toast.LENGTH_SHORT);
                    toast.show();
                }if (drag1 && drag2){
                    Toast toast = Toast.makeText(getApplicationContext(), "Bitte würfeln!! ", Toast.LENGTH_SHORT);
                    toast.show();
                }if(drag1 && !drag2){
                    Toast toast = Toast.makeText(getApplicationContext(), "Achtung, bereits gezogen!! ", Toast.LENGTH_SHORT);
                    toast.show();
                }
                if (!drag1) {
                    text.setText("Zu ziehen : " + zufallWuerfel2);
                    drag1 = true;
                }
                if (drag1 && drag2)
                    text.setText("Bitte würfeln");
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!drag2) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Ich ziehe " + zufallWuerfel2, Toast.LENGTH_SHORT);
                    toast.show();
                }if (drag1 && drag2){
                    Toast toast = Toast.makeText(getApplicationContext(), "Bitte würfeln!! ", Toast.LENGTH_SHORT);
                    toast.show();
                }if(drag2 && !drag1){
                    Toast toast = Toast.makeText(getApplicationContext(), "Achtung, bereits gezogen!! ", Toast.LENGTH_SHORT);
                    toast.show();
                }
                if (!drag2) {
                    text.setText("Zu ziehen : " + zufallWuerfel1);
                    drag2 = true;
                }
                if (drag1 && drag2)
                    text.setText("Bitte würfeln");
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
    private void buttondruecken() {

        but = (Button) findViewById(R.id.button1);
        but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (drag1 && drag2) {
                    ImageView bild = (ImageView) findViewById(R.id.wuerfel);
                    zufallWuerfel1 = (int) (Math.random() * (6)) + 1;
                    rollDice(zufallWuerfel1);
                    bild.setImageDrawable(neubild);

                    ImageView bild2 = (ImageView) findViewById(R.id.wuerfel2);
                    zufallWuerfel2 = (int) (Math.random() * (6)) + 1;
                    rollDice(zufallWuerfel2);
                    bild2.setImageDrawable(neubild);
                    text.setText("Zu ziehen : " + zufallWuerfel1 + " und: " + zufallWuerfel2);
                    drag1 = false;
                    drag2 = false;

                }else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Achtung, bereits gewürfelt!! ", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }
    private void rollDice(int zu){
        switch (zu){

            case 1:
                neubild = getResources().getDrawable(R.drawable.w1n);
                break;
            case 2:
                neubild = getResources().getDrawable(R.drawable.w2n);
                break;
            case 3:
                neubild = getResources().getDrawable(R.drawable.w3n);
                break;
            case 4:
                neubild = getResources().getDrawable(R.drawable.w4n);
                break;
            case 5:
                neubild = getResources().getDrawable(R.drawable.w5n);
                break;
            case 6:
                neubild = getResources().getDrawable(R.drawable.w6n);
                break;
        }
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mgr.unregisterListener(listener);
    }
    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
            {

                float x,y,z;
                x=event.values[0];

                if(x>max)
                { max=x; }

                if(max>10) {
                    if (drag1 && drag2) {
                        but_sound = MediaPlayer.create(GameBoardActivity.this, R.raw.klack);
                        but_sound.setVolume(1.0f, 1.0f);
                        but_sound.start();

                        ImageView bild = (ImageView) findViewById(R.id.wuerfel);
                        zufallWuerfel1 = (int) (Math.random() * (6)) + 1;
                        rollDice(zufallWuerfel1);
                        bild.setImageDrawable(neubild);

                        ImageView bild2 = (ImageView) findViewById(R.id.wuerfel2);
                        zufallWuerfel2 = (int) (Math.random() * (6)) + 1;
                        rollDice(zufallWuerfel2);
                        bild2.setImageDrawable(neubild);
                        text.setText("Zu ziehen : " + zufallWuerfel1 + " und: " + zufallWuerfel2);
                        drag1 = false;
                        drag2 = false;
                        max = 1;

                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Achtung, bereits gewürfelt!! ", Toast.LENGTH_SHORT);
                        toast.show();
                        max = 1;
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
