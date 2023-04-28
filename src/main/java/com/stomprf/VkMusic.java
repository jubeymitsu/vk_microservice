package com.stomprf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VkMusic {

    public String parseJsonPlaylist(Path path) throws IOException {
        byte[] jsonData = Files.readAllBytes(path);

//create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

//read JSON like DOM Parser
        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode payloadNode = rootNode.path("payload");
//        JsonNode playlistNode = payloadNode.path(1).path(1).path("playlist");
        JsonNode listNode = payloadNode.path(1).path(1).path("playlist").path("list");

        objectMapper.writeValue(path.toFile(), listNode);
        scrapIds(listNode);




        Iterator<JsonNode> elements = payloadNode.elements();
        int count = 0;
        while (elements.hasNext()){
            elements.next();
            count++;
        }
        System.out.println(count);

//        JsonNode phoneNosNode = rootNode.path("phoneNumbers");
//        Iterator<JsonNode> elements = phoneNosNode.elements();
//        while(elements.hasNext()){
//            JsonNode phone = elements.next();
//            System.out.println("Phone No = "+phone.asLong());
//        }
        return "";
    }

    private List<String> scrapIds(JsonNode audioData){
        Iterator<JsonNode> tracks = audioData.elements();
        List<String> ids = new ArrayList<>();
        while (tracks.hasNext()){
            JsonNode track = tracks.next();
            String[] audio_hashes = track.get(13).toString().split("/");
            String fullId = String.format("%s : %s : %s : %s ", track.get(1), track.get(0), audio_hashes[2], audio_hashes[5]);
            ids.add(fullId);
            System.out.println(fullId);
        }
        System.out.println("Ids length: " + ids.size());

        return ids;
    }

}
