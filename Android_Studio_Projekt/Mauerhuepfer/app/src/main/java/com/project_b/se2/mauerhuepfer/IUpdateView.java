package com.project_b.se2.mauerhuepfer;

/**
 * Created by rohrbe on 29.05.16.
 */
public interface IUpdateView {

    /**
     * Change the application state and update the visibility on on-screen views '
     * based on the new state of the application.
     *
     * @param state current State
     */
    void updateView(int state);
}
