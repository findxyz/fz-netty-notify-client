package xyz.fz.netty.notify.handler;

import xyz.fz.netty.notify.model.NotifyMessage;

public interface MessageHandler {
    void handle(NotifyMessage message);
}
