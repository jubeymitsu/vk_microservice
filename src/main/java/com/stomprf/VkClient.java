package com.stomprf;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VkClient {

    private static final String BASE_AUDIO_URL = "https://m.vk.com/audios";
    private static final String USER_AGENT = "Mozilla/5.0"
            + " (iPhone; CPU iPhone OS 12_1 like Mac OS X)"
            + " AppleWebKit/605.1.15"
            + " (KHTML, like Gecko)"
            + " Version/12.0 Mobile/15E148 Safari/604.1";

    private final OkHttpClient httpClient;

    public VkClient(Map<String, String> vkCookies) {
        httpClient = createHttpClient(vkCookies);
    }

    private OkHttpClient createHttpClient(final Map<String, String> vkCookies) {
        return new OkHttpClient()
                .newBuilder()
                .addNetworkInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    Request requestWithUserAgent = originalRequest.newBuilder()
                            .header("User-Agent", USER_AGENT)
                            .build();
                    return chain.proceed(requestWithUserAgent);
                })
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        // does nothing
                    }

                    @NotNull
                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> result = new ArrayList<>();
                        vkCookies.forEach((name, value) -> result.add(new Cookie.Builder()
                                .name(name)
                                .domain("vk.com")
                                .value(value)
                                .build()
                        ));
                        return result;
                    }
                })
                .build();
    }

    public String fromAudio(int ownerId, int offset, boolean my) {
        Request request = new Request.Builder()
                .url(BASE_AUDIO_URL + ownerId + "?offset=" + offset + (my ? "&section=my" : ""))
                .get()
                .build();
        Response response;
        String html = "";
        try {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                html = response.body().string();
            }
        } catch (IOException | NullPointerException e) {
            // nothing
        }

        return html;
    }
}
