package com.project_b.se2.mauerhuepfer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by Julian Janda on 27.05.2016.
 */
public class CustomGameBoardView extends View {

    private Drawable mDrawable;
    private Drawable mD2;
    private Block[][] gameField;


    public CustomGameBoardView(Context context, Block[][] gameField) {
        super(context);

        this.gameField = gameField;

/*
        int x = 0;
        int y = 0;
        int width = 50;
        int height = 50;

        Resources res = context.getResources();
        mDrawable = res.getDrawable(R.drawable.player_black);
        mDrawable.setBounds(x, y, x + width, y + height);

        x = 50;
        y = 0;
        width = 50;
        height = 50;

        mD2 = res.getDrawable(R.drawable.circle_black);
        mD2.setBounds(x, y, x + width, y + height);*/
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
