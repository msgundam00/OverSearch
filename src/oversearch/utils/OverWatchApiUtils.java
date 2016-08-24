package oversearch.utils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import oversearch.client.OverClient;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;

/**
 * Created by msgundam00 on 2016. 8. 18..
 */
public class OverWatchApiUtils {
    static final String css_player_level = ".player-level > div";
    static final String css_competitive_rank = ".competitive-rank > div";
    static final String css_competitive_section = "#competitive-play > section"; // [0] 주요 통계 [1] 상위 영웅 [2] 통계
    static final String css_get_hero_name = ".bar-text > .title";  //[0] 1순위 [1] 2순위 [3] 3순위 영웅 이름
    static final String css_get_hero_time = ".bar-text > .description"; //[0] 1순위 [1] 2순위 [3] 3순위 영웅 플레이 시간

    static int hashID = 0; //임시

    public static String getApiBT(String tag) {
        return tag.replace("#", "-");
    }

    public static OverClient getProfile(String tag) throws Exception {
        // TODO: Code Refactoring
        String BT = URLEncoder.encode(getApiBT(tag), "UTF-8");
        String url = "https://playoverwatch.com/ko-kr/career/pc/kr/";

        Document doc = Jsoup.connect(url + BT).get();
        Element element;
        if ((element = doc.select(css_competitive_rank).first()) == null) {
            //경쟁전 점수가 없는 경우
            return null;
        }
        OverClient overClient = new OverClient(hashID);
        int id_level = Integer.parseInt(doc.select(css_player_level).first().text()); //계정 레벨
        int id_rank = Integer.parseInt(element.text()); //계정 랭크점수

        element = doc.select(css_competitive_section).get(1);
        String[] most_hero = new String[3];
        String[] most_hero_time = new String[3];
        for(int i = 0; i < 3; i++) {
            most_hero[i] = element.select(css_get_hero_name).get(i).text();
            most_hero_time[i] = element.select(css_get_hero_time).get(i).text();
        }

        System.err.println("get Profile 완료");

        return overClient;
        /*
        URI uri = new URI("https://playoverwatch.com/ko-kr/career/pc/kr/" + BT);

        String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
        String host = "playoverwatch.com";
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
                            ChannelPipeline entries = ch.pipeline().addLast(new LoggingHandler())
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

                // Set some example cookies.
                request.headers().set(
                        HttpHeaders.Names.COOKIE,
                        ClientCookieEncoder.encode(
                                new DefaultCookie("my-cookie", "foo"),
                                new DefaultCookie("another-cookie", "bar")));

            // Send the HTTP request.
            ch.writeAndFlush(request);

            // Wait for the server to close the connection.
            ch.closeFuture().sync();
        } finally {
            // Shut down executor threads to exit.
            group.shutdownGracefully();
        }*/
    }

    public static void main(String [] args) throws Exception {
        String m = "망겜전문가#3983";
        String n = "그렌라간#31288";
        OverClient oc = getProfile(m);

        System.out.println("bye");
    }
}
