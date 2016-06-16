package com.project_b.se2.mauerhuepfer.interfaces;

import android.support.annotation.IntDef;

import com.project_b.se2.mauerhuepfer.UpdateState;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rohrbe on 29.05.16.
 */
public interface INetworkManager {

    /*
     * Advertise and Discover Timeouts
     */
    long TIMEOUT_ADVERTISE = 1000L * 60L;
    long TIMEOUT_DISCOVER = 1000L * 10L;

    /**
     * Possible states for this application:
     * IDLE - GoogleApiClient not yet connected, can't do anything.
     * READY - GoogleApiClient connected, ready to use Nearby Connections API.
     * ADVERTISING - advertising for peers to connect.
     * DISCOVERING - looking for a peer that is advertising.
     * CONNECTED - found a peer.
     */
    @Retention(RetentionPolicy.CLASS)
    @IntDef({STATE_NONETWORK, STATE_IDLE, STATE_READY, STATE_ADVERTISING, STATE_DISCOVERING, STATE_CONNECTED})
    @interface NearbyConnectionState {
    }

    int STATE_NONETWORK = 1022;
    int STATE_IDLE = 1023;
    int STATE_READY = 1024;
    int STATE_ADVERTISING = 1025;
    int STATE_DISCOVERING = 1026;
    int STATE_CONNECTED = 1027;


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

    void disconnect();
}
