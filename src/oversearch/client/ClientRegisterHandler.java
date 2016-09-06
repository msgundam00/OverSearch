package oversearch.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import oversearch.utils.OverWatchApiUtils;

import java.io.RandomAccessFile;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static oversearch.OverSearchServer.ROOT_DIR;
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

                ByteBuf content =  Unpooled.copiedBuffer(getUserResponse(cli), CharsetUtil.UTF_8);
                FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK,
                        content);
                HttpUtil.setContentLength(res, content.readableBytes());
                res.headers().set(CONTENT_TYPE, "application/json");
                ctx.writeAndFlush(res);
            }
            else if (req.method() == HttpMethod.GET) {
                try {
                    RandomAccessFile raf = new RandomAccessFile(ROOT_DIR + "/res/client/register00.html", "r");

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
                finally {
                    req.retain();
                }
            }
        }
        else {
            req.retain();
            ctx.fireChannelRead(req);
        }
    }

    private String getUserResponse(OverClient cli) {
        StringBuilder b = new StringBuilder();
        b.append("{\"postfix\" : \"")
                .append(this.path)
                .append("/")
                .append(getType(cli).getIndex())
                .append("\", \"id\" : ")
                .append(cli.hashId)
                .append(", \"level\" : ")
                .append(cli.id_level)
                .append(", \"rank\" : ")
                .append(cli.overall)
                .append(", \"most1\" : [")
                .append("{\"hero\" : \"")
                .append(cli.mostPlay[0].getName())
                .append("\", \"time\" : \"")
                .append(cli.mostPlay[0].getTime())
                .append("\"}, ")
                .append("{\"hero\" : \"")
                .append(cli.mostPlay[1].getName())
                .append("\", \"time\" : \"")
                .append(cli.mostPlay[1].getTime())
                .append("\"}, ")
                .append("{\"hero\" : \"")
                .append(cli.mostPlay[2].getName())
                .append("\", \"time\" : \"")
                .append(cli.mostPlay[2].getTime())
                .append("\"}]}");

        return b.toString();
    }
}
