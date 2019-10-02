package de.karasuma.discordbot.conannews.consolecommand;

import java.util.HashMap;
import java.util.Map;

public class ConsoleCommandHelp implements ConsoleCommand {

    private ConsoleCommandListenerRunnable consoleCommandsListener;

    public ConsoleCommandHelp(ConsoleCommandListenerRunnable consoleCommandListener) {
        this.consoleCommandsListener = consoleCommandListener;
    }

    @Override
    public String execute(String input) {
        String message = "";
        HashMap<String, ConsoleCommand> commands = consoleCommandsListener.getCommands();
        for (Map.Entry<String, ConsoleCommand> key : commands.entrySet()) {
            message += key.getKey() + ": " + key.getValue().getDescription() + "\n";
        }
        return message;
    }

    @Override
    public String getDescription() {
        return "Get a description for all available commands";
    }

}
