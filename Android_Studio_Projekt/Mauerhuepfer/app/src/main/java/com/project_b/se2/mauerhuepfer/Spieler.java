package com.project_b.se2.mauerhuepfer;

import java.util.Vector;

/**
 * Created by Anita on 02.05.2016.
 */
public class Spieler {
    private String name=null;                            //Name des Spielers
    private int onwnerID;                                //Nummer des Spielers
    private Figure figure = new Figure(name,onwnerID);

    public Spieler(String name, int ownerID) {
        this.name=name;
        this.onwnerID=ownerID;
    }

    public void startPosition(int position){
        figure.setStart(position);
    }

    public void moveFigure(int position){
        figure.move(position);
    }

    public int getFigureID(){
        return figure.getFigureID();
    }

    public void returnToBase(){
        figure.returnToBase();
    }

    public void figureOnField(){
        int figureID=figure.getFigureID();
        while (figureID < 4){
            figureID++;
            startPosition(0);
        }
    }

    public String getName() {
        return name;
    }

    public int getOnwnerID() {
        return onwnerID;
    }
}