package com.project_b.se2.mauerhuepfer;

import java.util.Vector;

/**
 * Created by Anita on 02.05.2016.
 */
public class Spieler {
    private String name;                            //Name des Spielers
    private int onwnerID;                           //Nummer des Spielers
    private Figure figure;


    public Spieler(String name, int onwnerID) {
        this.name = name;
        this.onwnerID = onwnerID;
    }

    public String getName() {
        return name;
    }

    public int getOnwnerID() {
        return onwnerID;
    }
}