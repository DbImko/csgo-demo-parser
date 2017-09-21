package com.github.dbimko.handler;


/**
 * Stop event handler
 *
 * @see com.github.dbimko.internal.model.Command#STOP
 */
public interface StopHandler {
    void handle(Void event);
}
