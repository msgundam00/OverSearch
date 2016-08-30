package oversearch.utils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.Map;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by msgundam00 on 2016. 8. 19..
 */
public class ProfileApiHandler extends SimpleChannelInboundHandler<Object> {
    // TODO: Parseing Json
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object req) throws Exception {
        if (req instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) req;

            System.err.println("STATUS: " + response.status());
            System.err.println("VERSION: " + response.protocolVersion());
            System.err.println();

            if (!response.headers().isEmpty()) {
                for (CharSequence name: response.headers().names()) {
                    for (CharSequence value: response.headers().getAll(name)) {
                        System.err.println("HEADER: " + name + " = " + value);
                    }
                }
                System.err.println();
            }

            if (HttpUtil.isTransferEncodingChunked(response)) {
                System.err.println("CHUNKED CONTENT {");
            } else {
                System.err.println("CONTENT {");
            }
        }
        if (req instanceof HttpContent) {
            HttpContent content = (HttpContent) req;

            System.err.print(content.content().toString(CharsetUtil.UTF_8));
            System.err.flush();

            if (content instanceof LastHttpContent) {
                System.err.println("} END OF CONTENT");
                ctx.close();
            }
        }
        if (req instanceof Map) {
            System.out.println("adsfasdfasdf");
        }
    }
}
