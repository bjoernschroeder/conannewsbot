package de.karasuma.discordbot.conannews;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;

import net.dv8tion.jda.api.entities.Activity;

public class FileReadAndWriter {

    final String filePath = "./info.json";
    boolean readableFile = false;
    Main main;

    public FileReadAndWriter(Main main) {
        this.main = main;
    }

    protected JsonNode createJSONObject(String filePathGameStatus) throws IOException {
        File file = new File(filePathGameStatus);
        ObjectMapper om = new ObjectMapper();
        return om.readTree(file);
    }

    @SuppressWarnings("unchecked")
    protected void createJSONFile (Activity.ActivityType activityType, String activityTitle) {
        ObjectMapper om = new ObjectMapper();
        ObjectNode node = om.createObjectNode();
        node.put("name", activityTitle);

        ArrayList<Activity.ActivityType> activityTypes = new ArrayList<>();
        for (Activity.ActivityType type : Activity.ActivityType.values()) {
            activityTypes.add(type);
        }

        long index = activityTypes.indexOf(activityType);
        node.put("typeIndex", index);

        try {
            FileWriter file = new FileWriter(filePath);
            file.write(node.toPrettyString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void initGameStatusFromFile() {
        File file = new File(filePath);
        if (file.isFile()) {
            try {
                JsonNode node = createJSONObject(filePath);
                handleJSONObject(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleJSONObject(JsonNode jsonObject) {
        try {
            if (jsonObject.has("name") && jsonObject.has("typeIndex")) {
                String gameName = jsonObject.get("name").asText();

                ArrayList<Activity.ActivityType> activityTypes = new ArrayList<>();
                for (Activity.ActivityType gameType : Activity.ActivityType.values()) {
                    activityTypes.add(gameType);
                }

                int index = jsonObject.get("typeIndex").asInt(-1);
                Activity.ActivityType activityType = null;

                if (index != -1) {
                    if (index >= 0 && index <= 3) {
                        activityType = activityTypes.get(index);
                    } else {
                        activityType = activityTypes.get(0);
                    }

                }

                // TODO: File reader should not set activity
                main.getWelcomeBot().setActivityTitle(gameName);
                main.getWikiBot().setActivityType(activityType);
                readableFile = true;
            } else {
                readableFile = false;
            }
        } catch (NullPointerException e) {
            readableFile = false;
        }
    }

    public boolean isReadableFile() {
        return readableFile;
    }

}
