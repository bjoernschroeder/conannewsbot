package de.karasuma.discordbot.conannews.botcommands;

import de.karasuma.discordbot.commandhandling.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONObject;

public class StatisticsCommand implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        JSONObject statistics = getStatistics();
    }

    private JSONObject getStatistics() {
        JSONObject statistics = new JSONObject();
        return statistics;
    }

    @Override
    public void executed(boolean sucess, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
