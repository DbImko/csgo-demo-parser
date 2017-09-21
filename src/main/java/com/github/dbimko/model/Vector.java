package com.github.dbimko.model;


import com.github.dbimko.DemoFileReader;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class Vector {

    private final float x;
    private final float y;
    private final float z;

    public static Vector parse(DemoFileReader reader) {
        float x = reader.readFloat();
        float y = reader.readFloat();
        float z = reader.readFloat();
        return new Vector(x, y, z);
    }

}
