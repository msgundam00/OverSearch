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
    static final AttributeKey<Integer> typeAttr = AttributeKey.newInstance("type");
    static final AttributeKey<String > idAttr = AttributeKey.newInstance("id");

    public OverMessageHandler(ClientPoolWSHandler pool) {
        this.pool = pool;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(new OverMessage("HELO", "Admin"));
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) {
        Channel ch = ctx.channel();
        pool.getChannel().remove(ch);
        // TODO: send remove info
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, OverMessage msg) throws Exception {
        Channel ch = ctx.channel();
        if (msg.cmd.equals("HELO")) {
            // Add Channel
            int idx = Integer.valueOf(msg.target);
            setId(ch, msg.id);
            setIndex(ch, idx);

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

    private void setIndex(Channel ch, int index) {
        ch.attr(typeAttr).set(index);
    }

    private int getIndex(Channel ch) {
        return ch.attr(typeAttr).get();
    }
}
