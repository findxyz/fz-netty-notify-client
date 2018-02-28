package xyz.fz.netty.notify;

import xyz.fz.netty.notify.client.NotifyClient;
import xyz.fz.netty.notify.handler.MessageHandler;
import xyz.fz.netty.notify.model.NotifyMessage;

import java.util.UUID;

public class Application {
    public static void main(String[] args) {
        int mustArgLength = 2;
        if (args.length < mustArgLength) {
            System.out.println("参数格式：host port");
            System.exit(0);
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        String id = UUID.randomUUID().toString();

        // done build notify client
        NotifyClient notifyClient = new NotifyClient(host, port, id, new MessageHandler() {
            @Override
            public void handle(NotifyMessage message) {
                System.out.println(message.getData());
            }
        });
        notifyClient.connect();
    }
}
