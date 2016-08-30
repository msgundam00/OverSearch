package oversearch.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import oversearch.utils.ClientPool;
import oversearch.utils.OverWatchApiUtils;

/**
 * Created by msgundam00 on 2016. 8. 18..
 */
public class ClientRegisterHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String path;
    private ClientPool[] clientPools;

    public ClientRegisterHandler(String s, ClientPool[] cps) {
        this.path = s;
        this.clientPools = cps;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        if (path.equals(req.uri())) {
            if (req.method() == HttpMethod.POST) {
                String tag = req.content().toString();
                OverWatchApiUtils.getProfile(tag);
                // TODO: reuturn hash id && add client into pool
                HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            }
            else if (req.method() == HttpMethod.GET) {

            }
        }
        else {
            ctx.fireChannelRead(req);
        }
    }
}
