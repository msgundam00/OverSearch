package oversearch.client;

import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

/**
 * Created by msgundam00 on 2016. 8. 18..
 */
public class ClientWSHandshakeHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String path;
    private ClientPoolWSHandler[] clientPoolWSHandlers;

    public ClientWSHandshakeHandler(String path, ClientPoolWSHandler[] clientPoolWSHandlers) {
        this.path = path;
        this.clientPoolWSHandlers = clientPoolWSHandlers;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        if (req.uri().startsWith(path)) {
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(req.uri(), null, true);
            WebSocketServerHandshaker h = wsFactory.newHandshaker(req);
            if (h == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                int idx = Integer.valueOf(req.uri().substring(path.length()+1));

                final ChannelHandler wsHandler = clientPoolWSHandlers[idx];
                h.handshake(ctx.channel(), req).addListener((ChannelFuture f) -> {
                    // replace the handler when done handshaking
                    ChannelPipeline p = f.channel().pipeline();
                    p.replace(ClientWSHandshakeHandler.class, "wsHandler", wsHandler);
                });
            }
        }
        else {
            req.retain();
            ctx.fireChannelRead(req);
        }
    }
}
