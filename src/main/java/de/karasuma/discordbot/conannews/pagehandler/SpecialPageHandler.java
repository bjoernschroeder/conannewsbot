package de.karasuma.discordbot.conannews.pagehandler;

import de.karasuma.discordbot.conannews.CoolDownHandler;
import de.karasuma.discordbot.conannews.util.HTTPUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class SpecialPageHandler extends PageHandler {

    private final String diffBaseUrl = "https://conanwiki.org/index.php?diff=";
    private final String SPECIAL_SITE_COLOR = "#F1C40F";
    private static final String REVIDS = "revids=";
    private static final String RVPROP = "rvprop=ids" +
            "|flags" +
            "|timestamp" +
            "|user" +
            "|userid" +
            "|contentmodel" +
            "|comment" +
            "|parsedcomment" +
            "|content" +
            "|tags";

    @Override
    public void handlePage(MessageReceivedEvent event, String searchTerm, CoolDownHandler coolDownHandler) {
        try {
            Connection.Response response = Jsoup
                    .connect(conanWikiBaseURL + searchTerm)
                    .followRedirects(true)
                    .execute();
            String redirectedUrl = response.url().toString();

            if (isDiff(redirectedUrl)) {
                JSONObject diffInfo = getDiffInfo(redirectedUrl);
                assert !diffInfo.isEmpty();
                sendDiffMessage(event, redirectedUrl, coolDownHandler, diffInfo);
            } else {
                String decodedPath = URLDecoder.decode(redirectedUrl, StandardCharsets.UTF_8.name());
                System.out.println(decodedPath);
                sendSpecialSiteLink(event, decodedPath, coolDownHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendDiffMessage(MessageReceivedEvent event, String url, CoolDownHandler coolDownHandler,
                                 JSONObject diffInfo) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.decode(SPECIAL_SITE_COLOR))
                .setAuthor(diffInfo.getString("title"),
                        url,
                        "https://conanwiki.org/favicon.png")
                .setDescription(url.replace(" ", "_"));
        sendMessage(event, embedBuilder, coolDownHandler);
    }

    private JSONObject getDiffInfo(String redirectedUrl) {
        String diffId = redirectedUrl.replace(diffBaseUrl, "");
        try {
            URL url = new URL(CONANWIKI_API_BASE_URL +
                    "?" + ACTION_QUERY +
                    "&" + PROP_REVISIONS +
                    "&" + REVIDS + diffId +
                    "&" + RVPROP +
                    "&" + FORMAT + "json"
            );
            JSONObject response = new HTTPUtil().getRequest(url);
            JSONObject pages = response.getJSONObject("query")
                    .getJSONObject("pages");

            Iterator<String> keys = pages.keys();
            if (keys.hasNext()) {
                String key = keys.next();
                JSONObject page = pages.getJSONObject(key);
                return page
                        .getJSONArray("revisions")
                        .getJSONObject(0)
                        .put("title", page.get("title"));
            }
            return new JSONObject();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    private boolean isDiff(String redirectedUrl) {
        return redirectedUrl.startsWith(conanWikiUrl + "index.php?diff=");
    }

    private void sendSpecialSiteLink(MessageReceivedEvent event, String url, CoolDownHandler coolDownHandler) {
        System.out.println(url);
        String title = url.replace(conanWikiBaseURL, "").replace(conanWikiUrl, "");
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.decode(SPECIAL_SITE_COLOR))
                .setAuthor("Suchergebnis: " + title,
                        url,
                        "https://conanwiki.org/favicon.png")
                .setDescription(url.replace(" ", "_"));
        sendMessage(event, embedBuilder, coolDownHandler);
    }
}
