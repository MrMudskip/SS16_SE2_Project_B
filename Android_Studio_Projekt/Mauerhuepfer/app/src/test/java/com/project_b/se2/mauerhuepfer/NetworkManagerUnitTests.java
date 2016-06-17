package com.project_b.se2.mauerhuepfer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by rohrbe on 17.06.16.
 */
@RunWith(MockitoJUnitRunner.class)
public class NetworkManagerUnitTests {
    @Mock
    NetworkManager networkManager;

    @Test
    public void testsendMessage() {
        networkManager.sendMessage(new UpdateState());
    }

    @Test
    public void testHostInfo() {
        networkManager.getHostInfo();
    }

    @Test
    public void testPlayerID() {
        int setId = 0;
        networkManager.setPlayerID(setId);
        int getId = networkManager.getPlayerID();
        Assert.assertEquals(setId, getId);
    }

    @Test
    public void testConnect() {
        networkManager.connect();
    }

    @Test
    public void testDisconnect() {
        networkManager.disconnect();
    }

    @Test
    public void testNumberofPlayers() {
        networkManager.getNumberOfPlayers();
    }

    @Test
    public void testOnEndpointFound() {
        networkManager.onEndpointFound("", "", "", "");
    }

    @Test
    public void testStartAdvertising() {
        networkManager.startAdvertising();
    }

    @Test
    public void testStartDiscovering() {
        networkManager.stopDiscovery();
    }

    @Test
    public void testOnConnectionRequest() {
        networkManager.onConnectionRequest("", "", "", new byte[1]);
    }

    @Test
    public void testOnMessageReceived() {
        networkManager.onMessageReceived("", new byte[1], true);
    }

    @Test
    public void testOnDisconnect() {
        networkManager.onDisconnected("");
    }
}
