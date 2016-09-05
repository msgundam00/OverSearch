package oversearch;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import oversearch.client.ClientRegisterHandler;
import oversearch.client.ClientWSHandshakeHandler;
import oversearch.client.OverClientWSHandler;
import oversearch.search.HttpSearchHandler;
import oversearch.utils.ClientPool;
import oversearch.utils.HttpNotFoundHandler;
import oversearch.utils.HttpStaticFileHandler;

import static oversearch.client.OverClient.RankType;

/**
 * Created by msgundam00 on 2016. 8. 17..
 */
public final class OverSearchServer {
    public static int PORT_NUM = 8080;
    public static String CLIENT_POST_FIX = "/register";
    public static String SEARCH_POST_FIX = "/search";

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class);

            final ClientPool[] clientPools = new ClientPool[RankType.values().length];
            for (RankType r : RankType.values()) {
                clientPools[r.getIndex()] = new ClientPool(r);
            }

            b.handler(new LoggingHandler())
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpServerCodec())
                                    .addLast(new HttpObjectAggregator(65536))
                                    .addLast(new HttpSearchHandler(SEARCH_POST_FIX, clientPools))
                                    .addLast(new ClientRegisterHandler(CLIENT_POST_FIX, clientPools))
                                    .addLast(new ClientWSHandshakeHandler(CLIENT_POST_FIX, new OverClientWSHandler(clientPools)))
                                    .addLast(new HttpStaticFileHandler())
                                    .addLast(new HttpNotFoundHandler());
                        }
                    });

            ChannelFuture f = b.bind(PORT_NUM).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
