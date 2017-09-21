package com.github.dbimko;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import static com.github.valvesoftware.NetmessagesPublic.SVC_Messages.svc_ServerInfo;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Slf4j
public class Example {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            log.error("No demo file provided");
            System.exit(1);
        }
        String path = args[0];
        FileInputStream in = new FileInputStream(new File(path));
        BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
        DemoFileReader reader = new DemoFileReader(bufferedInputStream);


        try (DemoFileParser parser = new DemoFileParser(reader)) {
            parser.addStopHandler(event -> log.info("Stop event"));

            parser.addPacketMessageHandler(
                    emptyList(),
                    singletonList(svc_ServerInfo),
                    (type, o) -> log.info("Message " + type)//+ " " + o)
            );

            parser.parseHeaders();

            for (int i = 0; parser.hasNextTick(); i++) {
                MDC.put("tick", String.valueOf(i));
                parser.parseNextTick();
            }
        }
    }
}
