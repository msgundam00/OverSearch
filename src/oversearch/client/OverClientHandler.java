package oversearch.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by msgundam00 on 2016. 8. 17..
 */
public class OverClientHandler extends SimpleChannelInboundHandler<OverClient> {
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //ctx.pipeline().addLast(new OverClientCodec(), new OverClientHandler());
    }

    public OverClientHandler(OverClient.RankType type) {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, OverClient client) throws Exception {
        if (client != null) {

        }
    }
}
