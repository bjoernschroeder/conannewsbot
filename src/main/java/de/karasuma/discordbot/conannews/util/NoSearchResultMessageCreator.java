package de.karasuma.discordbot.conannews.util;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class NoSearchResultMessageCreator {

    final String NO_RESULT_COLOR = "#FF0000";

    public EmbedBuilder generateNoSearchResultMessage() {
        return new EmbedBuilder()
                .setColor(Color.decode(NO_RESULT_COLOR))
                .setAuthor("Zu deiner Suchanfrage wurden keine Ergebnisse gefunden.",
                        "https://conanwiki.org/",
                        "https://conanwiki.org/favicon.png");
    }

}
