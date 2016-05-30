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

        //Draw gameBoard //TODO It should be enough to draw this once, not every time.
        for (int col = 0; col < gameBoard.length; col++) {
            for (int row = 0; row < gameBoard[col].length; row++) {
                Block currentBlock = gameBoard[col][row];
                // Draw block
                currentBlock.getImage().draw(canvas);

                //Draw Players
                if (currentBlock.getOccupyingFigure() != null){
                    currentBlock.getOccupyingFigure().getImage().draw(canvas);
                }

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
