package com.stomprf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static Map<String, String> vkCookies = new HashMap<>();

    public static void main(String[] args) {
        Main main = new Main();
        main.loadVkCookies("src/main/resources/cookies/cookies.txt");
        System.out.println(vkCookies.get("remixsid"));
        VkClient client = new VkClient(vkCookies);
        String httpResult = client.fromAudio(Integer.parseInt("718258940"), 0, true);

        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/main/resources/ParseResult.html"));) {
            bufferedWriter.write(httpResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseHttpResult(httpResult, ".AudioPlaylistRoot.AudioBlock__content .audio_item");
    }

    public static String parseHttpResult(String hhtpString,String selector){
        Document doc = Jsoup.parse(hhtpString);
        Elements tracks = doc.select(selector);
        System.out.println("Tracks size: " + tracks.size());
        return "";
    }

    public void loadVkCookies(String cookieFile) {
        StringBuilder cookieBuilder =  new StringBuilder();
        try {
            BufferedReader cookieBufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(cookieFile)
            ));
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

}
