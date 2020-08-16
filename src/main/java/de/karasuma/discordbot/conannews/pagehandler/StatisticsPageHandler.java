package de.karasuma.discordbot.conannews.pagehandler;

import de.karasuma.discordbot.conannews.CoolDownHandler;
import de.karasuma.discordbot.conannews.util.DecimalFormatUtil;
import de.karasuma.discordbot.conannews.util.HTTPUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONObject;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

public class StatisticsPageHandler extends PageHandler {

    private final String STATS_REQUEST_URL =
            "https://conanwiki.org/api.php?action=query&meta=siteinfo&siprop=statistics&format=json";
    private final String STATS_WIKI_LINK = "https://conanwiki.org/wiki/Spezial:Statistik";

    @Override
    public void handlePage(MessageReceivedEvent event, String searchTerm, CoolDownHandler coolDownHandler) {
        JSONObject stats = getStats();
        EmbedBuilder statsMessage = generateStatsMessage(stats);
        sendMessage(event, statsMessage, coolDownHandler);
    }

    private EmbedBuilder generateStatsMessage(JSONObject stats) {
        DecimalFormat decimalFormat = new DecimalFormatUtil().getDecimalFormatter();
        double averageEditsPerSite = stats.getInt("edits") / stats.getInt("pages");
        return new EmbedBuilder()
                .addField("Seiten / Artikel",
                        decimalFormat.format(stats.getInt("pages")) + " / " +
                                decimalFormat.format(stats.getInt("articles")),
                        true)
                .addField("Bearbeitungen", decimalFormat.format(stats.getInt("edits")), true)
                .addField("Bearbeitungen pro Seite im Durchschnitt", decimalFormat.format(averageEditsPerSite), true)
                .addField("Bilder", decimalFormat.format(stats.getInt("images")), true)
                .addField("Benutzer / Aktiv", decimalFormat.format(stats.getInt("users")) + " / " +
                        decimalFormat.format(stats.getInt("activeusers")), true)
                .setDescription(STATS_WIKI_LINK)
                .setAuthor("Statistiken", STATS_WIKI_LINK, "https://conanwiki.org/favicon.png")
                .setColor(Color.decode(SUCCESS_SEARCH_COLOR));
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
