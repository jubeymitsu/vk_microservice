package com.stomprf;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static final String USER_AGENT = "Mozilla/5.0"
            + " (iPhone; CPU iPhone OS 12_1 like Mac OS X)"
            + " AppleWebKit/605.1.15"
            + " (KHTML, like Gecko)"
            + " Version/12.0 Mobile/15E148 Safari/604.1";

    public static Map<String, String> vkCookies = new HashMap<>();

    public static void main(String[] args) {
        Main main = new Main();
        main.loadVkCookies("src/main/resources/cookies/cookies.txt");
        main.createHttpClient(vkCookies);

    }

    public void loadVkCookies(String cookieFile) {
        StringBuilder cookieBuilder =  new StringBuilder();
        try {
            BufferedReader cookieBufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(cookieFile)
            ));
//            cookieBuilder = new StringBuilder();
            while (cookieBufferedReader.ready()) {
                cookieBuilder.append(cookieBufferedReader.readLine());
            }
            cookieBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] rawCookies = cookieBuilder.toString().split(";\\s*");
        if (rawCookies.length == 0) {
            System.out.println("Cookies is empty");
            throw new RuntimeException("Empty cookies");
        }

        vkCookies = new HashMap<>();
        for (int i = 0; i != rawCookies.length; i++) {
            String[] cookie = rawCookies[i].split("=", 2);
            if (cookie.length < 2) {
                continue;
            }
            vkCookies.put(cookie[0].trim(), cookie[1].trim());
        }
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


}
