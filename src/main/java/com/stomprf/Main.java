package com.stomprf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static Map<String, String> vkCookies = new HashMap<>();
    public static Map<String, String> defaultCookies;

    public Main() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.loadVkCookies("src/main/resources/cookies/cookies.txt");
        main.loadDefaultCookies();

        System.out.println(vkCookies.get("remixsid"));
        VkClient client = new VkClient(vkCookies, defaultCookies);
//        String httpResult = client.fromAudio(Integer.parseInt("718258940"), 0, true);
        String httpResult = client.fromAudio(718258940,0, true);
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/ParseResult.html"));
        System.out.println("Result -> " + httpResult);
        writer.write(httpResult);
        writer.close();

//        Elements elements = parseHttpResult(httpResult, ".AudioPlaylistRoot.AudioBlock__content .audio_item");
//
//        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/main/resources/ParseResult.html"));) {
//            bufferedWriter.write(elements.get(0).toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static Elements parseHttpResult(String hhtpString,String selector){
        Document doc = Jsoup.parse(hhtpString);
        Elements tracks = doc.select(selector);
        System.out.println("Tracks size: " + tracks.size());
        return tracks;
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

    public void loadDefaultCookies() throws IOException {
        HashMap<String, String> result =
                new ObjectMapper().readValue(new FileInputStream("src/main/resources/cookies/defaultCookies.json"), HashMap.class);
        defaultCookies = new HashMap<>(result);
    }



}
