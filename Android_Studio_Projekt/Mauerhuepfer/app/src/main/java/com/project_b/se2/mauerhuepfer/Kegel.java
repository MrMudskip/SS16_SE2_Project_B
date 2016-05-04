package com.project_b.se2.mauerhuepfer;

/**
 * Created by Anita on 04.05.2016.
 */
public class Kegel {
    private Spieler besitzer;
    private int position;


    public Kegel(Spieler besitzer){
        this.besitzer=besitzer;
        this.position=-1;
    }

    public void bewege(int nposition){
        this.position=nposition;
    }

    public int getPosition() {
        return position;
    }

    public Spieler getBesitzer() {
        return besitzer;
    }

    public void entferne(){
        this.position=-1;
    }


}
