package de.karasuma.discordbot.conannews;

import de.karasuma.discordbot.conannews.consolecommand.ConsoleCommandListenerRunnable;
import de.karasuma.discordbot.conannews.mode.Mode;
import de.karasuma.discordbot.conannews.mode.TestMode;

import java.util.HashMap;

public class Main {

    private FileReadAndWriter fileReadAndWriter;
    private HashMap<String, DiscordBot> bots = new HashMap<String, DiscordBot>();
    private boolean running;

    private Mode mode;

    public static void main(String[] args) {
        Main main = new Main();
        main.createBotAndFileReadAndWriter();
    }

    public void createBotAndFileReadAndWriter() {
        mode = new TestMode();
        bots.put("welcome", new WelcomeBot(this, "ConanNews-Welcome"));
        bots.put("wiki", new WikiBot(this, "ConanWiki"));

        fileReadAndWriter = new FileReadAndWriter(this);

        for (DiscordBot bot : bots.values()) {
            bot.init();
        }

        running = true;
        Thread thread = new Thread(new ConsoleCommandListenerRunnable(this));
        thread.setDaemon(false);
        thread.start();

        System.out.println("Bots loaded successful. You can now start operating.");
    }

    public FileReadAndWriter getFileReaderAndWriter() {
        return fileReadAndWriter;
    }

    public HashMap<String, DiscordBot> getBots() {
        return bots;
    }

    public void setRunning(boolean value) {
        running = value;
    }

    public boolean getRunning() {
        return running;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }
}
