package oversearch.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import oversearch.client.OverClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by msgundam00 on 2016. 8. 18..
 */
public class OverWatchApiUtils {
    static final String css_player_level = ".player-level > div";
    static final String css_competitive_rank = ".competitive-rank > div";
    static final String css_competitive_section = "#competitive-play > section"; // [0] 주요 통계 [1] 상위 영웅 [2] 통계
    static final String css_get_hero_name = ".bar-text > .title";  //[0] 1순위 [1] 2순위 [3] 3순위 영웅 이름
    static final String css_get_hero_time = ".bar-text > .description"; //[0] 1순위 [1] 2순위 [3] 3순위 영웅 플레이 시간

    //static int hashID = 0; //임시

    public static String getApiBT(String tag) {
        return tag.replace("#", "-");
    }

    public static OverClient getProfile(String tag) throws Exception {
        // TODO: Code Refactoring

        String BT = URLEncoder.encode(getApiBT(tag), "UTF-8");
        String url = "https://playoverwatch.com/ko-kr/career/pc/kr/";

        Connection.Response res = Jsoup.connect(url + BT).ignoreHttpErrors(true).execute();
        if (res.statusCode() == 404) return null;  //계정을 찾을 수 없어 404리턴을 받은 경우 null을 반환할 것.
        Document doc = res.parse();

        Element element;

        int hashID = tag.hashCode();
        OverClient overClient = new OverClient(hashID);
        int id_level = Integer.parseInt(doc.select(css_player_level).first().text()); //계정 레벨
        int id_rank = 0; //계정 경쟁전 점수
        if ((element = doc.select(css_competitive_rank).first()) == null) {
            //경쟁전 점수가 없는 경우(플레이하지 않았거나 배치중인 경우)
            id_rank = -1; //unranked
        } else {
            id_rank = Integer.parseInt(element.text()); //계정 랭크점수
        }
        element = doc.select(css_competitive_section).get(1);
        String[] most_hero = new String[3];
        String[] most_hero_time = new String[3];
        for (int i = 0; i < 3; i++) {
            most_hero[i] = element.select(css_get_hero_name).get(i).text();
            most_hero_time[i] = element.select(css_get_hero_time).get(i).text();
        }

        overClient.setID_Level(id_level);
        overClient.setOverall(id_rank);
        overClient.setMostPlay(most_hero, most_hero_time);

        //System.out.println("get Profile finished");
        return overClient;
    }
}
