package de.karasuma.discordbot.conannews.pagehandler;

import de.karasuma.discordbot.conannews.CoolDownHandler;
import de.karasuma.discordbot.conannews.util.HTTPUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class StatisticsPageHandler extends PageHandler {

    private final String STATS_REQUEST_URL =
            "https://conanwiki.org/api.php?action=query&meta=siteinfo&siprop=statistics&format=json";
    private final String STATS_WIKI_LINK = "https://conanwiki.org/wiki/Spezial:Statistik";

    @Override
    public void handlePage(MessageReceivedEvent event, String searchTerm, CoolDownHandler coolDownHandler) {
        JSONObject stats = getStats();
    }

    private JSONObject getStats() {
        URL url = null;
        try {
            url = new URL(STATS_REQUEST_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        JSONObject response = new HTTPUtil().getRequest(url);
        return response
                .getJSONObject("query")
                .getJSONObject("statistics");
    }
}
