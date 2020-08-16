package de.karasuma.discordbot.conannews.botcommands;

import de.karasuma.discordbot.commandhandling.Command;
import de.karasuma.discordbot.conannews.CoolDownHandler;
import de.karasuma.discordbot.conannews.pagehandler.PageHandler;
import de.karasuma.discordbot.conannews.pagehandler.PageHandlerFactory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandWiki implements Command {

    private final CoolDownHandler coolDownHandler = new CoolDownHandler();

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    public void action(String[] args, MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || coolDownHandler.isOnCoolDown() || args.length == 0) {
            return;
        }

        String searchTerm = parseSearchTerm(args);
        System.out.println(searchTerm);

        PageHandlerFactory pageHandlerFactory = new PageHandlerFactory();
        PageHandler pageHandler = pageHandlerFactory.getPageHandler(searchTerm);
        pageHandler.handlePage(event, searchTerm, coolDownHandler);
    }

    private String parseSearchTerm(String[] args) {
        StringBuilder searchTerm = new StringBuilder(args[0]);
        for (int i = 1; i < args.length; i++) {
            searchTerm.append(" ")
                    .append(args[i].trim());
        }
        return searchTerm.toString();
    }

    @Override
    public void executed(boolean b, MessageReceivedEvent messageReceivedEvent) {

    }

    @Override
    public String help() {
        return null;
    }

}
