package oversearch.utils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import oversearch.client.OverClient;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;

import static oversearch.OverSearchServer.CLIENT_POST_FIX;
import static oversearch.OverSearchServer.PORT_NUM;

/**
 * Created by msgundam00 on 2016. 8. 22..
 */
public class OverClientBot {
    private final CopyOnWriteArrayList<OverClient> waitList = new CopyOnWriteArrayList<>();
    private final Semaphore sem = new Semaphore(0);

    public OverClientBot(String postfix) throws Exception {
        // TODO: Code Refactoring
        URI uri = new URI("ws://localhost:" + PORT_NUM + CLIENT_POST_FIX + postfix);

        String host = "localhost";

        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            final OverClientBotHandler handler = new OverClientBotHandler(
                    WebSocketClientHandshakerFactory.newHandshaker(
                            uri, null, null, false, new DefaultHttpHeaders()));

            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // TODO: Add Json Codec
                            ch.pipeline().addLast(new HttpClientCodec())
                                    .addLast(new HttpObjectAggregator(65536))
                                    .addLast(handler);
                        }
                    });
            // Make the connection attempt.
            Channel ch = b.connect(host, PORT_NUM).sync().channel();
            handler.getChannelFuture().sync();

            while (true) {
                sem.acquire();
                OverClient client = waitList.remove(0);
                // TODO: User Call && User Count
                ch.writeAndFlush(client.toString());
            }
            // Prepare the HTTP request.
        } finally {
            // Shut down executor threads to exit.
            group.shutdownGracefully();
        }
    }

    public void addUsers(List<OverClient> clients) {
        for (OverClient c : clients) {
            waitList.add(c);
            sem.release();
        }
    }
}
