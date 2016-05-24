package com.project_b.se2.mauerhuepfer;

import java.util.Vector;

/**
 * Created by Anita on 03.05.2016.
 */
public class Gamefield {
    private int dice1;
    private int dice2;
    private Spieler player;
    private int position;
    private int[] block = {};        //Height, Length
    private Block house;
    private Block field;
    private Block fin;


    public Gamefield(Spieler player) {
        this.player=player;
        this.position=-1;
    }

    public void moveFigure(){
        player.figureOnField();
        for (int i=0; i<block.length; i++){
            int dice = dice1+dice2;
            position=position+dice;
            if (){                                  //position free? is the figure one of mine?
                player.                             //figure is not mine
            } else {
                if (player.getFigureID()<4) {                            //do i have another figure on position -1?
                    player.figureOnField();
                    player.moveFigure(position);
                } else {
                    player.moveFigure(position);
                }
            }

        }
    }

    public void finish(){

    }

    public int getDice1() {
        return dice1;
    }

    public int getDice2() {
        return dice2;
    }

    /*public int getOwnerID() {
        return ownerID;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }*/
}
