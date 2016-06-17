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
import com.project_b.se2.mauerhuepfer.interfaces.IReceiveMessage;
import com.project_b.se2.mauerhuepfer.listener.ShakeDetector;

/**
 * Created by Puma on 10.06.16.
 */
public class Dice {

    // Dice attributes
    private Context context;
    private int dice1Value;
    private int dice2Value;
    private final int backgroundColor = Color.parseColor("#5b5533");
    private ImageView diceImage1;
    private ImageView diceImage2;
    private Button diceButton;
    private TextView infoText;
    private SensorManager mSensorManager;
    private ShakeDetector mSensorListener;
    private Game game;
    private INetworkManager networkManager;
    private UpdateState update = new UpdateState();

    private boolean dice1Selected = false;
    private boolean dice2Selected = false;
    private boolean dice1removed = false;
    private boolean dice2removed = false;
    private boolean firstDiceRollThisTurn = true;
    private boolean diceRollAllowed = false;
    private boolean hasCheated = false;

    private String playerName;
    MediaPlayer but_sound;

    public Dice(Context current, Game game, INetworkManager networkManager, String playerName) {
        this.context = current;
        this.game = game;
        this.networkManager = networkManager;
        this.playerName = playerName;

        diceImage1 = (ImageView) ((Activity) context).findViewById(R.id.wuerfel);
        diceImage2 = (ImageView) ((Activity) context).findViewById(R.id.wuerfel2);

        diceImage1.setVisibility(View.INVISIBLE);
        diceImage2.setVisibility(View.INVISIBLE);

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
                if (diceRollAllowed) {
                    diceButton();
                    but_sound = MediaPlayer.create(context, R.raw.klack);
                    but_sound.setVolume(1.0f, 1.0f);
                    but_sound.start();
                } else {
                    Toast.makeText(context, "Du bist nicht an der Reihe!", Toast.LENGTH_SHORT).show();
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
    }

    public void setFirstDiceRollThisTurn(boolean firstDiceRollThisTurn) {
        this.firstDiceRollThisTurn = firstDiceRollThisTurn;
    }

    public void setDiceRollAllowed(boolean diceRollAllowed) {
        this.diceRollAllowed = diceRollAllowed;
    }

    public void setHasCheated(boolean hasCheated) {
        this.hasCheated = hasCheated;
    }

    public boolean isHasCheated() {
        return hasCheated;
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

    public void setDice1Selected(boolean dice1Selected) {
        this.dice1Selected = dice1Selected;
    }

    public void setDice2Selected(boolean dice2Selected) {
        this.dice2Selected = dice2Selected;
    }

    public int getDice1Value() {
        return this.dice1Value;
    }

    public int getDice2Value() {
        return this.dice2Value;
    }

    public void dice1Used() {
        int diceImageBackground = backgroundColor;
        diceImage1.setColorFilter(diceImageBackground);
        diceImage1.setVisibility(View.INVISIBLE);
        diceRollAllowed = false;
        dice1removed = true;
    }

    public void dice2Used() {
        int diceImageBackground = backgroundColor;
        diceImage2.setColorFilter(diceImageBackground);
        diceImage2.setVisibility(View.INVISIBLE);
        diceRollAllowed = false;
        dice2removed = true;
    }

    public void diceButton() {
        if (diceRollAllowed) {
            throwDice();
            setDiceImage(dice1Value, 1);
            setDiceImage(dice2Value, 2);
            infoText.setText(" ");

            if (firstDiceRollThisTurn) {
                firstDiceRollThisTurn = false;
                update.setW1(dice1Value);
                update.setW2(dice2Value);
                update.setPlayerName(playerName);
                update.setUsage(IReceiveMessage.USAGE_DICE_ROLLED);
                networkManager.sendMessage(update);
            } else if (!hasCheated){
                Toast.makeText(context, "Du hast bereits gewürfelt du Schummler! ", Toast.LENGTH_SHORT).show();
                hasCheated = true;
            }
        } else {
            if (game.isGameWon()){
                Toast.makeText(context, "Game Over", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Du bist nicht an der Reihe!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void setDiceImage(int diceValue, int dice) {
        if (dice == 1) {
            ImageView image = (ImageView) ((Activity) context).findViewById(R.id.wuerfel);
            image.clearColorFilter();
            image.setImageDrawable(getDiceImage(diceValue));
            diceImage1.setVisibility(View.VISIBLE);
        } else {
            ImageView image = (ImageView) ((Activity) context).findViewById(R.id.wuerfel2);
            image.clearColorFilter();
            image.setImageDrawable(getDiceImage(diceValue));
            diceImage2.setVisibility(View.VISIBLE);
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

    private void dice1() {
        if (game.getPlayers()[game.getCurrentPlayerIndex()].getPID() == game.getMyPID()) { // It's my turn
            dice1Selected = true;
            dice2Selected = false;
            if (!dice2removed) {
                diceImage2.clearColorFilter();
                diceImage2.setImageDrawable(getDiceImage(getDice2Value()));
            }
            diceImage1.setColorFilter(backgroundColor, PorterDuff.Mode.MULTIPLY);
            infoText.setText("Mögliche Züge mit " + getDice1Value());
            game.setSelectedDiceNumber(getDice1Value(), 1, true);
        }
    }

    private void dice2() {
        if (game.getPlayers()[game.getCurrentPlayerIndex()].getPID() == game.getMyPID()) { // It's my turn
            dice1Selected = false;
            dice2Selected = true;
            if (!dice1removed) {
                diceImage1.clearColorFilter();
                diceImage1.setImageDrawable(getDiceImage(getDice1Value()));
            }
            diceImage2.setColorFilter(backgroundColor, PorterDuff.Mode.MULTIPLY);
            infoText.setText("Mögliche Züge mit " + getDice2Value());
            game.setSelectedDiceNumber(getDice2Value(), 2, true);
        }
    }

    public void printInfo(String msg) {
        infoText.setText(msg);
    }
}
