package com.project_b.se2.mauerhuepfer.interfaces;

import android.support.annotation.IntDef;

import com.project_b.se2.mauerhuepfer.UpdateState;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rohrbe on 14.05.16.
 */
public interface IReceiveMessage {

    /**
     * Possible Usage Codes:
     * PLAYERID - get Player ID.
     * DICE - any Player diced and share it with the others.
     * MOVE1 - any Player used his first dice.
     * MOVE2 - any Player used his second dice.
     * STARTGAME - Host start the Game.
     */
    @Retention(RetentionPolicy.CLASS)
    @IntDef({USAGE_GAME_INITIALISED, USAGE_CLICKED_PLAYER, USAGE_CLICKED_BLOCK, USAGE_PLAYER_ID, USAGE_DICE_SELECTED, USAGE_DICE_ROLLED, USAGE_REMOVE_FIGURE, USAGE_NEXT_PLAYER, USAGE_START_GAME, USAGE_MSG, USAGE_JOIN, USAGE_RESTART, USAGE_CHEATED})
    @interface UpdateUsageCode {
    }

    int USAGE_GAME_INITIALISED = 1025;
    int USAGE_CLICKED_PLAYER = 1026;
    int USAGE_CLICKED_BLOCK = 1027;
    int USAGE_PLAYER_ID = 1028;
    int USAGE_DICE_SELECTED = 1029;
    int USAGE_DICE_ROLLED = 1030;
    int USAGE_REMOVE_FIGURE = 1031;
    int USAGE_NEXT_PLAYER = 1032;
    int USAGE_START_GAME = 1033;
    int USAGE_MSG = 1034;
    int USAGE_JOIN = 1035;
    int USAGE_RESTART = 1036;
    int USAGE_CHEATED = 1037;


    /**
     * recieve Message and Game Updates
     *
     * @param status Containing Message and Game Parameters
     */
    void receiveMessage(UpdateState status);
}
