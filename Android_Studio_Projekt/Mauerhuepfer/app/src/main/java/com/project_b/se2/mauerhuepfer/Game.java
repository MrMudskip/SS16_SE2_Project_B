package com.project_b.se2.mauerhuepfer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Anita on 03.05.2016.
 */
public class Game {

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
    private Context context;
    private Resources resources;
    private int unit;

    private int dice1;
    private int dice2;
    private Player player;
    private int position;


    // 2D array containing all the blocks that make up the game field.     // TODO initiate all blocks of the game field and add their respective type.
    private Block[][] gameField = {
            {new Block(GB),new Block(GB),new Block(GB),new Block(GB),new Block(N),new Block(GG),new Block(GG),new Block(GG),new Block(GG)},
            {new Block(GY),new Block(GY),new Block(GY),new Block(GY),new Block(N),new Block(GR),new Block(GR),new Block(GR),new Block(GR)},
            {new Block(E),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(F)},
            {new Block(V),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W)},
            {new Block(E),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(F)},
            {new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(V)},
            {new Block(E),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(E)},
            {new Block(V),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W)},
            {new Block(E),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(E)},
            {new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(V)},
            {new Block(E),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(E)},
            {new Block(V),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W)},
            {new Block(E),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(S)},
            {new Block(BB),new Block(BB),new Block(BY),new Block(BY),new Block(N),new Block(BG),new Block(BG),new Block(BR),new Block(BR)},
            {new Block(BB),new Block(BB),new Block(BY),new Block(BY),new Block(N),new Block(BG),new Block(BG),new Block(BR),new Block(BR)},
    };

    public Game(Context context) {
        this.context = context;
        this.resources = this.context.getResources();
        this.unit = 40; // TODO compute unit size dynamically.

        if(initializeGameField()){
            drawGameField();
        }

    }

    public boolean initializeGameField(){
        for (int col = 0; col < gameField.length; col++) {
            for (int row = 0; row < gameField[col].length; row++) {
                setBlockParametersByType(gameField[col][row], gameField[col][row].getType(), col, row);
            }
        }
        return true;
    }


    public void setBlockParametersByType(Block block, int type, int col, int row) {
        Drawable drawable;
        switch (type) {
            case S: drawable = resources.getDrawable(R.drawable.circle_white_l_arrow); break;
            case H: drawable = resources.getDrawable(R.drawable.circle_white_rl); break;
 //           case E: drawable = resources.getDrawable(R.drawable.circle_white_rl); break;
            case V: drawable = resources.getDrawable(R.drawable.circle_white_ud); break;
            case F: drawable = resources.getDrawable(R.drawable.circle_blue_l); break;
            case W: drawable = resources.getDrawable(R.drawable.wall_1); break; // TODO handle different wall numbers
            case N: drawable = resources.getDrawable(R.drawable.empty); break;
            case BR: drawable = resources.getDrawable(R.drawable.circle_red); break;
            case BG: drawable = resources.getDrawable(R.drawable.circle_green); break;
            case BY: drawable = resources.getDrawable(R.drawable.circle_yellow); break;
            case BB: drawable = resources.getDrawable(R.drawable.circle_black); break;
            case GR: drawable = resources.getDrawable(R.drawable.circle_red); break;
            case GG: drawable = resources.getDrawable(R.drawable.circle_green); break;
            case GY: drawable = resources.getDrawable(R.drawable.circle_yellow); break;
            case GB: drawable = resources.getDrawable(R.drawable.circle_black); break;
            default: drawable = resources.getDrawable(R.drawable.empty);
            // TODO handle the other block types.
        }
        int lengthPos = col * unit;
        int heightPos = row * unit;
        drawable.setBounds(heightPos, lengthPos, (heightPos + unit), (lengthPos + unit));
        block.setImage(drawable);
    }

    public void drawGameField(){
        LinearLayout rootLayout = (LinearLayout)((Activity) context).findViewById(R.id.root_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f    // weight = 1
        );
        CustomGameBoardView mView = new CustomGameBoardView(context, gameField);
        mView.setLayoutParams(params);
        rootLayout.addView(mView, 0); // index = 0
    }




/*  //TODO resolve errors in this method.
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
*/

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
