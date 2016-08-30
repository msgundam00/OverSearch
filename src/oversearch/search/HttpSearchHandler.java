package oversearch.search;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import oversearch.client.OverClient;
import oversearch.utils.ClientPool;

import java.util.List;

/**
 * Created by msgundam00 on 2016. 8. 17..
 */
public class HttpSearchHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String path;
    private ClientPool[] clientPools;

    public HttpSearchHandler(String path, ClientPool[] cps) {
        this.path = path;
        this.clientPools = cps;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        if (path.equals(req.uri())) {
            if (req.method() == HttpMethod.POST) {
                // TODO: Parse SearchOpt
                OverSearchOpt opt = new OverSearchOpt(req.content().toString());
                searchClients(opt);
            }
            else if (req.method() == HttpMethod.GET) {
                // TODO: Pass Search Html File
            }
        }
        else {
            ctx.fireChannelRead(req);
        }
    }

    private List<OverClient> searchClients(OverSearchOpt opt) {
        // TODO: parse opt && get OverClient from clientPools
        return null;
    }
}
