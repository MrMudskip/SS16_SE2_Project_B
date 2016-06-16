package com.project_b.se2.mauerhuepfer;

import android.util.Log;

import com.project_b.se2.mauerhuepfer.interfaces.IReceiveMessage;

import java.io.Serializable;

/**
 * Includes all values needed to detect any status change of the game.
 */
public class UpdateState implements Serializable {
    private int usage = -1;
    private String msg;
    private String playerName;
    private int playerID = -1;
    private int colPosition = -1;
    private int rowPosition = -1;
    private int w1 = -1;
    private int w2 = -1;
    private int intValue;
    private Figure figure;
    private Block[][] gameBoard;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public int getColPosition() {
        return colPosition;
    }

    public void setColPosition(int colPosition) {
        this.colPosition = colPosition;
    }

    public int getRowPosition() {
        return rowPosition;
    }

    public void setRowPosition(int rowPosition) {
        this.rowPosition = rowPosition;
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

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    public Block[][] getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(Block[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public int getUsage() {
        return usage;
    }

    public void setUsage(@IReceiveMessage.UpdateUsageCode int usage) {
        this.usage = usage;
    }
}
