package de.karasuma.discordbot.conannews;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UserJoinedListener extends ListenerAdapter {

    final String DEFAULT_WELCOME_MSG = "Herzlich willkommen, @USER <:taeter:442749524746698752> " 
        + "Nimm dir einen Moment Zeit, um unsere <#448331066206322718> durchzulesen, beachte die Kanalthemen und sei gegrüßt!";

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        String msg = buildMsg(event.getUser().getAsMention(), "./message.json");
        event.getGuild()
            .getTextChannelsByName("\u2615cafe_poirot", true)
            .get(0)
            .sendMessage(msg)
            .queue();

        //TODO replace with logs
        System.out.println("User " + event.getMember().getUser().getName() + " joined server. Message:");
        System.out.println(msg);
        super.onGuildMemberJoin(event);
    }

    private String buildMsg(String userMention, String msgFilePath) {
        String msgRaw;
        try {
            msgRaw = readMsgFromFile(msgFilePath);
        } catch (IOException | ParseException e) {
            // message file does not exist or is invalid, create new message file
            msgRaw = DEFAULT_WELCOME_MSG;
            createMsgFile(msgFilePath, DEFAULT_WELCOME_MSG);
        }
        // add mentions of user
        return msgRaw.replaceAll("@USER", userMention);
    }

    private void createMsgFile(String msgFilePath, String msg) {
        JSONObject obj = new JSONObject();
        obj.put("message", msg);

        try {
            FileWriter file = new FileWriter(msgFilePath);
            file.write(obj.toString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readMsgFromFile(String msgFilePath) throws FileNotFoundException, IOException, ParseException {
        File file = new File(msgFilePath);
        if (!file.isFile()) {
            throw new FileNotFoundException("The provided path " + msgFilePath + " is not a file.");
        }

        JSONObject jsonObject = createJSONObject(file);
        if (!jsonObject.containsKey("message")) {
            throw new IOException("The provided json does not contain a message key.");
        }

        return (String) jsonObject.get("message");
    }

    private JSONObject createJSONObject(File msgFile) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        FileReader fileReader = new FileReader(msgFile);
        return (JSONObject) parser.parse(fileReader);
    }
}
