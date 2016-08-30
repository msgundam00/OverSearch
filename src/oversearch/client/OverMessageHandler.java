package oversearch.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by msgundam00 on 2016. 8. 17..
 */
public class OverMessageHandler extends SimpleChannelInboundHandler<OverClient> {
    public OverMessageHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, OverClient client) throws Exception {
        if (client != null) {

        }
        ctx.fireChannelRead(client);
    }
}
