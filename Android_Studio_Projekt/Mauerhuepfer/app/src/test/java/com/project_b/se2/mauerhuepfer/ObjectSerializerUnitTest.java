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

        Assert.assertNotEquals(update.getUsage(), IReceiveMessage.USAGE_DICE);
    }

    @Test
    public void testSerialize3() {
        UpdateState updateState = new UpdateState();
        Block[][] game = new Block[2][2];

        for (int i = 0; i < game[0].length; i++) {
            for (int j = 0; j < game.length; j++) {
                game[i][j] = new Block(1);
            }
        }
        updateState.setGameBoard(game);

        byte[] test = ObjectSerializer.serialize(updateState);
        UpdateState update = (UpdateState) ObjectSerializer.deSerialize(test);

        Assert.assertEquals(update.getGameBoard()[0][0].getType(), updateState.getGameBoard()[0][0].getType());
        Assert.assertEquals(update.getGameBoard()[0][1].getType(), updateState.getGameBoard()[0][1].getType());
        Assert.assertEquals(update.getGameBoard()[1][0].getType(), updateState.getGameBoard()[1][0].getType());
        Assert.assertEquals(update.getGameBoard()[1][1].getType(), updateState.getGameBoard()[1][1].getType());
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

}
