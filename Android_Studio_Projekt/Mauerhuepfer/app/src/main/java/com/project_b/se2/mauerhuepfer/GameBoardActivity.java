package com.project_b.se2.mauerhuepfer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
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

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameBoardActivity extends AppCompatActivity {

    //TODO Delete if no longer needed.
//    private View mContentView;
//    private View mControlsView;

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

        // remove activity title
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_game_board);

        //TODO Delete if no longer needed.
//        mVisible = true;
//        mControlsView = findViewById(R.id.fullscreen_content_controls);
//        mContentView = findViewById(R.id.fullscreen_content);


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
