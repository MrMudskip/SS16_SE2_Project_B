package com.project_b.se2.mauerhuepfer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

/**
 * Created by Puma on 10.06.16.
 */
public class Dice {

    // Dice attributes
    private Context context;
    private int dice1Value;
    private int dice2Value;
    private boolean isDice1ValueSelected = false;
    private boolean isDice2ValueSelected = false;
    private boolean isDice1ValueMoved = true;
    private boolean isDice2ValueMoved = true;
    private final int backgroundColor = Color.parseColor("#5b5533");

    public Dice(Context current){
        this.context = current;
    }

    private int getRandom(){
        return (int) (Math.random() * (6)) + 1;
    }

    public void throwDice(){
        this.dice1Value = getRandom();
        this.dice2Value = getRandom();
        this.isDice1ValueSelected = false;
        this.isDice2ValueSelected = false;
        this.isDice1ValueMoved = false;
        this.isDice2ValueMoved = false;
    }
    public int getDice1Value(){
        return this.dice1Value;
    }
    public int getDice2Value(){
        return this.dice2Value;
    }
    public int getBackgroundColor() {return this.backgroundColor;}
    public boolean checkDicePermission() {return (this.isDice1ValueMoved && this.isDice2ValueMoved);}
    public boolean isDice1ValueSelected() {return this.isDice1ValueSelected;}
    public boolean isDice2ValueSelected() {return this.isDice2ValueSelected;}
    public void selectDice1Value(){ this.isDice1ValueSelected = true;}
    public void selectDice2Value(){ this.isDice2ValueSelected = true;}
    public void deselectDice1Value(){ this.isDice1ValueSelected = false;}
    public void deselectDice2Value(){ this.isDice2ValueSelected = false;}
    public void moveDice1Value(){ this.isDice1ValueMoved = true;}
    public void moveDive2Value(){ this.isDice2ValueMoved = true;}
    public boolean isDice1ValueMoved() { return this.isDice1ValueMoved;}
    public boolean isDice2ValueMoved() { return this.isDice2ValueMoved;}

    public Drawable getDiceImage(int zu){
        Drawable temp = null;
        switch (zu){

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

}
