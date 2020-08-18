package de.karasuma.discordbot.conannews.pagehandler;

import de.karasuma.discordbot.conannews.CoolDownHandler;
import de.karasuma.discordbot.conannews.util.DateUtil;
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
    private final String FIRST_EDIT_REQUEST_URL =
            "https://conanwiki.org/api.php?action=query" +
                    "&prop=revisions&rvlimit=1&rvprop=timestamp&rvdir=newer&titles=Hauptseite&format=json";

    @Override
    public void handlePage(MessageReceivedEvent event, String searchTerm, CoolDownHandler coolDownHandler) {
        JSONObject stats = getStats();
        EmbedBuilder statsMessage = generateStatsMessage(stats);
        sendMessage(event, statsMessage, coolDownHandler);
    }

    private EmbedBuilder generateStatsMessage(JSONObject stats) {
        DecimalFormat decimalFormat = new DecimalFormatUtil().getDecimalFormatter();
        DateUtil dateUtil = new DateUtil();
        return new EmbedBuilder()
                .addField("Artikel / Seiten",
                        decimalFormat.format(stats.getInt("articles")) + " / " +
                                        decimalFormat.format(stats.getInt("pages")),
                        true)
                .addField("Dateien", decimalFormat.format(stats.getInt("images")), true)
                .addField("Bearbeitungen", decimalFormat.format(stats.getInt("edits")), true)
                .addField("Registrierte Benutzer",
                         decimalFormat.format(stats.getInt("activeusers")) + " von " +
                                 decimalFormat.format(stats.getInt("users")) + " aktiv", true)
                .addField("Gründungsdatum", dateUtil.getDateFromIso(stats.getString("originDateTimestamp"))
                        + ", " + dateUtil.getTimeFromIso(stats.getString("originDateTimestamp")) + " Uhr", true)
                .setDescription(STATS_WIKI_LINK)
                .setAuthor("Statistik", STATS_WIKI_LINK, "https://conanwiki.org/favicon.png")
                .setColor(Color.decode(SUCCESS_SEARCH_COLOR));
    }

    private JSONObject getStats() {
        URL statsRequestUrl = null;
        URL wikiOriginDateRequestUrl = null;
        try {
            statsRequestUrl = new URL(STATS_REQUEST_URL);
            wikiOriginDateRequestUrl = new URL(FIRST_EDIT_REQUEST_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HTTPUtil httpUtil = new HTTPUtil();
        JSONObject response = httpUtil.getRequest(statsRequestUrl);
        JSONObject firstEdit = httpUtil.getRequest(wikiOriginDateRequestUrl);

        JSONObject statistics = response.getJSONObject("query")
                .getJSONObject("statistics");
        String firstEditTimeStamp = firstEdit.getJSONObject("query")
                .getJSONObject("pages")
                .getJSONObject("1")
                .getJSONArray("revisions")
                .getJSONObject(0)
                .getString("timestamp");

        statistics.put("originDateTimestamp", firstEditTimeStamp);
        return statistics;
    }
}
