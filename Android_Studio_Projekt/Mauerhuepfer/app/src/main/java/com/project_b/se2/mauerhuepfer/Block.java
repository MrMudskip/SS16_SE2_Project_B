package com.project_b.se2.mauerhuepfer;

import android.graphics.drawable.Drawable;

/**
 * Created by Julian Janda on 24.05.2016.
 */
public class Block {

    private int type;
    private int previousBlock;
    private int nextBlock;
    private Drawable image;

    public Block(int type) {
        this.type = type;
        this.nextBlock = -1;
        this.previousBlock = -1;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
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
}