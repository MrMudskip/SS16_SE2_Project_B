package com.project_b.se2.mauerhuepfer;

import android.os.Bundle;

import com.project_b.se2.mauerhuepfer.interfaces.INetworkManager;
import com.project_b.se2.mauerhuepfer.interfaces.IReceiveMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by rohrbe on 17.06.16.
 */
@RunWith(MockitoJUnitRunner.class)
public class NetworkActivityUnitTest {

    @Mock
    NetworkActivity networkActivity;

    @Test
    public void testView1() {
        networkActivity.updateView(INetworkManager.STATE_IDLE);
    }

    @Test
    public void testView2() {
        networkActivity.updateView(INetworkManager.STATE_ADVERTISING);
    }

    @Test
    public void testView3() {
        networkActivity.updateView(INetworkManager.STATE_CONNECTED);
    }

    @Test
    public void testView4() {
        networkActivity.updateView(INetworkManager.STATE_DISCOVERING);
    }

    @Test
    public void testView5() {
        networkActivity.updateView(INetworkManager.STATE_NONETWORK);
    }

    @Test
    public void testView6() {
        networkActivity.updateView(INetworkManager.STATE_READY);
    }

    @Test
    public void testOnClick1() {
        networkActivity.onClick(networkActivity.findViewById(R.id.button_startGame));
    }

    @Test
    public void testOnClick2() {
        networkActivity.onClick(networkActivity.findViewById(R.id.button_advertise));
    }

    @Test
    public void testOnClick3() {
        networkActivity.onClick(networkActivity.findViewById(R.id.button_discover));
    }

    @Test
    public void testOnClick4() {
        networkActivity.onClick(networkActivity.findViewById(R.id.button_send));
    }

    @Test
    public void testOnClick5() {
        networkActivity.onClick(networkActivity.findViewById(R.id.buttonLaunch));
    }

    @Test
    public void testReceiveMessage1() {
        UpdateState update = new UpdateState();
        update.setUsage(IReceiveMessage.USAGE_MSG);
        networkActivity.receiveMessage(update);
    }

    @Test
    public void testReceiveMessage2() {
        UpdateState update = new UpdateState();
        update.setUsage(IReceiveMessage.USAGE_STARTGAME);
        networkActivity.receiveMessage(update);
    }

    @Test
    public void testReceiveMessage3() {
        UpdateState update = new UpdateState();
        update.setUsage(IReceiveMessage.USAGE_PLAYERID);
        networkActivity.receiveMessage(update);
    }

    @Test
    public void testReceiveMessage4() {
        UpdateState update = new UpdateState();
        update.setUsage(IReceiveMessage.USAGE_JOIN);
        networkActivity.receiveMessage(update);
    }

    @Test
    public void testReceiveMessage5() {
        UpdateState update = new UpdateState();
        update.setUsage(IReceiveMessage.USAGE_GAME_INITIALISED);
        networkActivity.receiveMessage(update);
    }

    @Test
    public void testOnCreate() {
        networkActivity.onCreate(new Bundle());
    }
}
