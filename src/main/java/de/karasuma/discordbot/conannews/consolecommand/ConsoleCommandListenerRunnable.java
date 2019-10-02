package de.karasuma.discordbot.conannews.consolecommand;
import java.util.HashMap;
import java.util.Scanner;

import de.karasuma.discordbot.conannews.DiscordBot;
import de.karasuma.discordbot.conannews.Main;

public class ConsoleCommandListenerRunnable implements Runnable {

    private Main main;
    private Scanner scanner;
    private HashMap<String, ConsoleCommand> commands;


    public ConsoleCommandListenerRunnable(Main main) {
        this.setMain(main);
    }

    @Override
    public void run() {
        commands = new HashMap<>();
        for (DiscordBot bot : getMain().getBots().values()) {
            if (bot.getConsoleCommands() != null) {
                commands.putAll(bot.getConsoleCommands());
            }
        }
        commands.put("help", new ConsoleCommandHelp(this));
        commands.put("status", new ConsoleCommandStatus(this));
        commands.put("exit", new ConsoleCommandExit(this));
        while(getMain().getRunning()) {
            scanner = new Scanner(System.in);
            ConsoleCommand command = commands.get(scanner.next());
            if (command != null) {
                System.out.println(command.execute(scanner.nextLine().trim()));
            } else {
                System.out.println("Invalid console command");
            }
        }
    }

    public HashMap<String, ConsoleCommand> getCommands() {
        return commands;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

}
