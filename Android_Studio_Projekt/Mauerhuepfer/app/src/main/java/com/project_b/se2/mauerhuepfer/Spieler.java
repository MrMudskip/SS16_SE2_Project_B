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

    public boolean gewonnen(){
        anzahl=kegel;
        if (anzahl==4){
            System.out.println("Du hast gewonnen!");
            return true;
        } return false;
    }



    private Vector<Figur> figuren = null;
    private int einstiegspunkt = -1;
    private int spielerNummer = -1;
    //private String name = null;


    /**
     * Konstruktur, der den Einstiegspunkt setzt und die Figuren initialisiert
     * @param spielerNummer die Nummer des Spielers (zwischen 0 und 3)
     */
    public Spieler(String name, int spielerNummer) {
        this.einstiegspunkt = spielerNummer * 10;
        this.name = name;
        this.spielerNummer = spielerNummer;
        this.figuren = new Vector<Figur>(4);

        for (int i = 0; i < 4; i++)
            this.figuren.add(new Figur(this));
    }

    /**
     * fuehrt einen Zug durch
     * @param anzahlSchritte die gewuerfelte Zahl (= Anzahl der zuruecklegbaren Schritte)
     * @return ob eine Figur bewegt werden konnte
     */
    public boolean ziehe(int figurnummerposition, int anzahlSchritte) {
        Figur figur = null;
        for (int i=0;i<figuren.size();i++)
        {
            figur = figuren.get(i);
            if (figur.getPosition()==figurnummerposition)
                break;
        }
        if (figur==null || figurnummerposition!=figur.getPosition()) {
            return false;
        }

        int neuePosition = Spielfeld.getInstance().bewegeFigur(this, figur.getPosition(), anzahlSchritte);
        if (neuePosition > -1) {
            figur.bewege(neuePosition);
            return true;
        } else {
            return false;
        }
    }

    /**
     * gibt den Einstiegspunkt dieses Spielers zurueck
     * @return der Einstiegspunkt
     */
    public int getEinstiegspunkt() {
        return this.einstiegspunkt;
    }

    /**
     * gibt die Spielernummer zurueck
     * @return die Spielernummer
     */
    public int getSpielernummer() {
        return this.spielerNummer;
    }

    /**
     * gibt an, ob ein Spieler gewonnen hat (= alle Figuren im Ziel)
     * @return true, wenn er gewonnen hat, sonst false
     */
    public boolean hatGewonnen() {
        for (Figur itFigur : figuren) {
            if (!itFigur.istInZiel())
                return false;
        }

        return true;
    }

    /**
     * liefert den Namen des Spielers
     * @return der Name des Spielers
     */
    public String getName() {
        return this.name;
    }

    /**
     * gibt an, ob ein Spieler schon/noch Figuren auf dem Brett hat
     * @return wahr, wenn dieser Spieler Figuren auf dem Brett hat
     */
    public boolean istDraussen() {
        for (Figur itFigur : figuren) {
            if ((itFigur.getPosition() > -1) && (itFigur.getPosition() < 58))
                return true;
        }

        return false;
    }

    /**
     * holt die erste Figur, die noch außerhalb des Spielfelds steht, auf das Spielfeld
     */
    public boolean kommRaus() {
        for (Figur itFig : figuren)
            if (itFig.getPosition() == -1) {
                if(Spielfeld.getInstance().kommRaus(this, itFig))
                {
                    itFig.aufStart();
                    return true;
                }
                else
                    break;
            }
        return false;
    }

    /**
     * getter fuer die Figuren
     * @return die Figuren des Spielers
     */
    public Vector<Figur> getFiguren() {
        return figuren;
    }



    /**
     * gibt an, ob dieser Spieler Figuren hat, die er setzen kann
     * @param anzahlSchritte die Anzahl der zurueckzulegenden Schritte (= gewuerfelte Zahl)
     * @return true, wenn dieser Spieler Figuren hat, die er mit den gegebenen Schritten setzen kann
     */
    public boolean hatZugMoeglichkeit(byte anzahlSchritte) {
        for (Figur itFigur : this.figuren) {
            if (((anzahlSchritte == 6) && (Spielfeld.getInstance().kannRausKommen(itFigur.getBesitzer(), itFigur))) || ((itFigur.getPosition() > -1) && (Spielfeld.getInstance().istZugGueltig(this, itFigur.getPosition(), anzahlSchritte))))
                return true;
        }

        return false;
    }


}
