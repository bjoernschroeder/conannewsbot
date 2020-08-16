package de.karasuma.discordbot.conannews.pagehandler;

import de.karasuma.discordbot.conannews.CoolDownHandler;
import de.karasuma.discordbot.conannews.util.NoSearchResultMessageCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class PageHandler {

    final String conanWikiBaseURL = "https://conanwiki.org/wiki/";
    final String conanWikiUrl = "https://conanwiki.org/";

    final String CONANWIKI_API_BASE_URL = "https://conanwiki.org/api.php";
    final String URL_SPACE_SYMBOL = "_";
    final String SUCCESS_SEARCH_COLOR = "#36a1e8";

    final String ACTION_QUERY = "action=query";
    final String PROP_REVISIONS = "prop=revisions";
    final String LIST_SEARCH = "list=search";
    final String SRSEARCH = "srsearch=";
    final String FORMAT = "format=";
    final String TITLES = "titles=";

    public abstract void handlePage(MessageReceivedEvent event, String searchTerm, CoolDownHandler coolDownHandler);

    void sendMessage(MessageReceivedEvent event, EmbedBuilder embedBuilder, CoolDownHandler coolDownHandler) {
        event.getChannel().sendMessage(embedBuilder.build()).queue();
        coolDownHandler.setOnCoolDown();
    }

    void sendNoSearchResultsMessage(MessageReceivedEvent event, CoolDownHandler coolDownHandler) {
        EmbedBuilder noSearchResultMessage = new NoSearchResultMessageCreator().generateNoSearchResultMessage();
        sendMessage(event, noSearchResultMessage, coolDownHandler);
    }

}
