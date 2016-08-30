package oversearch.client;

import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

/**
 * Created by msgundam00 on 2016. 8. 18..
 */
public class HttpClientHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String path;
    private ChannelHandler wsHandler;

    public HttpClientHandler(String path, ChannelHandler handler) {
        this.path = path;
        this.wsHandler = handler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        if (req.uri().startsWith(path)) {
            try {
                WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(path, null, true);
                WebSocketServerHandshaker h = wsFactory.newHandshaker(req);
                if (h == null) {
                    WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
                } else {
                    h.handshake(ctx.channel(), req).addListener((ChannelFuture f) -> {
                        // replace the handler when done handshaking
                        ChannelPipeline p = f.channel().pipeline();
                        p.replace(HttpClientHandler.class, "wsHandler", wsHandler);
                    });
                }
            } finally {
                req.release();
            }
        }
        else {
            ctx.fireChannelRead(req);
        }
    }
}
