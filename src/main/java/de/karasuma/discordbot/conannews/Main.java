package de.karasuma.discordbot.conannews;

import de.karasuma.discordbot.conannews.consolecommand.ConsoleCommandListenerRunnable;

import java.util.HashMap;

public class Main {

    private FileReadAndWriter fileReadAndWriter;

    private WelcomeBot welcomeBot;
    private WikiBot wikiBot;
    private boolean running;

    public static void main(String[] args) {
        if (args.length < 2) {
            //TODO: Replace with logs
            System.err.println("Application requires at least 2 tokens!");
            return;
        }
        Main main = new Main();
        main.createBotAndFileReadAndWriter(args[0], args[1]);
    }

    public void createBotAndFileReadAndWriter(String welcomeBotToken, String wikiBotToken) {
        welcomeBot = new WelcomeBot(this, "ConanNews-Welcome");
        wikiBot = new WikiBot(this, "ConanWiki");

        fileReadAndWriter = new FileReadAndWriter(this);

        welcomeBot.init(welcomeBotToken);
        wikiBot.init(wikiBotToken);

        running = true;
        Thread thread = new Thread(new ConsoleCommandListenerRunnable(this));
        thread.setDaemon(false);
        thread.start();

        // TODO: Replace with logs
        System.out.println("Bots loaded successful. You can now start operating.");
    }

    public FileReadAndWriter getFileReaderAndWriter() {
        return fileReadAndWriter;
    }

    public WelcomeBot getWelcomeBot() {
        return welcomeBot;
    }

    public WikiBot getWikiBot() {
        return wikiBot;
    }

    public void setRunning(boolean value) {
        running = value;
    }

    public boolean getRunning() {
        return running;
    }
}
