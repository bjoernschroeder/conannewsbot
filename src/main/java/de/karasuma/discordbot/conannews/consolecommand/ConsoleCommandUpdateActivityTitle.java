package de.karasuma.discordbot.conannews.consolecommand;

import de.karasuma.discordbot.conannews.DiscordBot;

public class ConsoleCommandUpdateActivityTitle implements ConsoleCommand {

    private DiscordBot bot;

    public ConsoleCommandUpdateActivityTitle(DiscordBot bot) {
        this.bot = bot;
    }

    @Override
    public String execute(String input) {
        String oldGameName = bot.getActivityTitle();
        try {
            bot.setActivityTitle(input.trim());
        } catch (IllegalArgumentException e) {
            bot.setActivityTitle(oldGameName);
            return "Invalide Input";
        }
        return "Updated game name to " + input;
    }

    @Override
    public String getDescription() {
        return "Update game name of " + bot.toString();
    }
}
