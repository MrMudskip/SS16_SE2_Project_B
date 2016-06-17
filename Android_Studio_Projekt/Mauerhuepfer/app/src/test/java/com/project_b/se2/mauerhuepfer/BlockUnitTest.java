package com.project_b.se2.mauerhuepfer;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by rohrbe on 17.06.16.
 */
public class BlockUnitTest {
    Block block = new Block(1);

    @Test
    public void testType1() {
        int type = 1;
        block.setType(type);
        Assert.assertEquals(block.getType(), type);
    }

    @Test
    public void testType2() {
        int type = -1;
        block.setType(type);
        Assert.assertEquals(block.getType(), type);
    }

    @Test
    public void testType3() {
        int type = 1024;
        block.setType(type);
        Assert.assertEquals(block.getType(), type);
    }

    @Test
    public void testType4() {
        int type = 10;
        block.setType(type);
        Assert.assertEquals(block.getType(), type);
    }

    @Test
    public void testType5() {
        int type = 18;
        block.setType(type);
        Assert.assertEquals(block.getType(), type);
    }

    @Test
    public void testRowPos6() {
        int row = -1;
        block.setRowPos(row);
        Assert.assertEquals(block.getRowPos(), row);
    }

    @Test
    public void testRowPos7() {
        int row = 1;
        block.setRowPos(row);
        Assert.assertEquals(block.getRowPos(), row);
    }

    @Test
    public void testRowPos8() {
        int row = 9;
        block.setRowPos(row);
        Assert.assertEquals(block.getRowPos(), row);
    }

    @Test
    public void testRowPos9() {
        int row = 10;
        block.setRowPos(row);
        Assert.assertEquals(block.getRowPos(), row);
    }

    @Test
    public void testColPos() {
        int col = 5;
        block.setColPos(col);
        Assert.assertEquals(block.getColPos(), col);
    }

    @Test
    public void testWallNumber() {
        int wall = 5;
        block.setWallNumber(wall);
        Assert.assertEquals(block.getWallNumber(), wall);
    }

    @Test
    public void testNextBlock() {
        int next = 5;
        block.setNextBlock(next);
        Assert.assertEquals(block.getNextBlock(), next);
    }

    @Test
    public void testPrevBlock() {
        int prev = 5;
        block.setPreviousBlock(prev);
        Assert.assertEquals(block.getPreviousBlock(), prev);
    }


}
