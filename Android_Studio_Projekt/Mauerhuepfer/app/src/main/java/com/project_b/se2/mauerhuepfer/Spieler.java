package com.project_b.se2.mauerhuepfer;

import java.util.Vector;

/**
 * Created by Anita on 02.05.2016.
 */
public class Spieler {
    private String name;                //Name des Spielers
    private int position;
    private int[] spielfeld = {58};
    private int anzahl;                 //Anzahl der Kegel
    private int wuerfel;

    public Spieler(String name){
        this.name=name;
        this.position=-1;
    }

    public void bewege(int newposition){
        this.position=newposition;
    }

    public void entferne(){
        this.position=-1;
    }

    public void mauer(){
        Mauer m = new Mauer(position, wuerfel, spielfeld);
        
        while (spielfeld.length<=7){
            m.mauer1();
        }
        while (spielfeld.length>9 || spielfeld.length<=17){
            m.mauer2();
        }
        while (spielfeld.length>19 || spielfeld.length<=27){
            m.mauer3();
        }
        while (spielfeld.length>29 || spielfeld.length<=37){
            m.mauer4();
        }
        while (spielfeld.length>40 || spielfeld.length<=47){
            m.mauer5();
        }
        if (spielfeld.length==40){
            position=spielfeld[58];
        }
    }





}
