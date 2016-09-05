package oversearch.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import oversearch.utils.HttpNotFoundHandler;
import oversearch.utils.OverWatchApiUtils;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static oversearch.client.OverClient.getType;
import static oversearch.utils.HttpNotFoundHandler.sendError;

/**
 * Created by msgundam00 on 2016. 8. 18..
 */
public class ClientRegisterHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String path;
    private ClientPoolWSHandler[] clientPoolWSHandlers;

    public ClientRegisterHandler(String p, ClientPoolWSHandler[] cps) {
        this.path = p;
        this.clientPoolWSHandlers = cps;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        if (path.equals(req.uri())) {
            if (req.method() == HttpMethod.POST) {
                String tag = req.content().toString(CharsetUtil.UTF_8);

                OverClient cli = OverWatchApiUtils.getProfile(tag);
                if (cli == null) {
                    sendError(ctx, HttpResponseStatus.BAD_REQUEST);
                    return;
                }

                clientPoolWSHandlers[getType(cli).getIndex()].addUser(cli);
                // TODO: reuturn hash id
                HttpResponse res = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
                String content = getUserResponse(cli);
                HttpUtil.setContentLength(res, content.length());
                res.headers().set(CONTENT_TYPE, "application/json");
                if (HttpUtil.isKeepAlive(req)) {
                    HttpUtil.setKeepAlive(res, true);
                }
                ctx.write(res); // 응답 헤더 전송
                HttpContent cont = new DefaultHttpContent(Unpooled.copiedBuffer(content.getBytes()));
                ctx.write(cont);
                ChannelFuture f = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
                if (!HttpUtil.isKeepAlive(req)) {
                    f.addListener(ChannelFutureListener.CLOSE);
                }
            }
            else if (req.method() == HttpMethod.GET) {
            }
        }
        else {
            ctx.fireChannelRead(req);
        }
    }

    private String getUserResponse(OverClient cli) {
        StringBuilder b = new StringBuilder();
        b.append("{\"postfix\" : \"")
                .append(this.path)
                .append("/")
                .append(getType(cli).getIndex())
                .append("\", \"id\" : \"")
                .append(cli.hashId)
                .append("\"}");

        return b.toString();
    }
}
