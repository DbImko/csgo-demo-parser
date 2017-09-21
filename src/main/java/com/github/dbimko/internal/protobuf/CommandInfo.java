package com.github.dbimko.internal.protobuf;

import com.github.dbimko.DemoFileReader;
import com.github.dbimko.model.QAngle;
import com.github.dbimko.model.Vector;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandInfo {

    public CommandInfo() {
    }

    public void parse(DemoFileReader readerWrapper) {
        // parser info
        int flags = readerWrapper.readSignedInt();
        Vector.parse(readerWrapper);
        QAngle.parse(readerWrapper);
        QAngle.parse(readerWrapper);

        Vector.parse(readerWrapper);
        QAngle.parse(readerWrapper);
        QAngle.parse(readerWrapper);

        flags = readerWrapper.readSignedInt();
        Vector.parse(readerWrapper);
        QAngle.parse(readerWrapper);
        QAngle.parse(readerWrapper);

        Vector.parse(readerWrapper);
        QAngle.parse(readerWrapper);
        QAngle.parse(readerWrapper);
    }
}
