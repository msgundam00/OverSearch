package oversearch.client;

/**
 * Created by msgundam00 on 2016. 8. 17..
 */
public class OverClient {
    public OverClient(int hashId) {
        this.hashId = hashId;
    }

    public enum Charactor {
        DVa("D.Va");

        String name;
        Charactor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum RankType {
        _30(0),
        _35(1),
        _40(2),
        _45(3),
        _50(4),
        _55(5),
        _60(6),
        _65(7),
        _70(8),
        _75(9),
        _80(10);

        private int index;

        RankType(int index) {
            this.index = index;
        }

        public static RankType getType(int overall) {
            if (overall < 30) {
                return RankType._30;
            }
            else if (overall < 35) {
                return RankType._35;
            }
            else if (overall < 40) {
                return RankType._40;
            }
            else if (overall < 45) {
                return RankType._45;
            }
            else if (overall < 50) {
                return RankType._50;
            }
            else if (overall < 55) {
                return RankType._55;
            }
            else if (overall < 60) {
                return RankType._60;
            }
            else if (overall < 65) {
                return RankType._65;
            }
            else if (overall < 70) {
                return RankType._70;
            }
            else if (overall < 75) {
                return RankType._75;
            }
            return RankType._80;
        }

        public int getIndex() {
            return index;
        }
    }

    final int hashId;
    int overall;
    Charactor[] mostPlay;

    public static OverClient parse(String tag) throws Exception {
        return null;
    }

    public static RankType getType(OverClient client) {
        return RankType.getType(client.overall);
    }
}
