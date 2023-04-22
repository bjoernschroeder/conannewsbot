package de.karasuma.discordbot.conannews;

import javax.security.auth.login.LoginException;

import de.karasuma.discordbot.conannews.consolecommand.ConsoleCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import java.util.HashMap;

public abstract class DiscordBot {

    private Main main;
    private JDABuilder builder;
    private JDA jda;

    protected final Activity.ActivityType DEFAULT_ACTIVITY_TYPE = Activity.ActivityType.PLAYING;
    protected final String DEFAULT_ACTIVITY_TITLE = "Empfangskomitee";

    private Activity.ActivityType activityType = DEFAULT_ACTIVITY_TYPE;
    private String activityTitle = DEFAULT_ACTIVITY_TITLE;
    private HashMap<String, ConsoleCommand> consoleCommands;
    private String name;

    public DiscordBot(Main main, String name) {
        this.setMain(main);
        this.name = name;
    }

    public abstract void init(String token);


    //TODO rewrite shutdown process, dont return string logs
    public String shutDownBot() {
        if (jda != null && jda.getStatus() == JDA.Status.SHUTDOWN) {
            return "Bot is already OFFLINE";
        }
        jda.shutdown();

        if (allBotsOffline()) {
            return "All bots OFFLINE. Type 'exit' to close application";
        }

        return "Bot has been shut down";
    }

    private boolean allBotsOffline() {
        //TODO: create isShutDown method for bots
        return main.getWelcomeBot().getJDA().getStatus() == JDA.Status.SHUTDOWN && main.getWikiBot().getJDA().getStatus() == JDA.Status.SHUTDOWN;
    }

    //TODO: rewrite, dont return string logs
    public String startBot() {
        if (jda != null && jda.getStatus() == JDA.Status.CONNECTED) {
            return "Bot is already ONLINE";
        }
        try {
            jda = getBuilder().build();
            jda.awaitReady();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return "Bot has been successfully started";
    }

    //TODO: rewrite updating activity:
    //      - pass activity to updateActivity
    //      - receive activity via console
    //      - updateActivity stores new activity in file
    private void updateActivity() {
        if(getBuilder() != null && activityType != null && activityTitle != null) {
            if (jda != null) {
                jda.getPresence().setActivity(Activity.of(activityType, activityTitle));
            }
            getBuilder().setActivity(Activity.of(activityType, activityTitle));
            getMain().getFileReaderAndWriter().createJSONFile(activityType, activityTitle);
        }
    }

    public HashMap<String, ConsoleCommand> getConsoleCommands() {
        return consoleCommands;
    }

    //TODO: Remove setters for activityTitle
    public void setActivityTitle(String input) {
        activityTitle = input;
        updateActivity();
    }
    public void setActivityType(Activity.ActivityType activityType) {
        this.activityType = activityType;
        updateActivity();
    }
    public String getActivityTitle() {
        return activityTitle;
    }


    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public JDA getJDA() {
        return jda;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public JDABuilder getBuilder() {
        return builder;
    }

    public void setBuilder(JDABuilder builder) {
        this.builder = builder;
    }

    public void setConsoleCommands(HashMap<String, ConsoleCommand> consoleCommands) {
        this.consoleCommands = consoleCommands;
    }
}
