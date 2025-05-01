package ai.idealistic.bdw.utils;

import lombok.experimental.UtilityClass;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class RequestUtils {

    public static String[] get(String link, String method, int timeOut) throws Exception {
        boolean isPost = method.equals("POST");
        String[] split = link.split(" ");

        URL url = new URL(split[0]);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("User-Agent", "1"); // todo change ID
        connection.setRequestMethod(method);
        connection.setReadTimeout(timeOut);
        connection.setDoOutput(true);
        connection.connect();

        if (isPost && split.length > 1) {
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(split[1]);
            wr.flush();
            wr.close();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        List<String> array = new ArrayList<>(50);
        String line;

        while ((line = in.readLine()) != null) {
            array.add(line);
        }
        in.close();
        return array.toArray(new String[0]);
    }

}
