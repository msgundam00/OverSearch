package oversearch.search;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static oversearch.client.OverClient.*;

/**
 * Created by msgundam00 on 2016. 8. 17..
 */
public class OverSearchOpt {
    public static String RANK_TYPE = "rankRange";
    public static String USE_MIKE = "useMike";

    public RankType rankType;
    public boolean useMike;

    public OverSearchOpt(String c) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject)parser.parse(c);

            String rt = (String)json.get(RANK_TYPE);
            if (rt.endsWith("-")) {

            } else if (rt.endsWith("+")) {

            }

            this.useMike = (boolean)json.get(USE_MIKE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
