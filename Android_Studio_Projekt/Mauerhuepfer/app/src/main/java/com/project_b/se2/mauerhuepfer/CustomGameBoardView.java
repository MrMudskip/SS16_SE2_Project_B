package com.project_b.se2.mauerhuepfer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Julian Janda on 27.05.2016.
 */
public class CustomGameBoardView extends View {

    private Block[][] gameBoard;


    public CustomGameBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw(Canvas canvas) {

        //Draw gameBoard
        for (int col = 0; col < gameBoard.length; col++) {
            for (int row = 0; row < gameBoard[col].length; row++) {
                Block currentBlock = gameBoard[col][row];
                // Draw block
                currentBlock.getImage().draw(canvas);
            }
        }
    }

    public Block[][] getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(Block[][] gameBoard) {
        this.gameBoard = gameBoard;
    }


}
