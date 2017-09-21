package com.github.dbimko.internal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Command {

    /**
     * first message
     */
    SIGN_ON(1),

    /**
     * it's a normal network packet that we stored off
     */
    PACKET(2),

    /**
     * sync client clock to demo tick
     */
    SYNC(3),

    /**
     * Console parser
     */
    CONSOLE(4),

    /**
     * User parser
     */
    USER(5),

    /**
     * network data tables
     */
    DATA_TABLE(6),


    /**
     * end of game.
     */
    STOP(7),

    /**
     * a blob of binary data understood by a callback function
     */
    CUSTOM(8),

    STRING_TABLES(9);

    @Getter
    private final int value;

    private static final Command[] VALUES = Command.values();

    public static Command parse(int value) {
        for (Command command : VALUES) {
            if (value == command.getValue()) {
                return command;
            }
        }
        throw new IllegalArgumentException(String.valueOf(value));
    }
}
