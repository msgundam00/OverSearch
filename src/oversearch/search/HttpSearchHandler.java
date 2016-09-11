package oversearch.search;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import oversearch.client.ClientPoolWSHandler;
import oversearch.client.OverClient;

import java.io.RandomAccessFile;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static oversearch.OverSearchServer.ROOT_DIR;

/**
 * Created by msgundam00 on 2016. 8. 17..
 */
public class HttpSearchHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String path;
    private ClientPoolWSHandler[] clientPoolWSHandlers;

    public HttpSearchHandler(String path, ClientPoolWSHandler[] cps) {
        this.path = path;
        this.clientPoolWSHandlers = cps;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        if (path.equals(req.uri())) {
            if (req.method() == HttpMethod.POST) {
                // TODO: Parse SearchOpt
                OverSearchOpt opt = new OverSearchOpt(req.content().toString(CharsetUtil.UTF_8));
                searchClients(opt);
            }
            else if (req.method() == HttpMethod.GET) {
                // TODO: Pass Search Html File
                RandomAccessFile raf = new RandomAccessFile(ROOT_DIR + "/res/client/search.html", "r");

                HttpResponse res = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
                HttpUtil.setContentLength(res, raf.length());
                res.headers().set(CONTENT_TYPE, "text/html");
                if (HttpUtil.isKeepAlive(req)) {
                    HttpUtil.setKeepAlive(res, true);
                }
                ctx.write(res); // 응답 헤더 전송

                ctx.write(new DefaultFileRegion(raf.getChannel(), 0, raf.length()));

                ChannelFuture f = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
                if (!HttpUtil.isKeepAlive(req)) {
                    f.addListener(ChannelFutureListener.CLOSE);
                }
            }
        }
        else {
            ctx.fireChannelRead(req.retain());
        }
    }

    private List<OverClient> searchClients(OverSearchOpt opt) {
        // TODO: parse opt && get OverClient from clientPoolWSHandlers
        return null;
    }
}
