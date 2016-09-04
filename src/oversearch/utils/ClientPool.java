package oversearch.utils;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import oversearch.client.OverClient;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by msgundam00 on 2016. 8. 22..
 */
public class ClientPool {
    private final ChannelGroup channel = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private final CopyOnWriteArrayList<OverClient> clientPool;
    private final OverClient.RankType type;

    public ClientPool(OverClient.RankType type) throws Exception {
        this.clientPool = new CopyOnWriteArrayList<>();
        this.type = type;
    }

    public void addUser(OverClient client) {
        clientPool.add(client);
    }

    public List<OverClient> getClient(int count) {
        List<OverClient> ret = new Vector<>();
        for (int idx = 0; idx < count; idx++) {
            OverClient client = clientPool.get(idx);
            ret.add(client);

            // TODO: Write Flush Ret Info
            // channels.writeAndFlush(new OverMessage())
        }
        return ret;
    }

    public ChannelGroup getChannel() {
        return this.channel;
    }
}
