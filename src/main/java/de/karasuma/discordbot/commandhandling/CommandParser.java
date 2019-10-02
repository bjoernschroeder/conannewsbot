package de.karasuma.discordbot.commandhandling;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class CommandParser {

    public final static String PREFIX = "!";

    public static CommandContainer parse(String raw, MessageReceivedEvent event) {
        String beheaded = raw.replaceFirst(PREFIX, "");
        String[] splitbeheaded = beheaded.split(" ");
        String invoke = splitbeheaded[0];
        ArrayList<String> split = new ArrayList<String>();

        for(String string : splitbeheaded) {
            split.add(string);
        }

        String[] args = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);
        return new CommandContainer(raw, beheaded, splitbeheaded, invoke, args, event);
    }

    public static class CommandContainer {
        public final String raw;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String invoke;
        public final String[] args;
        public final MessageReceivedEvent event;

        public CommandContainer(String raw, String beheaded, String[] splitBeheaded,
                                String invoke, String[] args, MessageReceivedEvent event) {
            this.raw = raw;
            this.beheaded = beheaded;
            this.splitBeheaded = splitBeheaded;
            this.invoke = invoke;
            this.args = args;
            this.event = event;
        }
    }

}
