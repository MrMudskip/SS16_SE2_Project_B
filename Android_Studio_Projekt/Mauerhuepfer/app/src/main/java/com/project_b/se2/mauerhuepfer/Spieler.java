package com.project_b.se2.mauerhuepfer;

import java.util.Vector;

/**
 * Created by Anita on 02.05.2016.
 */
public class Spieler {
    private String name;                //Name des Spielers
    private int position;
    private int kegel;                  //Anzahl der Kegel im Spiel

    private int anzahl;                 //Anzahl der Kegel im Ziel


    public Spieler(String name){
        this.name=name;
        this.position=-1;
        this.kegel=0;
    }

    public void entferne(){
        this.position=-1;
    }

    public boolean gewonnen(){
        anzahl=kegel;
        if (anzahl==4){
            System.out.println("Du hast gewonnen!");
            return true;
        } return false;
    }




}
