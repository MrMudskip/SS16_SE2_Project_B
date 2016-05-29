package com.project_b.se2.mauerhuepfer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by Julian Janda on 27.05.2016.
 */
public class CustomGameBoardView extends View {

    private Block[][] gameBoard;


    public CustomGameBoardView(Context context, Block[][] gameBoard) {
        super(context);
        this.gameBoard = gameBoard;
    }

    protected void onDraw(Canvas canvas) {
        //Draw gameBoard
        for (int col = 0; col < gameBoard.length; col++) {
            for (int row = 0; row < gameBoard[col].length; row++) {
                gameBoard[col][row].getImage().draw(canvas);
            }
        }

        //Draw players
        for (int col = 0; col < gameBoard.length; col++) {
            for (int row = 0; row < gameBoard[col].length; row++) {
                //TODO This should use an ID system or something similar.
                //TODO Delete experimental code.
                // ***** Experimental code below, delete later. *****
                if (gameBoard[col][row].getType() == 13) {  //BB = 13

                    Drawable drawable = getContext().getResources().getDrawable(R.drawable.player_black);
                    int heightPixels = getResources().getDisplayMetrics().heightPixels;
                    int unit = (int) ((heightPixels / gameBoard.length) * 0.8);
                    int lengthPos = col * unit;
                    int heightPos = row * unit;
                    drawable.setBounds(heightPos, lengthPos, (heightPos + unit), (lengthPos + unit));
                    drawable.draw(canvas);
                }
                // ***** Experimental Code END *****
            }
        }
    }

    public Block[][] getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(Block[][] gameField) {
        this.gameBoard = gameField;
    }


}
