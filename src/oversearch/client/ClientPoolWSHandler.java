package oversearch.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by msgundam00 on 2016. 8. 22..
 */
public class ClientPoolWSHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private final ChannelGroup channel = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private final CopyOnWriteArrayList<OverClient> clientPool;
    private final OverClient.RankType type;

    public ClientPoolWSHandler(OverClient.RankType type) throws Exception {
        this.clientPool = new CopyOnWriteArrayList<>();
        this.type = type;
    }

    public void addUser(OverClient client) {
        clientPool.add(client);
    }

    public List<OverClient> getClient(int count) {
        List<OverClient> ret = new Vector<>();
        for (int idx = 0; idx < count; idx++) {
            OverClient client = clientPool.get(idx);
            ret.add(client);

            // TODO: Write Flush Ret Info
            // channels.writeAndFlush(new OverMessage())
        }
        return ret;
    }

    public ChannelGroup getChannel() {
        return this.channel;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        ctx.channel().pipeline().addLast(new OverMessageCodec(), new OverMessageHandler(this));
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
