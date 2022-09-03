package de.karasuma.discordbot.conannews.consolecommand;

import de.karasuma.discordbot.conannews.DiscordBot;
import de.karasuma.discordbot.conannews.WelcomeBot;
import de.karasuma.discordbot.conannews.WikiBot;

public class ConsoleCommandStatus implements ConsoleCommand {

    private ConsoleCommandListenerRunnable consoleCommandListener;

    public ConsoleCommandStatus(ConsoleCommandListenerRunnable consoleCommandListener) {
        this.consoleCommandListener = consoleCommandListener;
    }

    @Override
    public String execute(String input) {
        String message = "";
        WelcomeBot welcomeBot = consoleCommandListener.getMain().getWelcomeBot();
        WikiBot wikiBot = consoleCommandListener.getMain().getWikiBot();
        message += welcomeBot.toString() + ": " + welcomeBot.getJDA().getStatus() + "\n";
        message += wikiBot.toString() + ": " + wikiBot.getJDA().getStatus() + "\n";
        return message;
    }

    @Override
    public String getDescription() {
        return "Shows online status of all Bots";
    }

}
