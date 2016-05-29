package com.project_b.se2.mauerhuepfer;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by Julian Janda on 27.05.2016.
 */
public class CustomGameBoardView extends View {

    private Block[][] gameField;


    public CustomGameBoardView(Context context, Block[][] gameField) {
        super(context);
        this.gameField = gameField;
    }

    protected void onDraw(Canvas canvas) {
        for (int col = 0; col < gameField.length; col++) {
            for (int row = 0; row < gameField[col].length; row++) {
                gameField[col][row].getImage().draw(canvas);
            }
        }
    }

    public Block[][] getGameField() {
        return gameField;
    }

    public void setGameField(Block[][] gameField) {
        this.gameField = gameField;
    }


}
