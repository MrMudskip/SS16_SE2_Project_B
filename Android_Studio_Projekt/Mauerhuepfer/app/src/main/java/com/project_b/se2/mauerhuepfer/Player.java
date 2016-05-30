package com.project_b.se2.mauerhuepfer;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by Anita on 02.05.2016.
 */
public class Player {

    //Player colours: //TODO: Not ideal that this appears here AND in the game class.
    static final int RED = 0;
    static final int GREEN = 1;
    static final int YELLOW = 2;
    static final int BLACK = 3;

   //Other variables
    Context context;
    private int colour;
    private Figure[] figures = {new Figure(), new Figure(), new Figure(), new Figure()};


    public Player(Context context, int colour) {
        this.context = context;
        this.colour = colour;
        initializeFigures();
    }

    public void initializeFigures(){ // TODO Decide if image position should also be managed here.
        Drawable image;
        switch (colour) {
            case RED: image = context.getResources().getDrawable(R.drawable.player_red); break;
            case GREEN: image = context.getResources().getDrawable(R.drawable.player_green); break;
            case YELLOW: image = context.getResources().getDrawable(R.drawable.player_yellow); break;
            case BLACK: image = context.getResources().getDrawable(R.drawable.player_black); break;
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
}