package com.github.dbimko.internal.protobuf;

import com.github.dbimko.DemoFileReader;
import com.github.dbimko.handler.PacketMessageHandler;
import com.github.dbimko.handler.StopHandler;
import com.github.dbimko.internal.model.Command;
import com.github.valvesoftware.NetmessagesPublic;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Slf4j
public class CommandReader {

    private final PacketParser packetParser;
    private final CommandInfo commandInfo;

    private StopHandler stopHandler;
    private List<Integer> requestedMessages;
    private PacketMessageHandler packetMessageHandler;

    public CommandReader(PacketParser packetParser,
                         CommandInfo commandInfo) {
        this.packetParser = packetParser;
        this.commandInfo = commandInfo;
    }

    public void parseCommand(DemoFileReader reader) {
        Command command = Command.parse(reader.readByte());

        long inGameTick = reader.readUnsignedInt();
        int playerSlot = reader.readByte();
        log.trace("Player slot " + playerSlot);
        MDC.put("inGameTick", String.valueOf(inGameTick));

        switch (command) {
            case PACKET:
            case SIGN_ON: {
                commandInfo.parse(reader);
                long seqNrIn = reader.readUnsignedInt();
                long seqNrOut = reader.readUnsignedInt();
                log.trace("SeqNrIn " + seqNrIn + ", SeqNrOut " + seqNrOut);
                final int packetLength = reader.readSignedInt();
                packetParser.parse(
                        reader.readBytesAsWrapper(packetLength),
                        requestedMessages,
                        packetMessageHandler
                );
                break;
            }
            case DATA_TABLE: {
                final int packetLength = reader.readSignedInt();
                DemoFileReader localReader = reader.readBytesAsWrapper(packetLength);
                log.debug("Read and skip " + localReader);
                break;
            }
            case STRING_TABLES: {
                final int packetLength = reader.readSignedInt();
                DemoFileReader localReader = reader.readBytesAsWrapper(packetLength, true);
                log.debug("Read and skip " + localReader);
                break;
            }
            case SYNC:
                // skip
                break;
            case STOP: {
                stopHandler.handle(null);
                break;
            }
            default: {
                throw new IllegalStateException("default " + command);
            }
        }
    }

    public void addStopHandler(StopHandler stopHandler) {
        this.stopHandler = stopHandler;
    }

    public void addPacketMessageHandler(List<NetmessagesPublic.NET_Messages> requestedNetMessages,
                                        List<NetmessagesPublic.SVC_Messages> requestedSvcMessages,
                                        PacketMessageHandler packetMessageHandler) {
        requestedMessages = IntStream.concat(
                requestedNetMessages.stream().mapToInt(NetmessagesPublic.NET_Messages::getNumber),
                requestedSvcMessages.stream().mapToInt(NetmessagesPublic.SVC_Messages::getNumber)
        ).boxed().collect(toList());

        this.packetMessageHandler = packetMessageHandler;
    }
}
