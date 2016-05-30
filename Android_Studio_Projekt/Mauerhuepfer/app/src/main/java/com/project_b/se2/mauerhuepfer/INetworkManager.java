package com.project_b.se2.mauerhuepfer;

/**
 * Created by rohrbe on 29.05.16.
 */
public interface INetworkManager {

    /**
     * Send a Message to all Connected Devices
     *
     * @param status Update
     */
    void sendMessage(UpdateState status);

    /**
     * add a Class (listener). Necessary to receive Messages.
     *
     * @param listener Class that implements IReceiveMessage
     */
    void addMessageReceiverListener(IReceiveMessage listener);

    /**
     * remove a Class (listener).
     *
     * @param listener Class that implements IReceiveMessage
     */
    void removeMessageReceiverListener(IReceiveMessage listener);
}
