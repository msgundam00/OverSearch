package oversearch.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import oversearch.utils.ClientPool;
import oversearch.utils.OverWatchApiUtils;

import static oversearch.client.OverClient.getType;

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

                OverClient cli = OverWatchApiUtils.getProfile(tag);
                clientPools[getType(cli).getIndex()].addUser(cli);
                // TODO: reuturn hash id
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
