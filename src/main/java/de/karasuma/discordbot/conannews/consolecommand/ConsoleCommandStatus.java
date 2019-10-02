package de.karasuma.discordbot.conannews.consolecommand;

import de.karasuma.discordbot.conannews.DiscordBot;

public class ConsoleCommandStatus implements ConsoleCommand {

    private ConsoleCommandListenerRunnable consoleCommandListener;

    public ConsoleCommandStatus(ConsoleCommandListenerRunnable consoleCommandListener) {
        this.consoleCommandListener = consoleCommandListener;
    }

    @Override
    public String execute(String input) {
        String message = "";
        for (DiscordBot bot : consoleCommandListener.getMain().getBots().values()) {
            message += bot.toString() + ": " + bot.getJDA().getStatus() + "\n";
        }
        return message;
    }

    @Override
    public String getDescription() {
        return "Shows online status of all Bots";
    }

}
