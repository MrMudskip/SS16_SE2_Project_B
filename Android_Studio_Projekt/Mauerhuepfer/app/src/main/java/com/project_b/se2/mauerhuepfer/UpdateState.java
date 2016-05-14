package com.project_b.se2.mauerhuepfer;

import java.io.Serializable;

/**
 * Includes all values needed to detect any status change of the game
 */
public class UpdateState implements Serializable {
    public String msg;
    public int player;
    public int position;

    @Override
    public String toString() {
        //return msg + "\nPlayer " + player + " now at: " + position;
        return msg + ";" + player + ";" + position;
    }
}
