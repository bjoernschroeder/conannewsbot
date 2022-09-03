package de.karasuma.discordbot.conannews;

import de.karasuma.discordbot.conannews.consolecommand.*;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.HashMap;

public class WelcomeBot extends DiscordBot {

    public WelcomeBot(Main main, String name) {
        super(main, name);
    }

    @Override
    public void init(String token) {
        JDABuilder jdaBuilder = JDABuilder.createDefault(token);
        jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS, new GatewayIntent[]{GatewayIntent.GUILD_MESSAGES});
        setBuilder(jdaBuilder);
        getBuilder().setToken(token);
        getBuilder().setAutoReconnect(true);
        getBuilder().addEventListeners(new UserJoinedListener());

        getMain().getFileReaderAndWriter().initGameStatusFromFile();

        if (!getMain().getFileReaderAndWriter().isReadableFile()) {
            //TODO: replace with logs
            System.out.println("Error reading file: Creating new file...");
            getBuilder().setActivity(Activity.of(DEFAULT_ACTIVITY_TYPE, DEFAULT_ACTIVITY_TITLE));
            getMain().getFileReaderAndWriter().createJSONFile(DEFAULT_ACTIVITY_TYPE, DEFAULT_ACTIVITY_TITLE);
        }

        setupConsoleCommands();
        startBot();
    }

    private void setupConsoleCommands() {
        setConsoleCommands(new HashMap<>());
        getConsoleCommands().put("welcome_update_gamename", new ConsoleCommandUpdateActivityTitle(this));
        getConsoleCommands().put("welcome_update_gametype", new ConsoleCommandUpdateActivityType(this));
        getConsoleCommands().put("welcome_stop", new ConsoleCommandStop(this));
        getConsoleCommands().put("welcome_start", new ConsoleCommandStart(this));
    }
}
