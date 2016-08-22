package oversearch.search;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * Created by msgundam00 on 2016. 8. 17..
 */
public class OverSearchCodec extends MessageToMessageCodec<String, OverSearchOpt> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, OverSearchOpt overSearchOpt, List<Object> list) throws Exception {

    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, String s, List<Object> list) throws Exception {

    }
}
