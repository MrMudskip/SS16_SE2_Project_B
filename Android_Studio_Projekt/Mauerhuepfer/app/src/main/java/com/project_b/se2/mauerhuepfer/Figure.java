package com.project_b.se2.mauerhuepfer;

import android.graphics.drawable.Drawable;

/**
 * Created by Anita on 04.05.2016.
 */
public class Figure {

    private Drawable image;
    private Player owner;
    private int colPos;
    private int rowPos;
    private int baseColPos;
    private int baseRowPos;
    private int goalColPos;
    private int goalRowPos;
    private int unit;

    public Figure(Player owner) {
        this.owner = owner;
        this.colPos = -1;
        this.rowPos = -1;
        this.baseColPos = -1;
        this.baseRowPos = -1;
        this.goalColPos = -1;
        this.goalRowPos = -1;
        this.unit = Game.unit;
    }

    public boolean setPos(int col, int row) {
        // Set new position logically.
        colPos = col;
        rowPos = row;

        //Adjust image position accordingly.
        if (image != null) {
            int lengthPos = col * unit;
            int heightPos = row * unit;
            image.setBounds(heightPos, lengthPos, (heightPos + unit), (lengthPos + unit));
            return true;
        } else {
            return false;
        }
    }

    public void walkUp() {
        setPos(colPos - 1, rowPos);
    }

    public void walkRight() {
        setPos(colPos, rowPos + 1);
    }

    public void walkDown() {
        setPos(colPos + 1, rowPos);
    }

    public void walkLeft() {
        setPos(colPos, rowPos - 1);
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public Player getOwner() {
        return owner;
    }

    public int getColPos() {
        return colPos;
    }

    public int getRowPos() {
        return rowPos;
    }

    public int getBaseColPos() {
        return baseColPos;
    }

    public void setBaseColPos(int baseColPos) {
        this.baseColPos = baseColPos;
    }

    public int getBaseRowPos() {
        return baseRowPos;
    }

    public void setBaseRowPos(int baseRowPos) {
        this.baseRowPos = baseRowPos;
    }

    public int getGoalColPos() {
        return goalColPos;
    }

    public void setGoalColPos(int goalColPos) {
        this.goalColPos = goalColPos;
    }

    public int getGoalRowPos() {
        return goalRowPos;
    }

    public void setGoalRowPos(int goalRowPos) {
        this.goalRowPos = goalRowPos;
    }
}




