package com.project_b.se2.mauerhuepfer;

import android.graphics.drawable.Drawable;

/**
 * Created by Anita on 04.05.2016.
 */
public class Figure {

    private Drawable image;
    private final int ownerID;
    private int colPos;
    private int rowPos;
    private int unit;

    public Figure(int ownerID) {
        this.ownerID = ownerID;
        this.colPos = 0;
        this.rowPos = 0;
        this.unit = Game.unit;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public int getColPos() {
        return colPos;
    }

    public int getRowPos() {
        return rowPos;
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
}




