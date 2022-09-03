package de.karasuma.discordbot.conannews.consolecommand;

import de.karasuma.discordbot.conannews.DiscordBot;

public class ConsoleCommandExit implements ConsoleCommand {

    private ConsoleCommandListenerRunnable consoleCommandListener;

    public ConsoleCommandExit(ConsoleCommandListenerRunnable consoleCommandListener) {
        this.consoleCommandListener = consoleCommandListener;
    }

    //TODO: rewrite, dont return log string
    @Override
    public String execute(String input) {
        consoleCommandListener.getMain().getWikiBot().shutDownBot();
        consoleCommandListener.getMain().getWelcomeBot().shutDownBot();

        System.out.println("All Bots OFFLINE");
        consoleCommandListener.getMain().setRunning(false);
        return "Application has been shut down" + "\n";
    }

    @Override
    public String getDescription() {
        return "Ends the program";
    }

}
