package com.project_b.se2.mauerhuepfer;

import com.project_b.se2.mauerhuepfer.interfaces.IReceiveMessage;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by rohrbe on 17.06.16.
 */
public class ObjectSerializerUnitTest {

    @Test
    public void testSerialize() {
        UpdateState updateState = new UpdateState();
        updateState.setUsage(IReceiveMessage.USAGE_NEXTPLAYER);
        updateState.setPlayerID(1);

        byte[] test = ObjectSerializer.serialize(updateState);
        UpdateState update = (UpdateState) ObjectSerializer.deSerialize(test);

        Assert.assertEquals(update.getUsage(), updateState.getUsage());
        Assert.assertEquals(update.getPlayerID(), updateState.getPlayerID());
    }

    @Test
    public void testSerialize2() {
        UpdateState updateState = new UpdateState();
        updateState.setUsage(IReceiveMessage.USAGE_NEXTPLAYER);

        byte[] test = ObjectSerializer.serialize(updateState);
        UpdateState update = (UpdateState) ObjectSerializer.deSerialize(test);

        Assert.assertNotEquals(update.getUsage(), IReceiveMessage.USAGE_DICE_ROLLED);
    }

    @Test
    public void testSerialize3() {
        UpdateState updateState = new UpdateState();
        Block[][] game = new Block[2][2];
        game[0][0] = new Block(1);
        game[0][1] = new Block(2);
        game[1][0] = new Block(3);
        game[1][1] = new Block(4);

        game[0][0].setColPos(1);
        game[0][1].setNextBlock(5);
        game[0][1].setPreviousBlock(5);
        game[1][0].setRowPos(4);
        game[1][1].setType(5);
        game[1][1].setWallNumber(5);

        updateState.setGameBoard(game);
        byte[] test = ObjectSerializer.serialize(updateState);
        UpdateState update = (UpdateState) ObjectSerializer.deSerialize(test);

        Assert.assertEquals(update.getGameBoard()[0][0].getColPos(), updateState.getGameBoard()[0][0].getColPos());
        Assert.assertEquals(update.getGameBoard()[0][1].getNextBlock(), updateState.getGameBoard()[0][1].getNextBlock());
        Assert.assertEquals(update.getGameBoard()[0][1].getPreviousBlock(), updateState.getGameBoard()[0][1].getPreviousBlock());
        Assert.assertEquals(update.getGameBoard()[1][0].getRowPos(), updateState.getGameBoard()[1][0].getRowPos());
        Assert.assertEquals(update.getGameBoard()[1][1].getType(), updateState.getGameBoard()[1][1].getType());
        Assert.assertEquals(update.getGameBoard()[1][1].getWallNumber(), updateState.getGameBoard()[1][1].getWallNumber());
    }

    @Test
    public void testSerialize4() {
        UpdateState updateState = new UpdateState();
        updateState.setW1(1);
        updateState.setW2(4);

        byte[] test = ObjectSerializer.serialize(updateState);
        UpdateState update = (UpdateState) ObjectSerializer.deSerialize(test);

        Assert.assertEquals(update.getW1(), updateState.getW1());
        Assert.assertEquals(update.getW2(), updateState.getW2());
    }

    @Test
    public void testSerialize5() {
        UpdateState updateState = new UpdateState();
        updateState.setPlayerName("Hubert");
        updateState.setMsg("Hallo");

        byte[] test = ObjectSerializer.serialize(updateState);
        UpdateState update = (UpdateState) ObjectSerializer.deSerialize(test);

        Assert.assertEquals(update.getPlayerName(), updateState.getPlayerName());
        Assert.assertEquals(update.getMsg(), updateState.getMsg());
    }

    @Test
    public void testSerialize6() {
        UpdateState updateState = new UpdateState();
        updateState.setIntValue(55);

        byte[] test = ObjectSerializer.serialize(updateState);
        UpdateState update = (UpdateState) ObjectSerializer.deSerialize(test);

        Assert.assertEquals(update.getIntValue(), updateState.getIntValue());
    }

}
