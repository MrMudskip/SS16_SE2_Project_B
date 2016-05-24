package com.project_b.se2.mauerhuepfer;

import java.util.Vector;

/**
 * Created by Anita on 03.05.2016.
 */
public class Gamefield {
    private int cube1;
    private int cube2;  // I think these should be called dice ("Spielfürfel") ~Julian
    private Spieler spieler;
    private int position;
    private int[] block = {};        //Height, Length


    public Gamefield(Spieler player) {
        this.spieler=player;
        this.position=-1;
    }

    public void moveFigure(){
        for (int i=0; i<block.length; i++){
            int cube = cube1+cube2;
            position=position+cube;
            if (){                                  //position free? is the figure one of my?
                //spieler.move(position)            //figure is not mine
            } if ()   {
                 if () {                            //do i have another figure on position -1?

                 } else
            } else {
                //spieler.move(position)
            }

        }
    }

    public void finish(){

    }

    public int getCube1() {
        return cube1;
    }

    public int getCube2() {
        return cube2;
    }

    /*public int getOwnerID() {
        return ownerID;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }*/
}
