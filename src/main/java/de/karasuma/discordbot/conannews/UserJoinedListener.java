package de.karasuma.discordbot.conannews;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
// import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
        } catch (IOException e) {
            // message file does not exist or is invalid, create new message file
            msgRaw = DEFAULT_WELCOME_MSG;
            createMsgFile(msgFilePath, DEFAULT_WELCOME_MSG);
        }
        // add mentions of user
        return msgRaw.replaceAll("@USER", userMention);
    }

    private void createMsgFile(String msgFilePath, String msg) {
        ObjectMapper om = new ObjectMapper();
        ObjectNode node = om.createObjectNode();
        node.put("test", "val");

        try {
            FileWriter file = new FileWriter(msgFilePath);
            file.write(node.toPrettyString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readMsgFromFile(String msgFilePath) throws FileNotFoundException, IOException {
        File file = new File(msgFilePath);
        if (!file.isFile()) {
            throw new FileNotFoundException("The provided path " + msgFilePath + " is not a file.");
        }

        // JSONObject jsonObject = createJSONObject(file);
        JsonNode node = createJsonNode(file);
        if (!node.has("message")) {
            throw new IOException("The provided json does not contain a message key.");
        }

        return node.get("message").asText();
    }

    private JsonNode createJsonNode(File msgFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(msgFile);
    }
}
