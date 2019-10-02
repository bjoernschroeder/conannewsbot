package de.karasuma.discordbot.conannews;

import de.karasuma.discordbot.commandhandling.CommandHandler;
import de.karasuma.discordbot.commandhandling.CommandListener;
import de.karasuma.discordbot.conannews.consolecommand.*;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import java.util.HashMap;

public class WikiBot extends DiscordBot{

    private boolean cooldown = false;
    private final String ACTIVITY_NAME = "ConanWiki.org";

    public WikiBot(Main main, String name) {
        super(main, name);
    }

    @Override
    public void init() {
        setBuilder(new JDABuilder(AccountType.BOT));
        getBuilder().setToken(getMain().getMode().getWikiToken());
        getBuilder().setAutoReconnect(true);
        getBuilder().addEventListeners(new CommandListener());

        getBuilder().setActivity(Activity.of(DEFAULT_ACTIVITY_TYPE, ACTIVITY_NAME));

        addCommands();
        setupConsoleCommands();
        startBot();
    }

    private void addCommands() {
        CommandHandler.commands.put("wiki", new CommandWiki(this));
        CommandHandler.commands.put("Wiki", new CommandWiki(this));
        CommandHandler.commands.put("WIKI", new CommandWiki(this));
        CommandHandler.commands.put("Cw", new CommandWiki(this));
        CommandHandler.commands.put("cw", new CommandWiki(this));
        CommandHandler.commands.put("CW", new CommandWiki(this));
    }

    public boolean isCooldown() {
        return cooldown ;
    }

    public void setCooldown(boolean value) {
        cooldown = value;
    }

    private void setupConsoleCommands() {
        setConsoleCommands(new HashMap<String, ConsoleCommand>());
        getConsoleCommands().put("wiki_stop", new ConsoleCommandStop(this));
        getConsoleCommands().put("wiki_start", new ConsoleCommandStart(this));
        getConsoleCommands().put("wiki_update_gamename", new ConsoleCommandUpdateActivityTitle(this));
        getConsoleCommands().put("wiki_update_gametype", new ConsoleCommandUpdateActivityType(this));
    }

}
