package oversearch.client;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * Created by msgundam00 on 2016. 8. 26..
 */
public class OverMessage {
    @Override
    public String toString() {
        return new String();
    }

    public static OverMessage parse(TextWebSocketFrame t) {
        t.text();
        return new OverMessage();
    }
}
