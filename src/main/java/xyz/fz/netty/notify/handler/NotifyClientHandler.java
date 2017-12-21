package xyz.fz.netty.notify.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import xyz.fz.netty.notify.client.NotifyClient;
import xyz.fz.netty.notify.model.NotifyMessage;
import xyz.fz.netty.notify.util.BaseUtil;

public class NotifyClientHandler extends SimpleChannelInboundHandler<String> {

    private String fromId;

    private MessageHandler messageHandler;

    public NotifyClientHandler(String fromId, MessageHandler messageHandler) {
        this.fromId = fromId;
        this.messageHandler = messageHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NotifyMessage message = new NotifyMessage.Builder().connectMessage().from(fromId).build();
        ctx.channel().writeAndFlush(BaseUtil.toDelimiterJson(message));
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final String s) throws Exception {
        BaseUtil.scheduledExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // done server message
                System.out.println("server message: " + s);
                NotifyMessage message = BaseUtil.parseJson(s, NotifyMessage.class);
                NotifyMessage pongMessage = new NotifyMessage.Builder().pongMessage().build();
                if (message != null) {
                    switch (message.getType()) {
                        case NotifyMessage.MESSAGE:
                            ctx.channel().writeAndFlush(BaseUtil.toDelimiterJson(pongMessage));
                            messageHandler.handle(message);
                            break;
                        case NotifyMessage.PING:
                            ctx.channel().writeAndFlush(BaseUtil.toDelimiterJson(pongMessage));
                            break;
                        case NotifyMessage.PONG:
                            break;
                        case NotifyMessage.CONNECT:
                            break;
                        case NotifyMessage.CLOSE:
                            NotifyClient.run = false;
                            ctx.close();
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
