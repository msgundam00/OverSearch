package oversearch.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

/**
 * Created by msgundam00 on 2016. 8. 17..
 */
public class OverMessageHandler extends SimpleChannelInboundHandler<OverMessage> {
    private final ClientPoolWSHandler pool;
    static final AttributeKey<String > idAttr = AttributeKey.newInstance("id");

    public OverMessageHandler(ClientPoolWSHandler pool) {
        this.pool = pool;
    }


    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush(new OverMessage("HELO", "Admin"));
        pool.getChannel().add(ctx.channel());
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.channel().writeAndFlush(new OverMessage("HELO", "Admin"));
        pool.getChannel().add(ctx.channel());
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) {
        Channel ch = ctx.channel();
        pool.getChannel().remove(ch);
        // pool.removeUser(getId(ch));
        // TODO: send remove info
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, OverMessage msg) throws Exception {
        Channel ch = ctx.channel();
        if (msg.cmd.equals("HELO")) {
            // Add Channel
            setId(ch, msg.id);

            //pool.activateUser(msg.id);
            pool.getChannel().add(ch);
        }
    }

    private void setId(Channel ch, String id) {
        ch.attr(idAttr).set(id);
    }

    private String getId(Channel ch) {
        return ch.attr(idAttr).get();
    }
}
