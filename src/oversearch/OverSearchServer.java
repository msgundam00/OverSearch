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
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.logging.LoggingHandler;
import oversearch.client.*;
import oversearch.search.HttpSearchHandler;
import oversearch.utils.HttpNotFoundHandler;

import java.util.concurrent.CopyOnWriteArrayList;

import static oversearch.client.OverClient.*;

/**
 * Created by msgundam00 on 2016. 8. 17..
 */
public final class OverSearchServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class);

            CopyOnWriteArrayList<OverClient>[] clientPool = new CopyOnWriteArrayList[RankType.values().length];

            b.handler(new LoggingHandler())
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpServerCodec())
                                    .addLast(new HttpObjectAggregator(65536))
                                    .addLast(new HttpSearchHandler("/search"))
                                    .addLast(new ClientRegisterHandler("/register", clientPool))
                                    .addLast(new HttpClientHandler("/register"))
                                    .addLast(new JsonObjectDecoder())
                                    .addLast(new HttpNotFoundHandler());
                        }
                    });

            ChannelFuture f = b.bind(8080).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
