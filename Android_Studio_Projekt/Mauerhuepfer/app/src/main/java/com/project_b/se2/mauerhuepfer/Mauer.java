package com.project_b.se2.mauerhuepfer;

/**
 * Created by Anita on 02.05.2016.
 */
public class Mauer {
    int position1;
    int position2;
    int[] mauer1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

    public int wuerfeln(){
        return (int)(Math.random() * 6 + 1);
    }

    private void mauer1(){
        int gewürfelt;

        gewürfelt=wuerfeln();

        if (mauer1.length==gewürfelt){
            position1=position2;
        }

    }


}
