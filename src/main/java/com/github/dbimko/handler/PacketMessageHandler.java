package com.github.dbimko.handler;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.ProtocolMessageEnum;

/**
 * Handler for Packet Entities messages
 *
 * @see com.github.dbimko.internal.model.Command#PACKET
 * @see com.github.dbimko.internal.model.Command#SIGN_ON
 */
public interface PacketMessageHandler {

    void handle(ProtocolMessageEnum commandMessage, GeneratedMessageV3 o);
}
