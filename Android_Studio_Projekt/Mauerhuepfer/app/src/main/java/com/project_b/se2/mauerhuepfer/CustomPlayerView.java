package com.project_b.se2.mauerhuepfer;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Julian Janda on 05.06.2016.
 */
public class CustomPlayerView extends View {

    private Player[] players;

    public CustomPlayerView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    protected void onDraw(Canvas canvas) {
        //Draw figures
        for (Player player : players) {
            for (int j = 0; j < player.getFigures().length; j++) {
                player.getFigures()[j].getImage().draw(canvas);
            }
        }
    }
}
