package oversearch.utils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.net.URI;
import java.net.URLEncoder;

/**
 * Created by msgundam00 on 2016. 8. 18..
 */
public class OverWatchApiUtils {
    public static String getApiBT(String tag) {
        return tag.replace("#", "-");
    }

    public static void getProfile(String tag) throws Exception {
        // TODO: Code Refactoring
        String BT = URLEncoder.encode(getApiBT(tag), "UTF-8");

        URI uri = new URI("https://api.lootbox.eu/pc/kr/" + BT + "/profile");

        String scheme = uri.getScheme() == null? "http" : uri.getScheme();
        String host = "api.lootbox.eu";
        int port = 443;

        boolean ssl = "https".equalsIgnoreCase(scheme);
        final SslContext sslCtx;
        if (ssl) {
            sslCtx = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }

        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    // TODO: Add Json Codec
                                    ch.pipeline().addLast(new LoggingHandler())
                                            .addLast(sslCtx.newHandler(ch.alloc()))
                                            .addLast(new HttpClientCodec())
                                            .addLast(new HttpContentDecompressor())
                                            .addLast(new HttpObjectAggregator(65536))
                                            .addLast(new ProfileApiHandler());
                                }
                            });
            // Make the connection attempt.
            Channel ch = b.connect(host, port).sync().channel();
            // Prepare the HTTP request.
            HttpRequest request = new DefaultHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath());
            request.headers().set(HttpHeaders.Names.HOST, host);
            request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            request.headers().set(HttpHeaders.Names.ACCEPT, "application/json");
            //request.headers().set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);

                /*// Set some example cookies.
                request.headers().set(
                        HttpHeaders.Names.COOKIE,
                        ClientCookieEncoder.encode(
                                new DefaultCookie("my-cookie", "foo"),
                                new DefaultCookie("another-cookie", "bar")));
*/
            // Send the HTTP request.
            ch.writeAndFlush(request);

            // Wait for the server to close the connection.
            ch.closeFuture().sync();
        } finally {
            // Shut down executor threads to exit.
            group.shutdownGracefully();
        }
    }
}
