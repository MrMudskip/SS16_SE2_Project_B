package com.project_b.se2.mauerhuepfer;

import android.graphics.drawable.Drawable;

/**
 * Created by Julian Janda on 24.05.2016.
 */
public class Block {

    private int type;
    private Block previousBlock;
    private Block nextBlock;
    private boolean occupied;
    private Figure occupyingFigure;
    private Drawable image;



    public Block(int type) {
        this.type = type;
        this.occupied = false;
    }



    // Getters and setters below this line ---------------------------

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Figure getOccupyingFigure() {
        return occupyingFigure;
    }

    public void setOccupyingFigure(Figure occupyingFigure) {
        this.occupyingFigure = occupyingFigure;
        this.occupied = true;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public Block getNextBlock() {
        return nextBlock;
    }

    public void setNextBlock(Block nextBlock) {
        this.nextBlock = nextBlock;
    }

    public Block getPreviousBlock() {
        return previousBlock;
    }

    public void setPreviousBlock(Block previousBlock) {
        this.previousBlock = previousBlock;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}