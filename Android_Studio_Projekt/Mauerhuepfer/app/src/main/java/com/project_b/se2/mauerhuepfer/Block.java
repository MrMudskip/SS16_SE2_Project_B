package com.project_b.se2.mauerhuepfer;

import android.graphics.drawable.Drawable;

/**
 * Created by Julian Janda on 24.05.2016.
 */
public class Block {

    private int type;
    private Drawable image;



    public Block(int type) {
        this.type = type;
    }



    // Getters and setters below this line ---------------------------

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
}