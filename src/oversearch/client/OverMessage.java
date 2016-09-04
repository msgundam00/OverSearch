package oversearch.client;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * Created by msgundam00 on 2016. 8. 26..
 */
public class OverMessage {
    public final String cmd;
    public final String id;
    public String target;

    public OverMessage(String cmd, String id) {
        this(cmd, id, null);
    }

    public OverMessage(String cmd, String id, String target) {
        this.cmd = cmd;
        this.id = id;
        this.target = target;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        if (target != null) {
            b.append(target).append(" ").append(cmd);
        }

        return b.toString();
    }

    public static OverMessage parse(TextWebSocketFrame t) {
        String[] tokens = t.text().split("\\s+");
        if (tokens.length < 2) {
            throw new IllegalArgumentException();
        }
        else if (tokens.length > 3) {
            return new OverMessage(tokens[0], tokens[1], tokens[2]);
        }

        return new OverMessage(tokens[0], tokens[1]);
    }
}
