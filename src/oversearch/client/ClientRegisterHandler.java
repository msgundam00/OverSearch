package oversearch.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import oversearch.utils.OverWatchApiUtils;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by msgundam00 on 2016. 8. 18..
 */
public class ClientRegisterHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String path;
    private CopyOnWriteArrayList<OverClient>[] clientPool;

    public ClientRegisterHandler(String path, CopyOnWriteArrayList<OverClient>[] clientPool) {
        this.path = path;
        this.clientPool = clientPool;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        if (path.equals(req.uri())) {
            String tag = req.content().toString();
            OverWatchApiUtils.getProfile(tag);
            // TODO: reuturn hash id && add client into pool
            HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        }
        else {
            ctx.fireChannelRead(req);
        }
    }
}
