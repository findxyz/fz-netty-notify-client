package xyz.fz.netty.notifyClient.handler;

import xyz.fz.netty.notifyClient.model.NotifyMessage;

public interface MessageHandler {
    void handle(NotifyMessage message);
}
