package oversearch.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * Created by msgundam00 on 2016. 8. 17..
 */
public class OverClientCodec extends MessageToMessageCodec<String, OverClient> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, OverClient overClient, List<Object> list) throws Exception {
        list.add(overClient.toString());
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, String s, List<Object> list) throws Exception {
        list.add(OverClient.parse(s));
    }
}
