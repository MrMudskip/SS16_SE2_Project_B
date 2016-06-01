package com.project_b.se2.mauerhuepfer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by Anita on 03.05.2016.
 */
public class Game {

    //Player colours
    static final int RED = 0;
    static final int GREEN = 1;
    static final int YELLOW = 2;
    static final int BLACK = 3;

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
    private CustomGameBoardView gameBoardView;
    private int unit;
    private int numberOfPlayers;
    private Player[] players;

    private int dice1;
    private int dice2;
    private int position;

    // 2D array containing all the blocks that make up the game field.     // TODO find a solution for different wall numbers.
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


    public Game(Context context, int numberOfPlayers) {
        this.context = context;
        this.resources = this.context.getResources();
        int heightPixels = this.resources.getDisplayMetrics().heightPixels;
        this.unit = (int) ((heightPixels / gameBoard.length) * 0.8);
        // TODO: Find a way to get view size instead of screen size, so the scaling with 0.8 isn't necessary.
        this.numberOfPlayers = numberOfPlayers;

        initializeGameBoard();
        initializePlayers();
        gameBoardView = (CustomGameBoardView)((Activity) context).findViewById(R.id.CustomGameBoardView);
        gameBoardView.setGameBoard(gameBoard);
    //    initializeGameBoardView();

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
        //Set images
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

        //Set starting figures //TODO This should use the players figures, not create new ones.
        Drawable figureImg;
        switch (type){
            case BR: {figureImg = resources.getDrawable(R.drawable.player_red);} break;
            case BG: {figureImg = resources.getDrawable(R.drawable.player_green);} break;
            case BY: {figureImg = resources.getDrawable(R.drawable.player_yellow);} break;
            case BB: {figureImg = resources.getDrawable(R.drawable.player_black);} break;
            default: figureImg = resources.getDrawable(R.drawable.empty);
        }
        figureImg.setBounds(heightPos, lengthPos, (heightPos + unit), (lengthPos + unit));
        Figure figure = new Figure();
        figure.setImage(figureImg);
        block.setOccupyingFigure(figure);
    }

    public void initializePlayers(){
        players = new Player[numberOfPlayers];
        // TODO Maybe let the player choose their own colour?
        for (int colour = 0; colour < numberOfPlayers; colour++) {
            players[colour] = new Player(context, colour);
        }
    }

    public void initializeGameBoardView(){
/*        LinearLayout rootLayout = (LinearLayout)((Activity) context).findViewById(R.id.root_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f    // weight = 1
        );
        params.gravity = (Gravity.CENTER);
        gameBoardView = new CustomGameBoardView(context, gameBoard);
        gameBoardView.setLayoutParams(params);
        rootLayout.addView(gameBoardView, 0); // index = 0
*/
       /*
        <FrameLayout
        android:layout_width="fill_parent"
        android:layout_height="wrap_content" >

        <com.proj.MyView
        android:id="@+id/board1"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"/>

        <com.proj.MyView
        android:id="@+id/board2"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content" />

        </FrameLayout>
        */
    }

    public void rollDice(){ //TODO Think of a better name for this method.
        // TODO Do something meaningful here.
/*        Drawable drawable = resources.getDrawable(R.drawable.player_black);
        int lengthPos = 0 * unit;
        int heightPos = 2 * unit;
        drawable.setBounds(heightPos, lengthPos, (heightPos + unit), (lengthPos + unit));
        gameBoard[2][0].setImage(drawable);

        //gameBoardView.setGameBoard(gameBoard);
        gameBoardView.invalidate();
*/
    }

}