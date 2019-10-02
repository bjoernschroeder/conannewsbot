package de.karasuma.discordbot.conannews.consolecommand;
import de.karasuma.discordbot.conannews.DiscordBot;
import net.dv8tion.jda.api.entities.Activity;

public class ConsoleCommandUpdateActivityType implements ConsoleCommand {

    private DiscordBot bot;

    public ConsoleCommandUpdateActivityType(DiscordBot bot) {
        this.bot = bot;
    }

    @Override
    public String execute(String input) {
        try {
            int gameTypeIndex = Integer.parseInt(input);
            bot.setActivityType(Activity.ActivityType.fromKey(gameTypeIndex));
            return Activity.ActivityType.fromKey(gameTypeIndex).toString();
        } catch (NumberFormatException e) {
            return "Invalide input";
        }

    }

    @Override
    public String getDescription() {
        return "Update game type of " + bot.toString() + " with following values:\n"
                + "0 = DEFAULT\n"
                + "1 = STREAMING\n"
                + "2 = LISTENING\n"
                + "3 = WATCHING";
    }

}
