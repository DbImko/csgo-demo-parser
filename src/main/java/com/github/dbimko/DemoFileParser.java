package com.github.dbimko;

import com.github.dbimko.handler.PacketMessageHandler;
import com.github.dbimko.handler.StopHandler;
import com.github.dbimko.internal.protobuf.CommandInfo;
import com.github.dbimko.internal.protobuf.CommandReader;
import com.github.dbimko.internal.protobuf.DemoHeaderReader;
import com.github.dbimko.internal.protobuf.PacketParser;
import com.github.dbimko.model.DemoHeader;
import com.github.valvesoftware.NetmessagesPublic;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

/**
 *
 */
@Slf4j
public class DemoFileParser implements AutoCloseable {

    private static final int HEADER_SIZE = 1072;

    private final DemoFileReader reader;
    private final DemoHeaderReader headerReader;
    private final CommandReader commandReader;


    private boolean stop;

    public DemoFileParser(DemoFileReader reader) {
        this.reader = reader;
        headerReader = new DemoHeaderReader();
        commandReader = new CommandReader(
                new PacketParser(),
                new CommandInfo()
        );
    }

    public DemoHeader parseHeaders() {
        return headerReader.parse(reader.readBytesAsWrapper(HEADER_SIZE));
    }

    public void parseNextTick() {
        commandReader.parseCommand(reader);
    }


    public boolean hasNextTick() {
        return !stop;
    }


    @Override
    public void close() throws Exception {
        reader.close();
    }

    public void addStopHandler(StopHandler stopHandler) {
        commandReader.addStopHandler(event -> {
            stop = true;
            stopHandler.handle(event);
        });
    }

    public void addPacketMessageHandler(PacketMessageHandler packetMessageHandler) {
        commandReader.addPacketMessageHandler(
                asList(NetmessagesPublic.NET_Messages.values()),
                asList(NetmessagesPublic.SVC_Messages.values()),
                packetMessageHandler
        );
    }

    public void addPacketMessageHandler(List<NetmessagesPublic.NET_Messages> requestedNetMessages,
                                        List<NetmessagesPublic.SVC_Messages> requestedSvcMessages,
                                        PacketMessageHandler packetMessageHandler) {
        commandReader.addPacketMessageHandler(
                ofNullable(requestedNetMessages).orElse(emptyList()),
                ofNullable(requestedSvcMessages).orElse(emptyList()),
                packetMessageHandler
        );
    }
}
