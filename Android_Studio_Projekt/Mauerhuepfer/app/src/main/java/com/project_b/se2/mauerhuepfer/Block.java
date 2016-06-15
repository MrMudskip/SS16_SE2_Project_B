package com.project_b.se2.mauerhuepfer;

import android.graphics.drawable.Drawable;

/**
 * Created by Julian Janda on 24.05.2016.
 */
public class Block {

    private int type;
    private int colPos;
    private int rowPos;
    private int wallNumber;
    private int previousBlock;
    private int nextBlock;
    private Drawable image;

    public Block(int type) {
        this.type = type;
        this.colPos = -1;
        this.rowPos = -1;
        this.wallNumber = -1;
        this.nextBlock = -1;
        this.previousBlock = -1;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getColPos() {
        return colPos;
    }

    public void setColPos(int colPos) {
        this.colPos = colPos;
    }

    public int getRowPos() {
        return rowPos;
    }

    public void setRowPos(int rowPos) {
        this.rowPos = rowPos;
    }

    public int getWallNumber() {
        return wallNumber;
    }

    public void setWallNumber(int wallNumber) {
        this.wallNumber = wallNumber;
    }

    public int getPreviousBlock() {
        return previousBlock;
    }

    public void setPreviousBlock(int previousBlock) {
        this.previousBlock = previousBlock;
    }

    public int getNextBlock() {
        return nextBlock;
    }

    public void setNextBlock(int nextBlock) {
        this.nextBlock = nextBlock;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}