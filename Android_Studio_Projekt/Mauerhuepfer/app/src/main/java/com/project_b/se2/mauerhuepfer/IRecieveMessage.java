package com.project_b.se2.mauerhuepfer;

/**
 * Created by rohrbe on 14.05.16.
 */
public interface IRecieveMessage {

    /**
     * recieve Message and Game Updates
     *
     * @param status Containing Message and Game Parameters
     */
    void receiveMessage(UpdateState status);
}
