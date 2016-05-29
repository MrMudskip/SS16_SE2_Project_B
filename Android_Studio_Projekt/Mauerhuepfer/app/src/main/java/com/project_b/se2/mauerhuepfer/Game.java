package com.project_b.se2.mauerhuepfer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Anita on 03.05.2016.
 */
public class Game {

    // Block types
    static final int S = 0;
    static final int H = 1;
    static final int EU = 2;
    static final int ER = 3;
    static final int ED = 4;
    static final int EL = 5;
    static final int V = 6;
    static final int F = 7;
    static final int W = 8;
    static final int N = 9;
    static final int BR = 10;
    static final int BG = 11;
    static final int BY = 12;
    static final int BB = 13;
    static final int GR = 14;
    static final int GG = 15;
    static final int GY = 16;
    static final int GB = 17;

    // Other variables
    private Context context;
    private Resources resources;
    private int gameboardWidth;
    private int gameBoardHeight;
    private int unit;

    private int dice1;
    private int dice2;
    private Player player;
    private int position;


    // 2D array containing all the blocks that make up the game field.     // TODO initiate all blocks of the game field and add their respective type.
    private Block[][] gameBoard = {
            {new Block(GB),new Block(GB),new Block(GB),new Block(GB),new Block(N),new Block(GG),new Block(GG),new Block(GG),new Block(GG)},
            {new Block(GY),new Block(GY),new Block(GY),new Block(GY),new Block(N),new Block(GR),new Block(GR),new Block(GR),new Block(GR)},
            {new Block(ER),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(F)},
            {new Block(V),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W)},
            {new Block(EU),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(ED)},
            {new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(V)},
            {new Block(ER),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(EL)},
            {new Block(V),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W)},
            {new Block(EU),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(ED)},
            {new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(V)},
            {new Block(ER),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(EL)},
            {new Block(V),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W),new Block(W)},
            {new Block(EU),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(H),new Block(S)},
            {new Block(BB),new Block(BB),new Block(BY),new Block(BY),new Block(N),new Block(BG),new Block(BG),new Block(BR),new Block(BR)},
            {new Block(BB),new Block(BB),new Block(BY),new Block(BY),new Block(N),new Block(BG),new Block(BG),new Block(BR),new Block(BR)},
    };

    public Game(Context context) {
        this.context = context;
        this.resources = this.context.getResources();
        int heightPixels = this.resources.getDisplayMetrics().heightPixels;
        this.unit = (int) ((heightPixels / gameBoard.length) * 0.8);
        // TODO: Find a way to get view size instead of screen size, so the scaling with " * 0.8" isn't necessary.

        initializeGameBoard();
        initializeGameBoardView();

    }

    public boolean initializeGameBoard(){
        for (int col = 0; col < gameBoard.length; col++) {
            for (int row = 0; row < gameBoard[col].length; row++) {
                setBlockParametersByType(gameBoard[col][row], gameBoard[col][row].getType(), col, row);
            }
        }
        return true;
    }


    public void setBlockParametersByType(Block block, int type, int col, int row) {
        Drawable drawable;
        switch (type) {
            case S: drawable = resources.getDrawable(R.drawable.circle_white_l_arrow); break;
            case H: drawable = resources.getDrawable(R.drawable.circle_white_rl); break;
            case EU: drawable = resources.getDrawable(R.drawable.circle_white_ur); break;
            case ER: drawable = resources.getDrawable(R.drawable.circle_white_rd); break;
            case ED: drawable = resources.getDrawable(R.drawable.circle_white_dl); break;
            case EL: drawable = resources.getDrawable(R.drawable.circle_white_ul); break;
            case V: drawable = resources.getDrawable(R.drawable.circle_white_ud); break;
            case F: drawable = resources.getDrawable(R.drawable.circle_blue_l); break;
            case W: drawable = resources.getDrawable(R.drawable.wall_6); break; // TODO handle different wall numbers
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
        }
        int lengthPos = col * unit;
        int heightPos = row * unit;
        drawable.setBounds(heightPos, lengthPos, (heightPos + unit), (lengthPos + unit));
        block.setImage(drawable);
    }

    public void initializeGameBoardView(){
        LinearLayout rootLayout = (LinearLayout)((Activity) context).findViewById(R.id.root_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f    // weight = 1
        );
        params.gravity = (Gravity.CENTER);
        CustomGameBoardView mView = new CustomGameBoardView(context, gameBoard);
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
