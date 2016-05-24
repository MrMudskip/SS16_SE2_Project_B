package com.project_b.se2.mauerhuepfer;

import android.graphics.Bitmap;

/**
 * Created by Julian Janda on 24.05.2016.
 */
public class Block {

    private int type;
    private Bitmap image; // TODO: decide if bitmap is the right choice here


    public Block(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
