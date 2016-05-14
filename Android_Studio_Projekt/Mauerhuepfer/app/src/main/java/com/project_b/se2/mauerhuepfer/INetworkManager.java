package com.project_b.se2.mauerhuepfer;

/**
 * Created by rohrbe on 14.05.16.
 */
public interface INetworkManager {

    void updateState(int state);

    void receiveMessage(UpdateState status);
}
