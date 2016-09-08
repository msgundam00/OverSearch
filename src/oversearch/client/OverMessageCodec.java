package oversearch.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

/**
 * Created by msgundam00 on 2016. 8. 17..
 */
public class OverMessageCodec extends MessageToMessageCodec<TextWebSocketFrame, OverMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, OverMessage m, List<Object> list) throws Exception {
        list.add(new TextWebSocketFrame(m.toString()));
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame t, List<Object> list) throws Exception {
        list.add(OverMessage.parse(t));
    }
}
