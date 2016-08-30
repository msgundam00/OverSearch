package oversearch.utils;

import oversearch.client.OverClient;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by msgundam00 on 2016. 8. 22..
 */
public class ClientPool {
    private final CopyOnWriteArrayList<OverClient> clientPool;
    private final OverClient.RankType type;
    private final OverClientBot bot;

    public ClientPool(OverClient.RankType type) throws Exception {
        this.clientPool = new CopyOnWriteArrayList<>();
        this.type = type;
        this.bot = new OverClientBot(String.valueOf(type.getIndex()));
    }

    public void addUser(OverClient client) {
        clientPool.add(client);
    }

    public List<OverClient> getClient(int count) {
        List<OverClient> ret = new Vector<>();
        for (int idx = 0; idx < count; idx++) {
            OverClient client = clientPool.get(idx);
            ret.add(client);
        }

        bot.addUsers(ret);
        return ret;
    }
}
