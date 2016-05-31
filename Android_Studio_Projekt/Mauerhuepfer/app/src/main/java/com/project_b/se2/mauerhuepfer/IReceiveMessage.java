package com.project_b.se2.mauerhuepfer;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rohrbe on 14.05.16.
 */
public interface IReceiveMessage {

    @Retention(RetentionPolicy.CLASS)
    @IntDef({USAGE_PLAYERID, USAGE_DICE, USAGE_MOVE1, USAGE_MOVE2, USAGE_NEXTPLAYER, USAGE_STARTGAME})
    @interface UpdateUsageCode {
    }

    int USAGE_PLAYERID = 1028;
    int USAGE_DICE = 1029;
    int USAGE_MOVE1 = 1030;
    int USAGE_MOVE2 = 1031;
    int USAGE_NEXTPLAYER = 1031;
    int USAGE_STARTGAME = 1032;


    /**
     * recieve Message and Game Updates
     *
     * @param status Containing Message and Game Parameters
     */
    void receiveMessage(UpdateState status);
}
