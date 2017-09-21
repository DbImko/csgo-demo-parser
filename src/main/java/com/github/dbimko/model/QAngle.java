package com.github.dbimko.model;


import com.github.dbimko.DemoFileReader;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class QAngle {

    private final float x;
    private final float y;
    private final float z;

    public static QAngle parse(DemoFileReader reader) {
        float x = reader.readFloat();
        float y = reader.readFloat();
        float z = reader.readFloat();
        return new QAngle(x, y, z);
    }
}
