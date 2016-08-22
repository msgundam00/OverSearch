package oversearch.search;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;

/**
 * Created by msgundam00 on 2016. 8. 17..
 */
public class HttpSearchHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String path;

    public HttpSearchHandler(String path) {
        this.path = path;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        if (req.method() == HttpMethod.POST && path.equals(req.uri())) {

        }
        else {
            ctx.fireChannelRead(req);
        }
    }
}
