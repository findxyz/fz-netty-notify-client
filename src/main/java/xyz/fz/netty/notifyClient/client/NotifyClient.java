package xyz.fz.netty.notifyClient.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import xyz.fz.netty.notifyClient.handler.MessageHandler;
import xyz.fz.netty.notifyClient.handler.NotifyClientHandler;
import xyz.fz.netty.notifyClient.util.BaseUtil;

import java.util.concurrent.TimeUnit;

import static xyz.fz.netty.notifyClient.util.Constants.DELIMITER_BYTES;

public class NotifyClient {

    public static volatile boolean run = true;

    private String host;

    private int port;

    private String fromId;

    private MessageHandler messageHandler;

    public NotifyClient(String host, int port, String fromId, MessageHandler messageHandler) {
        this.host = host;
        this.port = port;
        this.fromId = fromId;
        this.messageHandler = messageHandler;
    }

    public void connect() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ByteBuf delimiter = Unpooled.copiedBuffer(DELIMITER_BYTES);
                    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(8192, delimiter));
                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(new StringEncoder());
                    ch.pipeline().addLast(new NotifyClientHandler(fromId, messageHandler));
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();

            // done client startup
            System.out.println("notify client startup...");
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                workerGroup.shutdownGracefully();
            } finally {
                if (run) {
                    // done reconnect
                    System.out.println("notify client reconnecting...");
                    BaseUtil.scheduledExecutor.schedule(new Runnable() {
                        @Override
                        public void run() {
                            connect();
                        }
                    }, 15 + (int) (Math.random() * 10), TimeUnit.SECONDS);
                } else {
                    System.out.println("notify client shutdown...");
                    BaseUtil.scheduledExecutor.shutdown();
                }
            }
        }
    }
}
