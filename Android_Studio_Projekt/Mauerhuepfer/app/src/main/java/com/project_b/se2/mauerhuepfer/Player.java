package com.project_b.se2.mauerhuepfer;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by Anita on 02.05.2016.
 */
public class Player {

    Context context;
    private int colour;
    private Figure[] figures = {new Figure(this), new Figure(this), new Figure(this), new Figure(this)};

    public Player(Context context, int colour) {
        this.context = context;
        this.colour = colour;
        initializeFigures();
    }

    public void initializeFigures() {
        Drawable image;
        switch (colour) {
            case Game.RED: image = context.getResources().getDrawable(R.drawable.player_red); break;
            case Game.GREEN: image = context.getResources().getDrawable(R.drawable.player_green); break;
            case Game.YELLOW: image = context.getResources().getDrawable(R.drawable.player_yellow); break;
            case Game.BLACK: image = context.getResources().getDrawable(R.drawable.player_black); break;
            default: image = context.getResources().getDrawable(R.drawable.empty);
        }

        for (int i = 0; i < figures.length; i++) {
            Drawable clone = image.getConstantState().newDrawable().mutate(); //Deep copy to avoid figures sharing the same image.
            figures[i].setImage(clone);
        }
    }

    public int getColour() {
        return colour;
    }

    public Figure[] getFigures() {
        return figures;
    }
}