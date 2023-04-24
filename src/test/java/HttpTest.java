import com.stomprf.Main;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpTest {

    OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.code());
            return response.headers().toString();
        }
    }

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.loadDefaultCookies();
        Main.defaultCookies.entrySet().forEach(System.out::println);
    }

    public static String parseTarget(String url) {
        Pattern p = Pattern.compile("com/audios((-|)[\\d]+)");
        Matcher matches = p.matcher(url);
        if (matches.find()) {
            return matches.group(1);
        }

        p = Pattern.compile("com/(.+)");
        matches = p.matcher(url);
        if (matches.find()) {
            return matches.group(1);
        }
        return null;
    }

}
