package com.project_b.se2.mauerhuepfer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project_b.se2.mauerhuepfer.interfaces.INetworkManager;
import com.project_b.se2.mauerhuepfer.listener.ShakeDetector;

/**
 * Created by Puma on 10.06.16.
 */
public class Dice {

    // Dice attributes
    private Context context;
    private int dice1Value;
    private int dice2Value;
    /*private boolean isDice1ValueSelected = false;
    private boolean isDice2ValueSelected = false;
    private boolean isDice1ValueMoved = true;
    private boolean isDice2ValueMoved = true;*/
    private final int backgroundColor = Color.parseColor("#5b5533");
    private ImageView diceImage1;
    private ImageView diceImage2;
    private Button diceButton;
    private TextView infoText;
    private SensorManager mSensorManager;
    private ShakeDetector mSensorListener;
    private Game game;
    private boolean dice1Selected = false;
    private boolean dice2Selected = false;
    private boolean dice1removed = false;
    private boolean dice2removed = false;

    MediaPlayer but_sound;


    public Dice(Context current, Game game) {
        this.context = current;
        this.game = game;
        diceImage1 = (ImageView) ((Activity) context).findViewById(R.id.wuerfel);
        diceImage2 = (ImageView) ((Activity) context).findViewById(R.id.wuerfel2);

        infoText = (TextView) ((Activity) context).findViewById(R.id.textView);
        diceButton = (Button) ((Activity) context).findViewById(R.id.button1);

        diceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diceButton();
            }
        });
        diceImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dice1();
            }
        });
        diceImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dice2();
            }
        });
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeDetector();

        mSensorListener.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                if (true) {
                    diceButton();
                    but_sound = MediaPlayer.create(context, R.raw.klack);
                    but_sound.setVolume(1.0f, 1.0f);
                    but_sound.start();
                } else {
                    Toast.makeText(context, "Nicht möglich!! ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int getRandom() {
        return (int) (Math.random() * (6)) + 1;
    }

    public void throwDice() {
        this.dice1Value = getRandom();
        this.dice2Value = getRandom();
        dice1Selected = false;
        dice2Selected = false;
       /* this.isDice1ValueSelected = false;
        this.isDice2ValueSelected = false;
        this.isDice1ValueMoved = false;
        this.isDice2ValueMoved = false;*/
    }

    public void setDice1removed(boolean dice1removed) {
        this.dice1removed = dice1removed;
    }

    public void setDice2removed(boolean dice2removed) {
        this.dice2removed = dice2removed;
    }

    public boolean isDice2removed() {
        return dice2removed;
    }

    public boolean isDice1removed() {
        return dice1removed;
    }

    public ShakeDetector getmSensorListener() {
        return mSensorListener;
    }

    public SensorManager getmSensorManager() {
        return mSensorManager;
    }

    public boolean isDice1Selected() {
        return dice1Selected;
    }

    public boolean isDice2Selected() {
        return dice2Selected;
    }

    public int getDice1Value() {
        return this.dice1Value;
    }

    public int getDice2Value() {
        return this.dice2Value;
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    /*public boolean checkDicePermission() {
        return (this.isDice1ValueMoved && this.isDice2ValueMoved);
    }

    public boolean isDice1ValueSelected() {
        return this.isDice1ValueSelected;
    }

    public boolean isDice2ValueSelected() {
        return this.isDice2ValueSelected;
    }

    public void selectDice1Value() {
        this.isDice1ValueSelected = true;
    }

    public void selectDice2Value() {
        this.isDice2ValueSelected = true;
    }

    public void deselectDice1Value() {
        this.isDice1ValueSelected = false;
    }

    public void deselectDice2Value() {
        this.isDice2ValueSelected = false;
    }

    public void moveDice1Value() {
        this.isDice1ValueMoved = true;
    }

    public void moveDive2Value() {
        this.isDice2ValueMoved = true;
    }

    public boolean isDice1ValueMoved() {
        return this.isDice1ValueMoved;
    }

    public boolean isDice2ValueMoved() {
        return this.isDice2ValueMoved;
    }*/

    /*public void moveDiceValue() {
        int diceImageBackground = this.getBackgroundColor();

        if (isDice1ValueSelected()) {
            diceImage1.setColorFilter(diceImageBackground);
            this.moveDice1Value();
        }
        if (isDice2ValueSelected()) {
            diceImage2.setColorFilter(diceImageBackground);
            moveDive2Value();
        }
    }*/

    public void dice1Used() {
        int diceImageBackground = this.getBackgroundColor();
        diceImage1.setColorFilter(diceImageBackground);
        dice1removed = true;
    }

    public void dice2Used() {
        int diceImageBackground = this.getBackgroundColor();
        diceImage2.setColorFilter(diceImageBackground);
        dice2removed = true;
    }

    public void diceButton() {
        if (true) {
            throwDice();
            ImageView image1 = (ImageView) ((Activity) context).findViewById(R.id.wuerfel);
            image1.clearColorFilter();
            image1.setImageDrawable(getDiceImage(getDice1Value()));

            ImageView image2 = (ImageView) ((Activity) context).findViewById(R.id.wuerfel2);
            image2.clearColorFilter();
            image2.setImageDrawable(getDiceImage(getDice2Value()));

            infoText.setText(" ");
        } else {
            Toast.makeText(context, "Nicht möglich!! ", Toast.LENGTH_SHORT).show();
        }
    }

    public Drawable getDiceImage(int zu) {
        Drawable temp = null;
        switch (zu) {

            case 1:
                temp = context.getResources().getDrawable(R.drawable.w1n);
                break;
            case 2:
                temp = context.getResources().getDrawable(R.drawable.w2n);
                break;
            case 3:
                temp = context.getResources().getDrawable(R.drawable.w3n);
                break;
            case 4:
                temp = context.getResources().getDrawable(R.drawable.w4n);
                break;
            case 5:
                temp = context.getResources().getDrawable(R.drawable.w5n);
                break;
            case 6:
                temp = context.getResources().getDrawable(R.drawable.w6n);
                break;
        }
        return temp;
    }

   /* private void clickOnDice1() {
        if (checkDicePermission()) {
            Toast.makeText(context, "Bitte würfeln!! ", Toast.LENGTH_SHORT).show();
            infoText.setText("Bitte würfeln");
        }
        if (!isDice2ValueMoved() && !isDice1ValueMoved()) {
            //  diceImage2.clearColorFilter();
            deselectDice2Value();

        }
        if (!isDice1ValueSelected() && !isDice1ValueMoved()) {
            //int diceImage1Background = getBackgroundColor();
            diceImage1.setColorFilter(getBackgroundColor(), PorterDuff.Mode.MULTIPLY);
            infoText.setText("Mögliche Züge mit " + getDice1Value());
            game.setSelectedDiceNumber(getDice1Value());
            selectDice1Value();
        }
//        else if (isDice1ValueSelected() && !isDice1ValueMoved()) {
//            isDice1ValueSelected = true;
//        }
    }

    private void clickOnDice2() {
        if (checkDicePermission()) {
            Toast.makeText(context, "Bitte würfeln!! ", Toast.LENGTH_SHORT).show();
            infoText.setText("Bitte würfeln");
        }
        if (!isDice1ValueMoved() && !isDice2ValueMoved()) {
            //   diceImage1.clearColorFilter();
            deselectDice1Value();


        }
        if (!isDice2ValueSelected() && !isDice2ValueMoved()) {
            int diceImage2Background = getBackgroundColor();
            diceImage2.setColorFilter(diceImage2Background, PorterDuff.Mode.MULTIPLY);
            infoText.setText("Mögliche Züge mit " + getDice2Value());
            game.setSelectedDiceNumber(getDice2Value());
            selectDice2Value();
        }
//        else if (isDice2ValueSelected() && !isDice2ValueMoved()) {
//            isDice2ValueSelected = true;
//        }
    }*/

    private void dice1() {
        dice1Selected = true;
        dice2Selected = false;
        if (!dice2removed) {
            diceImage2.clearColorFilter();
            diceImage2.setImageDrawable(getDiceImage(getDice2Value()));
        }
        diceImage1.setColorFilter(getBackgroundColor(), PorterDuff.Mode.MULTIPLY);
        infoText.setText("Mögliche Züge mit " + getDice1Value());
        game.setSelectedDiceNumber(getDice1Value());
    }

    private void dice2() {
        dice1Selected = false;
        dice2Selected = true;
        if (!dice1removed) {
            diceImage1.clearColorFilter();
            diceImage1.setImageDrawable(getDiceImage(getDice1Value()));
        }
        diceImage2.setColorFilter(getBackgroundColor(), PorterDuff.Mode.MULTIPLY);
        infoText.setText("Mögliche Züge mit " + getDice2Value());
        game.setSelectedDiceNumber(getDice2Value());
    }


}
