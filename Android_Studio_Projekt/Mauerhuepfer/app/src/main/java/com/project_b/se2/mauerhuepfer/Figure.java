package com.project_b.se2.mauerhuepfer;

/**
 * Created by Anita on 04.05.2016.
 */
public class Figure{
    private String owner;
    private int position;
    private int ownerID;
    private int figureID;
    private int start;
    private int end;


    public Figure(int ownerID, int figureID){
        this.ownerID=ownerID;
        this.figureID=figureID;
        this.position=-1;
    }

    public void move(int nposition){
        this.position=nposition;
    }

    public int getPosition() {
        return position;
    }

    public String getOwner() {
        return owner;
    }

    public void returnToBase(){
        this.position=-1;
    }
    public int getOwnerID() {
        return ownerID;
    }

    public int getFigureID() {
        return figureID;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

}
