package com.project_b.se2.mauerhuepfer;

import java.util.Vector;

/**
 * Created by Anita on 03.05.2016.
 */
public class Spiel {
    private int[] spielfeld = {58};
    private int wuerfel;
    private int position;
    private int neueposition;
    private Spieler spieler = null;

    public Spiel (Spieler spieler){
        this.spieler=spieler;
        this.position=-1;
    }

    public void spiel(){
        position=spielfeld[0];
        while (position != spielfeld[58]){
            //gewürfelt
            neueposition=position+wuerfel;

        }

    }

    public void mauer(){
        Mauer m = new Mauer(position, wuerfel, spielfeld);

        while (spielfeld.length<=7){
            m.mauer1();
        }
        while (spielfeld.length>=10 || spielfeld.length<=17){
            m.mauer2();
        }
        while (spielfeld.length>=20 || spielfeld.length<=27){
            m.mauer3();
        }
        while (spielfeld.length>=30 || spielfeld.length<=37){
            m.mauer4();
        }
        while (spielfeld.length>=41 || spielfeld.length<=47){
            m.mauer5();
        }
        if (spielfeld.length==40){
            position=spielfeld[58];
        }
    }

}
