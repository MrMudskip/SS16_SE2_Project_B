package com.project_b.se2.mauerhuepfer;

/**
 * Created by Anita on 02.05.2016.
 */
public class Mauer {
    int p;     //position alt
    int p2;     //position neu
    int wuerfel;
    int[] mauer1 = {1, 5, 6, 5, 4, 3, 2, 1};
    int[] mauer2 = {1, 2, 3, 4, 5, 6, 5, 4};
    int[] mauer3 = {1, 5, 6, 5, 4, 3, 2, 1};
    int[] mauer4 = {1, 2, 3, 4, 5, 6, 5, 4};
    int[] mauer5 = {5, 6, 5, 4, 3, 2, 1};

    public Mauer (int p, int wuerfel){
        this.p=p;
        this.wuerfel=wuerfel;
    }

    public void mauer1(){

        if (mauer1.length==wuerfel){
            switch (mauer1.length){
                case 1:
                    p=p+18; break;
                case 5:
            }
        }
    }

    public void mauer2(){

        if (mauer2.length==wuerfel){
            p=p2;
        }
    }

    public void mauer3(){

        if (mauer3.length==wuerfel){
            p=p2;
        }
    }

    public void mauer4(){

        if (mauer4.length==wuerfel{
            p=p2;
        }
    }

    public void mauer5(){

        if (mauer5.length==wuerfel){
            p=p2;
        }
    }



}
