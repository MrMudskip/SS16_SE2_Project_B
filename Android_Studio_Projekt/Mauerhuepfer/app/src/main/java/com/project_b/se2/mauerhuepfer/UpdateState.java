package com.project_b.se2.mauerhuepfer;

import android.util.Log;

import java.io.Serializable;

/**
 * Includes all values needed to detect any status change of the game.
 */
public class UpdateState implements Serializable {
    private int usage = -1;
    private String msg;
    private String player;
    private int playerID = -1;
    private int position = -1;
    private int w1 = -1;
    private int w2 = -1;

    @Override
    public String toString() {
        return msg + ";" + player + ";" + position + ";" + w1 + ";" + w2;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getW1() {
        return w1;
    }

    public void setW1(int w1) {
        if (0 < w1 && w1 < 7) {
            this.w1 = w1;
        } else {
            Log.e("ERROR", "Set w2 Failure: " + w1);
        }
    }

    public int getW2() {
        return w2;
    }

    public void setW2(int w2) {
        if (0 < w2 && w2 < 7) {
            this.w2 = w2;
        } else {
            Log.e("ERROR", "Set w2 Failure: " + w2);
        }
    }

    public int getUsage() {
        return usage;
    }

    public void setUsage(@IReceiveMessage.UpdateUsageCode int usage) {
        this.usage = usage;
    }
}
