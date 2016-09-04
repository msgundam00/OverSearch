package oversearch.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import oversearch.utils.ClientPool;

/**
 * Created by msgundam00 on 2016. 8. 22..
 */
public class OverClientWSHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private final ClientPool[] clientPools;

    public OverClientWSHandler(ClientPool[] clientPools) {
        this.clientPools = clientPools;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        ctx.channel().pipeline().addLast(new OverMessageCodec(), new OverMessageHandler(clientPools));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        if (msg instanceof TextWebSocketFrame) {
           ctx.fireChannelRead(msg);
        }
        else {
        }
    }
}
