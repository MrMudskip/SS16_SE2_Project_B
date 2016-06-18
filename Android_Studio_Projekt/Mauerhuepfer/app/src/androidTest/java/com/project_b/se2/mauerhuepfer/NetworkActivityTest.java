package com.project_b.se2.mauerhuepfer;

import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;

import com.project_b.se2.mauerhuepfer.interfaces.INetworkManager;

import org.junit.Assert;

/**
 * Created by rohrbe on 18.06.16.
 */
public class NetworkActivityTest extends ActivityInstrumentationTestCase2<NetworkActivity> {

    public NetworkActivityTest() {
        super(NetworkActivity.class);
    }

    public void testActivityExists() {
        NetworkActivity activity = getActivity();
        assertNotNull(activity);
    }

    public void testReceive() {
        NetworkActivity activity = getActivity();
        activity.receiveMessage(new UpdateState());
    }

    public void testPlayerID() {
        NetworkManager networkManager = new NetworkManager(getActivity());
        int pid = 3;
        networkManager.setPlayerID(pid);
        int getPid = networkManager.getPlayerID();

        Assert.assertEquals(pid, getPid);
    }

    public void testWrongPlayerID() {
        NetworkManager networkManager = new NetworkManager(getActivity());
        int pid = 4;
        networkManager.setPlayerID(pid);
        int getPid = networkManager.getPlayerID();

        Assert.assertEquals(-1, getPid);
    }

    public void testNumberofPlayer() {
        NetworkManager networkManager = new NetworkManager(getActivity());
        Assert.assertEquals(1, networkManager.getNumberOfPlayers());
    }
}
