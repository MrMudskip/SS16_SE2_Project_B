package com.project_b.se2.mauerhuepfer;

/**
 * Created by Anita on 02.05.2016.
 */
public class Mauer {
    int position1;
    int position2;
    int[] mauer1_3 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    int[] mauer2_4 = {12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};

    public int wuerfeln(){
        return (int)(Math.random() * 6 + 1);
    }

    public void mauer1(){


        if (mauer1_3.length==wuerfeln()){
            position1=position2;
        }

    }

    public void mauer2(){
        if (mauer2_4.length==wuerfeln()){
            position1=position2;
        }
    }

    public void mauer3(){
        if (mauer1_3.length==wuerfeln()){
            position1=position2;
        }
    }

    public void mauer4(){
        if (mauer2_4.length==wuerfeln()){
            position1=position2;
        }
    }




}
