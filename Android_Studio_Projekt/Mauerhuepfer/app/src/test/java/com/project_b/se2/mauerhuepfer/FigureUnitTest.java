package com.project_b.se2.mauerhuepfer;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

/**
 * Created by rohrbe on 17.06.16.
 */
public class FigureUnitTest {
    @Mock
    Player player;

    Figure fig = new Figure(player);

    @Test
    public void test1() {
        int val = 1;
        fig.setBaseColPos(val);
        Assert.assertEquals(val, fig.getBaseColPos());
    }

    @Test
    public void test2() {
        int val = 1;
        fig.setBaseRowPos(val);
        Assert.assertEquals(val, fig.getBaseRowPos());
    }

    @Test
    public void test3() {
        int val = 1;
        fig.setGoalColPos(val);
        Assert.assertEquals(val, fig.getGoalColPos());
    }

    @Test
    public void test4() {
        int val = 1;
        fig.setGoalRowPos(val);
        Assert.assertEquals(val, fig.getGoalRowPos());
    }

    @Test
    public void test5() {
        int row = 1;
        int col = 2;
        Assert.assertFalse(fig.setPos(row, col));
    }

    @Test
    public void test6() {
        int row = 1;
        int col = 2;
        fig.setPos(col, row);
        fig.walkRight();
        Assert.assertEquals(row + 1, fig.getRowPos());
    }

    @Test
    public void test7() {
        int row = 1;
        int col = 2;
        fig.setPos(col, row);
        fig.walkLeft();
        Assert.assertEquals(row - 1, fig.getRowPos());
    }

    @Test
    public void test8() {
        int row = 1;
        int col = 2;
        fig.setPos(col, row);
        fig.walkUp();
        Assert.assertEquals(col - 1, fig.getColPos());
    }

    @Test
    public void test9() {
        int row = 1;
        int col = 2;
        fig.setPos(col, row);
        fig.walkDown();
        Assert.assertEquals(col + 1, fig.getColPos());
    }

}
