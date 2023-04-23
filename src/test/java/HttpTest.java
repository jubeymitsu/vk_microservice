import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

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
        String response = new HttpTest().run("https://www.google.com");
//        System.out.println(response);
    }

}
