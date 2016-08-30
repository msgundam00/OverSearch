package oversearch.utils;

import org.junit.Test;
import oversearch.client.OverClient;

import static oversearch.utils.OverWatchApiUtils.getProfile;

/**
 * Created by msgundam00 on 2016. 8. 19..
 */
public class OverWatchApiUtilsTest {
    @Test
    public void getProfileTest() throws Exception {
        // TODO: Test Code Fix
        String m = "망겜전문가#3983";
        String n = "잔치국수#3418";//"그렌라간#31288";
        OverClient oc = getProfile(n);

        System.out.println("bye");
    }
}
