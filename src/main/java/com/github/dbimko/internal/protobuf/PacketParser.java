package com.github.dbimko.internal.protobuf;

import com.github.dbimko.DemoFileReader;
import com.github.dbimko.handler.PacketMessageHandler;
import com.github.valvesoftware.NetmessagesPublic;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Parser;
import com.google.protobuf.ProtocolMessageEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.valvesoftware.NetmessagesPublic.NET_Messages.net_Disconnect;
import static com.github.valvesoftware.NetmessagesPublic.NET_Messages.net_File;
import static com.github.valvesoftware.NetmessagesPublic.NET_Messages.net_NOP;
import static com.github.valvesoftware.NetmessagesPublic.NET_Messages.net_SetConVar;
import static com.github.valvesoftware.NetmessagesPublic.NET_Messages.net_SignonState;
import static com.github.valvesoftware.NetmessagesPublic.NET_Messages.net_StringCmd;
import static com.github.valvesoftware.NetmessagesPublic.NET_Messages.net_Tick;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_BSPDecal;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_ClassInfo;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_CreateStringTable;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_CrosshairAngle;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_FixAngle;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_GameEvent;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_GameEventList;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_GetCvarValue;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_Menu;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_PacketEntities;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_Prefetch;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_Print;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_SendTable;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_ServerInfo;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_SetPause;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_SetView;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_Sounds;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_TempEntities;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_UpdateStringTable;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_UserMessage;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_VoiceData;
import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_VoiceInit;

@Slf4j
public class PacketParser {

    private Map<ProtocolMessageEnum, Parser<? extends GeneratedMessageV3>> PARSERS = ImmutableMap.<ProtocolMessageEnum, Parser<? extends GeneratedMessageV3>>builder()
            .put(net_NOP, NetmessagesPublic.CNETMsg_NOP.parser())
            .put(net_Disconnect, NetmessagesPublic.CNETMsg_Disconnect.parser())
            .put(net_File, NetmessagesPublic.CNETMsg_File.parser())
            .put(net_Tick, NetmessagesPublic.CNETMsg_Tick.parser())
            .put(net_StringCmd, NetmessagesPublic.CNETMsg_StringCmd.parser())
            .put(net_SetConVar, NetmessagesPublic.CNETMsg_SetConVar.parser())
            .put(net_SignonState, NetmessagesPublic.CNETMsg_SignonState.parser())

            .put(svc_ServerInfo, NetmessagesPublic.CSVCMsg_ServerInfo.parser())
            .put(svc_SendTable, NetmessagesPublic.CSVCMsg_SendTable.parser())
            .put(svc_ClassInfo, NetmessagesPublic.CSVCMsg_ClassInfo.parser())
            .put(svc_SetPause, NetmessagesPublic.CSVCMsg_SetPause.parser())
            .put(svc_CreateStringTable, NetmessagesPublic.CSVCMsg_CreateStringTable.parser())
            .put(svc_UpdateStringTable, NetmessagesPublic.CSVCMsg_UpdateStringTable.parser())
            .put(svc_VoiceInit, NetmessagesPublic.CSVCMsg_VoiceInit.parser())
            .put(svc_VoiceData, NetmessagesPublic.CSVCMsg_VoiceData.parser())
            .put(svc_Print, NetmessagesPublic.CSVCMsg_Print.parser())
            .put(svc_Sounds, NetmessagesPublic.CSVCMsg_Sounds.parser())
            .put(svc_SetView, NetmessagesPublic.CSVCMsg_SetView.parser())
            .put(svc_FixAngle, NetmessagesPublic.CSVCMsg_FixAngle.parser())
            .put(svc_CrosshairAngle, NetmessagesPublic.CSVCMsg_CrosshairAngle.parser())
            .put(svc_BSPDecal, NetmessagesPublic.CSVCMsg_BSPDecal.parser())
            .put(svc_UserMessage, NetmessagesPublic.CSVCMsg_UserMessage.parser())
            .put(svc_GameEvent, NetmessagesPublic.CSVCMsg_GameEvent.parser())
            .put(svc_PacketEntities, NetmessagesPublic.CSVCMsg_PacketEntities.parser())
            .put(svc_TempEntities, NetmessagesPublic.CSVCMsg_TempEntities.parser())
            .put(svc_Prefetch, NetmessagesPublic.CSVCMsg_Prefetch.parser())
            .put(svc_Menu, NetmessagesPublic.CSVCMsg_Menu.parser())
            .put(svc_GameEventList, NetmessagesPublic.CSVCMsg_GameEventList.parser())
            .put(svc_GetCvarValue, NetmessagesPublic.CSVCMsg_GetCvarValue.parser())
            .build();

    public PacketParser() {
    }

    @SneakyThrows
    public void parse(DemoFileReader reader,
                      List<Integer> requestedMessages,
                      PacketMessageHandler packetMessageHandler) {

        do {
            final int cmd = reader.readProtobufVarInt();
            final int length = reader.readProtobufVarInt();
            ProtocolMessageEnum commandMessage = Optional.<ProtocolMessageEnum>ofNullable(NetmessagesPublic.NET_Messages.forNumber(cmd))
                    .orElseGet(() -> NetmessagesPublic.SVC_Messages.forNumber(cmd));

            DemoFileReader localReaderWrapper = reader.readBytesAsWrapper(length);

            if (commandMessage == null) {
                log.debug("Unknown command " + cmd);
            } else {
                if (requestedMessages.contains(commandMessage.getNumber())) {
                    Parser<? extends GeneratedMessageV3> parser = PARSERS.get(commandMessage);
                    if (parser == null) {
                        log.debug("Bad parser message " + commandMessage);
                    } else {
                        GeneratedMessageV3 result = parser.parseFrom(localReaderWrapper.toByteArray());
                        packetMessageHandler.handle(commandMessage, result);
                    }
                }
            }
        } while (reader.length() > 0);
    }

}
