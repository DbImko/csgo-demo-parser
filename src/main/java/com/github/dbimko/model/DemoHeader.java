package com.github.dbimko.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DemoHeader {
    /**
     * Should be HL2DEMO
     */
    private final String filestamp;

    /**
     * Name of server
     */
    private final String serverName;

    /**
     * Name of client who recorded the game
     */
    private final String clientName;

    /**
     * Name of map
     */
    private final String mapName;

    /**
     * Name of game directory (com_gamedir)
     */
    private final String gameDirectory;

    /**
     * Demo protocol version
     */
    private final int protocol;

    /**
     * Network protocol version
     */
    private final int networkProtocol;

    /**
     * Time of track
     */
    private final float playbackTime;

    /**
     * # of ticks in track
     */
    private final int ticks;

    /**
     * # of frames in track
     */
    private final int frames;

    /**
     * Length of sigondata in bytes
     */
    private final int signonLength;
}
