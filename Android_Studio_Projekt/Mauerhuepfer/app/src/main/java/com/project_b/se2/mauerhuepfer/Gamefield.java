package com.project_b.se2.mauerhuepfer;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
/**
 * Created by Anita on 03.05.2016.
 */
public class Gamefield {

    // Block types
    static final int S = 0;
    static final int H = 1;
    static final int E = 2;
    static final int V = 3;
    static final int F = 4;
    static final int W = 5;
    static final int N = 6;
    static final int BR = 7;
    static final int BG = 8;
    static final int BY = 9;
    static final int BB = 10;
    static final int GR = 11;
    static final int GG = 12;
    static final int GY = 13;
    static final int GB = 14;

    // Other variables
    private int dice1;
    private int dice2;
    private Spieler player;
    private int position;
    private int[] block = {};        //Height, Length
    private Block house;
    private Block field;
    private Block fin;

    // 2D array containing all the blocks that make up the game field.
    // TODO initiate all blocks of the game field.
    private Block[][] gameField = {
            {new Block(H),new Block(H),new Block(H),new Block(H)},
            {new Block(H),new Block(H),new Block(H),new Block(H)},
            {new Block(H),new Block(H),new Block(H),new Block(H)},
            {new Block(H),new Block(H),new Block(H),new Block(H)},
            {new Block(H),new Block(H),new Block(H),new Block(H)},
            {new Block(H),new Block(H),new Block(H),new Block(H)},
            {new Block(H),new Block(H),new Block(H),new Block(H)},
            {new Block(H),new Block(H),new Block(H),new Block(H)},
    };

    public Gamefield(Spieler player) {
        this.player=player;
        this.position=-1;
    }

    public void setBlockParametersByType(Block block, int type) {
        switch (type) {
            case S: {
                // TODO handle this type.
            }
            case H: {
                Resources res = mContext.getResources(); // TODO execute this on some higher level (only once).
                Drawable img = res.getDrawable(R.drawable.w1n); // TODO Replace placeholder image
                block.setImage(img);
            }
            // TODO handle the other block types.
        }
    }

    public void drawGameField(){

        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[i].length; j++) {

            }
        }
    }

    public void moveFigure(){
        player.figureOnField();
        for (int i=0; i<block.length; i++){
            int dice = dice1+dice2;
            position=position+dice;
            if (){                                  //position free? is the figure one of mine?
                player.                             //figure is not mine
            } else {
                if (player.getFigureID()<4) {                            //do i have another figure on position -1?
                    player.figureOnField();
                    player.moveFigure(position);
                } else {
                    player.moveFigure(position);
                }
            }

        }
    }

    public void finish(){

    }

    public int getDice1() {
        return dice1;
    }

    public int getDice2() {
        return dice2;
    }

    /*public int getOwnerID() {
        return ownerID;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }*/
}
