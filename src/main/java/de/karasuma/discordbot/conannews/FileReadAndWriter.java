package de.karasuma.discordbot.conannews;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import net.dv8tion.jda.api.entities.Activity;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileReadAndWriter {

    final String filePath = "./info.json";
    boolean readableFile = false;
    Main main;

    public FileReadAndWriter(Main main) {
        this.main = main;
    }

    protected JSONObject createJSONObject(String filePathGameStatus) {
        File file = new File(filePathGameStatus);
        JSONParser parser = new JSONParser();
        JSONObject obj = null;

        try {
            FileReader fileReader = new FileReader(file);
            obj = (JSONObject) parser.parse(fileReader);
            readableFile = true;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            readableFile = false;
        }

        return obj;
    }

    protected void createJSONFile (Activity.ActivityType activityType, String activityTitle) {
        JSONObject obj = new JSONObject();
        obj.put("name", activityTitle);

        ArrayList<Activity.ActivityType> activityTypes = new ArrayList<>();
        for (Activity.ActivityType type : Activity.ActivityType.values()) {
            activityTypes.add(type);
        }

        long index = activityTypes.indexOf(activityType);

        obj.put("typeIndex", index);

        try {
            FileWriter file = new FileWriter(filePath);
            file.write(obj.toString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void initGameStatusFromFile() {
        File file = new File(filePath);
        if (file.isFile()) {
            JSONObject jsonObject = createJSONObject(filePath);
            handleJSONObject(jsonObject);
        }
    }

    private void handleJSONObject(JSONObject jsonObject) {
        try {
            if (jsonObject.containsKey("name") && jsonObject.containsKey("typeIndex")) {
                String gameName = (String) jsonObject.get("name");

                ArrayList<Activity.ActivityType> activityTypes = new ArrayList<>();
                for (Activity.ActivityType gameType : Activity.ActivityType.values()) {
                    activityTypes.add(gameType);
                }

                Long activityTypeIndex = (Long) jsonObject.get("typeIndex");
                Integer index = Integer.valueOf(String.valueOf(activityTypeIndex));
                Activity.ActivityType activityType = null;

                if (activityTypeIndex != null) {
                    if (activityTypeIndex >= 0 && activityTypeIndex <= 3) {
                        activityType = activityTypes.get(index);
                    } else {
                        activityType = activityTypes.get(0);
                    }

                }

                main.getBots().get("welcome").setActivityTitle(gameName);
                main.getBots().get("welcome").setActivityType(activityType);

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
