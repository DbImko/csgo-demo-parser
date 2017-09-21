package com.github.dbimko.internal.protobuf;

import com.github.dbimko.DemoFileReader;
import com.github.dbimko.model.DemoHeader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoHeaderReader {

    /**
     * max length of a filesystem pathname
     */
    private static final int DEFAULT_MAX_OS_PATH = 260;

    private static final int FILESTAMP = 8;

    public DemoHeader parse(DemoFileReader reader) {
        String filestamp = reader.readCString(FILESTAMP);
        int protocol = reader.readSignedInt();
        int networkProtocol = reader.readSignedInt();

        String serverName = reader.readCString(DEFAULT_MAX_OS_PATH);

        String clientName = reader.readCString(DEFAULT_MAX_OS_PATH);
        String mapName = reader.readCString(DEFAULT_MAX_OS_PATH);
        String gameDirectory = reader.readCString(DEFAULT_MAX_OS_PATH);

        float playbackTime = reader.readFloat();

        int ticks = reader.readSignedInt();
        int frames = reader.readSignedInt();
        int signonLength = reader.readSignedInt();

        return new DemoHeader(filestamp,
                serverName,
                clientName,
                mapName,
                gameDirectory,
                protocol,
                networkProtocol,
                playbackTime,
                ticks,
                frames,
                signonLength);
    }

}
