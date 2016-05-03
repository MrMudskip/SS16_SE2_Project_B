package com.project_b.se2.mauerhuepfer;

/**
 * Created by Anita on 03.05.2016.
 */
public class Figur {
    private Spieler besitzer;
    private int position;

    public Figur(Spieler besitzer) {
        this.besitzer = besitzer;
        this.position = -1;
    }

    /**
     * bewegt die Figur um eine bestimmte Anzahl an Schritten
     * param anzahlSchritte die Anzahl der Schritte, um die die Figur bewegt werden soll
     */
    public void bewege(int neuePosition) {
        this.position = neuePosition;
    }

    /**
     * gibt die aktuelle Position der Figur auf dem Spielbrett zurueck
     * @return die aktuelle Position
     */
    public int getPosition() {
        return position;
    }

    /**
     * gibt den Besitzer der Figur zurueck
     * @return
     */
    public Spieler getBesitzer() {
        return besitzer;
    }

    /**
     * entfernt diese Figur aus dem Spiel
     */
    public void entferne() {
        this.position = -1;
    }


}
