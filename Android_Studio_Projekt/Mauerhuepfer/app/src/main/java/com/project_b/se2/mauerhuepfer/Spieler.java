package com.project_b.se2.mauerhuepfer;

import java.util.Vector;

/**
 * Created by Anita on 02.05.2016.
 */
public class Spieler {
    private String name;                    //Name des Spielers
    private int position;
    private int kegel;                      //Anzahl der Kegel im Spiel
    private int anzahl;                     //Anzahl der Kegel im Ziel
    private int nr;                         //Nummer des Spielers
    private Vector<Kegel> figur = null;


    public Spieler(String name, int nr) {
        this.name = name;
        this.position = -1;
        this.kegel = 0;
        this.nr = nr;
        this.figur=new Vector<Kegel>(4);

        for (int i=0; i<4; i++){
            this.figur.add(new Kegel(this));
        }
    }

    public void spiel(){

    }

    public void kommtRaus() {
        kegel = kegel + 1;
        position = 0;
    }



    /*public boolean gewonnen(){
        anzahl=kegel;
        if (anzahl==4){
            System.out.println("Du hast gewonnen!");
            return true;
        } return false;
    }*/


}