package com.project_b.se2.mauerhuepfer;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by Anita on 02.05.2016.
 */
public class Player {

    Context context;
    private int colour;
    private final int PID = (int)(Math.random() * 100); //TODO Use the PID from the net code.
    private Figure[] figures = {new Figure(PID), new Figure(PID), new Figure(PID), new Figure(PID)};

    public Player(Context context, int colour) {
        this.context = context;
        this.colour = colour;
        initializeFigures();
    }

    public void initializeFigures(){ // TODO Decide if image position should also be managed here.
        Drawable image;
        switch (colour) {
            case Game.RED: image = context.getResources().getDrawable(R.drawable.player_red); break;
            case Game.GREEN: image = context.getResources().getDrawable(R.drawable.player_green); break;
            case Game.YELLOW: image = context.getResources().getDrawable(R.drawable.player_yellow); break;
            case Game.BLACK: image = context.getResources().getDrawable(R.drawable.player_black); break;
            default:  image = context.getResources().getDrawable(R.drawable.empty);
        }
        for (int i = 0; i < figures.length; i++) {
            figures[i].setImage(image);
        }
    }

    public int getColour() {
        return colour;
    }

    public Figure[] getFigures() {
        return figures;
    }

    public int getPID() {
        return PID;
    }
}