package com.project_b.se2.mauerhuepfer;

/**
 * Created by rohrbe on 29.05.16.
 */
public interface INetworkManager {

    void sendMessage(UpdateState status);

    public void addMessageReceiverListener(IRecieveMessage listener);

    public void removeMessageReceiverListener(IRecieveMessage listener);
}
