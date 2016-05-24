package com.project_b.se2.mauerhuepfer;

/**
 * Created by Anita on 02.05.2016.
 */
public class Mauer {
    int p;     //position alt
    int cube1;
    int cube2;
    int wuerfel = cube1 + cube2;
    int[] mauer1 = {1, 5, 6, 5, 4, 3, 2, 1};
    int[] mauer2 = {1, 2, 3, 4, 5, 6, 5, 4};
    int[] mauer3 = {1, 5, 6, 5, 4, 3, 2, 1};
    int[] mauer4 = {1, 2, 3, 4, 5, 6, 5, 4};
    int[] mauer5 = {5, 6, 5, 4, 3, 2, 1};

    private int[] spielfeld = {58};

    public Mauer (int p, int cube1, int cube2){
        this.p=p;
        this.cube1=cube1;
        this.cube2=cube2;
    }

    public void mauer1(){

        if (mauer1.length==wuerfel || mauer1.length==cube1 || mauer1.length==cube2){
            if (p==0){
                p=spielfeld[18];
            } else if (p==1){
                p=spielfeld[17];
            } else if (p==2){
                p=spielfeld[16];
            } else if (p==3){
                p=spielfeld[15];
            } else if (p==4){
                p=spielfeld[14];
            } else if (p==5){
                p=spielfeld[13];
            } else if (p==6){
                p=spielfeld[12];
            } else if(p==7){
                p=spielfeld[11];
            }
        }
    }



    public void mauer2(){

        if (mauer2.length==wuerfel || mauer2.length==cube1 || mauer2.length==cube2){
            if (p==10){
                p=spielfeld[28];
            } else if (p==11){
                p=spielfeld[27];
            } else if (p==12){
                p=spielfeld[26];
            } else if (p==13){
                p=spielfeld[25];
            } else if (p==14){
                p=spielfeld[24];
            } else if (p==15){
                p=spielfeld[23];
            } else if (p==16){
                p=spielfeld[22];
            } else if (p==17){
                p=spielfeld[21];
            }
        }
    }

    public void mauer3(){

        if (mauer3.length==wuerfel || mauer3.length==cube1 || mauer3.length==cube2){
            if (p==20){
                p=spielfeld[38];
            } else if (p==21){
                p=spielfeld[37];
            } else if (p==22){
                p=spielfeld[36];
            } else if (p==23){
                p=spielfeld[35];
            } else if (p==24){
                p=spielfeld[34];
            } else if (p==25){
                p=spielfeld[33];
            } else if (p==26){
                p=spielfeld[32];
            } else if (p==27){
                p=spielfeld[31];
            }
        }
    }

    public void mauer4(){

        if (mauer4.length==wuerfel || mauer4.length==cube1 || mauer4.length==cube2) {
            if (p == 30) {
                p = spielfeld[48];
            } else if (p == 31) {
                p = spielfeld[47];
            } else if (p == 32) {
                p = spielfeld[46];
            } else if (p == 33) {
                p = spielfeld[45];
            } else if (p == 34) {
                p = spielfeld[44];
            } else if (p == 35) {
                p = spielfeld[43];
            } else if (p == 36) {
                p = spielfeld[42];
            } else if (p == 37) {
                p = spielfeld[41];
            }
            }

        }

    public void mauer5(){

        if (mauer5.length==wuerfel || mauer5.length==cube1 || mauer5.length==cube2){
            if (p==41){
                p=spielfeld[57];
            } else if (p==42){
                p=spielfeld[56];
            } else if (p==43){
                p=spielfeld[55];
            } else if (p==44){
                p=spielfeld[54];
            } else if (p==45){
                p=spielfeld[53];
            } else if (p==46){
                p=spielfeld[52];
            } else if (p==47){
                p=spielfeld[51];
            }
        }
    }



}
