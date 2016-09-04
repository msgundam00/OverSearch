package oversearch.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.internal.chmv8.ConcurrentHashMapV8;
import oversearch.utils.ClientPool;

/**
 * Created by msgundam00 on 2016. 8. 17..
 */
public class OverMessageHandler extends SimpleChannelInboundHandler<OverMessage> {
    private final ClientPool[] clientPools;
    static final AttributeKey<Integer> typeAttr = AttributeKey.newInstance("type");
    static final AttributeKey<String > idAttr = AttributeKey.newInstance("id");

    public OverMessageHandler(ClientPool[] clientPools) {
        this.clientPools = clientPools;
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) {
        Channel ch = ctx.channel();
        ClientPool pool = clientPools[getIndex(ch)];

        // pool.removeUser(getId(ch));
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
            ClientPool pool = clientPools[idx];

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
