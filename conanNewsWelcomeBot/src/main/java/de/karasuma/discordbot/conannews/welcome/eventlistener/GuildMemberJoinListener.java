package de.karasuma.discordbot.conannews.welcome.eventlistener;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GuildMemberJoinListener extends ListenerAdapter {

    static String welcomeMessageSplit1 = "Herzlich willkommen, ";
    static String welcomeMessageSplit2 = " <:taeter:442749524746698752> Nimm dir einen Moment Zeit, um unsere <#448331066206322718> durchzulesen, beachte die Kanalthemen und sei gegrüßt!";
    static String filePath = "./message.json";
    String message = "";
    boolean readableFile = false;

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        readableFile = false;
        readFromFile();

        if (readableFile) {
            event.getGuild().getTextChannelsByName("\u2615cafe_poirot", true).get(0)
                    .sendMessage(buildMessage(event)).queue();
        } else {
            event.getGuild().getTextChannelsByName("\u2615cafe_poirot", true).get(0).sendMessage(welcomeMessageSplit1
                    + event.getMember().getUser().getAsMention() + welcomeMessageSplit2).queue();
            createJSONWithDefaultMessage();
        }
        System.out.println("User " + event.getMember().getUser().getName() + " joined server. Message:");
        System.out.println(buildMessage(event).toString());
        super.onGuildMemberJoin(event);
    }

    private CharSequence buildMessage(GuildMemberJoinEvent event) {
        message = message.replaceAll("@USER", event.getMember().getUser().getAsMention());
        return message;
    }

    public static void createJSONWithDefaultMessage() {
        JSONObject obj = new JSONObject();
        String message = welcomeMessageSplit1 + "@USER" + welcomeMessageSplit2;
        obj.put("message", message);

        try {
            FileWriter file = new FileWriter(filePath);
            file.write(obj.toString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFromFile() {
        File file = new File(filePath);
        if (file.isFile()) {
            JSONObject jsonObject = createJSONObject();
            handleJSONObject(jsonObject);
        }
    }

    private JSONObject createJSONObject() {
        File file = new File(filePath);
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

    private void handleJSONObject(JSONObject jsonObject) {
        try {
            if (jsonObject.containsKey("message")) {
                message = (String) jsonObject.get("message");
            } else {
                readableFile = false;
            }
        } catch (NullPointerException e) {
            readableFile = false;
        }
    }
}
