package ai.idealistic.bdw.utils;

import java.net.URL;

public class JavaURL {

    public static boolean isValid(String url, boolean nullOk) {
        if (url == null || url.isEmpty()) {
            return nullOk;
        }
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
