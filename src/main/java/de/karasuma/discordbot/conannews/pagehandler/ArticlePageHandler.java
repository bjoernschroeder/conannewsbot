package de.karasuma.discordbot.conannews.pagehandler;

import com.cedarsoftware.util.StringUtilities;
import de.karasuma.discordbot.conannews.CoolDownHandler;
import de.karasuma.discordbot.conannews.WikiArticleChecker;
import de.karasuma.discordbot.conannews.util.DecimalFormatUtil;
import de.karasuma.discordbot.conannews.util.HTTPUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

public class ArticlePageHandler extends PageHandler {

    @Override
    public void handlePage(MessageReceivedEvent event, String searchTerm, CoolDownHandler coolDownHandler) {
        WikiArticleChecker wikiArticleChecker = new WikiArticleChecker();

        Document doc;
        String title = searchTerm;
        try {
            doc = Jsoup.connect(conanWikiBaseURL + searchTerm).get();
            if (wikiArticleChecker.articleExists(doc)) {
                Element titleElement = doc.getElementById("firstHeading");
                title = titleElement.wholeText();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String> metaInfo = getMetaInfo(title);
        System.out.println(metaInfo);

        if (metaInfo.isEmpty()) {
            sendNoSearchResultsMessage(event, coolDownHandler);
        } else {
            EmbedBuilder message = generateMessage(metaInfo);
            sendMessage(event, message, coolDownHandler);
        }
    }

    private EmbedBuilder generateMessage(Map<String, String> metaInfo) {
        DecimalFormat decimalFormat = new DecimalFormatUtil().getDecimalFormatter();
        return new EmbedBuilder()
                .addField("Seitenlänge in Bytes", decimalFormat.format(Integer.valueOf(metaInfo.get("size"))), true)
                .addField("Anzahl Wörter", decimalFormat.format(Integer.valueOf(metaInfo.get("wordcount"))), true)
                .setDescription(conanWikiBaseURL + metaInfo.get("title").replace(" ", URL_SPACE_SYMBOL))
                .setAuthor("Suchergebnis: " + metaInfo.get("title"),
                        conanWikiBaseURL + metaInfo.get("title").replace(" ", URL_SPACE_SYMBOL),
                        "https://conanwiki.org/favicon.png")
                .setColor(Color.decode(SUCCESS_SEARCH_COLOR))
                .setFooter("Letzte Änderung von " + metaInfo.get("user") +
                        " am " + metaInfo.get("date") +
                        ", " + metaInfo.get("time") +
                        " Uhr");
    }

    Map<String, String> getMetaInfo(String title) {

        HashMap<String, String> metaInfo = requestArticleInfo(title);
        if (metaInfo.isEmpty()) {
            return metaInfo;
        }
        metaInfo.putAll(requestLastRevisionInformation(metaInfo.get("title"), metaInfo.get("pageid")));
        return metaInfo;
    }

    private HashMap<String, String> requestArticleInfo(String title) {
        HashMap<String, String> metaInfo = new HashMap<>();

        try {
            URL url = new URL(CONANWIKI_API_BASE_URL +
                    "?" + ACTION_QUERY +
                    "&" + LIST_SEARCH +
                    "&" + SRSEARCH + title.replace(" ", "%20") +
                    "&" + PROP_REVISIONS +
                    "&" + FORMAT + "json");

            JSONObject request = new HTTPUtil().getRequest(url);
            System.out.println(request);
            if (request.getJSONObject("query").getJSONObject("searchinfo").getInt("totalhits") == 0) {
                return metaInfo;
            }

            JSONObject result = null;
            JSONArray search = request
                    .getJSONObject("query")
                    .getJSONArray("search");

            HashSet<JSONObject> results = new HashSet<>();

            for (int i = 0; i < search.length(); i++) {
                result = search.getJSONObject(i);
                if (!result.getString("snippet").contains("REDIRECT")
                        && !result.getString("snippet").contains("WEITERLEITUNG")) {
                    results.add(result);
                }
            }

            String searchTermString = title.replace("%20", " ");

            int lowestDistance = Integer.MAX_VALUE;
            for (JSONObject resultJSONObject : results) {
                String retrievedTitle = resultJSONObject.getString("title");
                int distance = StringUtilities.levenshteinDistance(retrievedTitle, searchTermString);
                if (lowestDistance > distance) {
                    lowestDistance = distance;
                    result = resultJSONObject;
                }
            }

            int wordCount = result.getInt("wordcount");
            int size = result.getInt("size");
            int pageId = result.getInt("pageid");
            String articleTitle = result.getString("title");

            metaInfo.put("wordcount", String.valueOf(wordCount));
            metaInfo.put("size", String.valueOf(size));
            metaInfo.put("pageid", String.valueOf(pageId));
            metaInfo.put("title", articleTitle);

        } catch (IOException protocolException) {
            protocolException.printStackTrace();
        }
        return metaInfo;
    }

    private HashMap<String, String> requestLastRevisionInformation(String title, String pageId) {
        HashMap<String, String> lastRevisionInfo = new HashMap<>();
        try {
            URL url = new URL(CONANWIKI_API_BASE_URL +
                    "?" + ACTION_QUERY +
                    "&" + PROP_REVISIONS +
                    "&" + TITLES + title.replace(" ", "%20") +
                    "&" + FORMAT + "json");

            JSONObject request = new HTTPUtil().getRequest(url);
            JSONArray revisions = request
                    .getJSONObject("query")
                    .getJSONObject("pages")
                    .getJSONObject(pageId)
                    .getJSONArray("revisions");

            if (!revisions.isEmpty()) {
                String isoTimestamp = revisions.getJSONObject(0).getString("timestamp");
                String username = revisions.getJSONObject(0).getString("user");

                OffsetDateTime offsetDateTime = OffsetDateTime.parse(isoTimestamp);
                LocalDateTime localDateTime = offsetDateTime.atZoneSameInstant(ZoneId.of("Europe/Berlin")).toLocalDateTime();
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("d. MMM YYYY").withLocale(Locale.GERMAN);
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss").withLocale(Locale.GERMAN);

                lastRevisionInfo.put("user", username);
                lastRevisionInfo.put("date", localDateTime.format(dateFormat)
                        .replace("März", "Mär.")
                        .replace("Juni", "Jun.")
                        .replace("Juli", "Jul."));
                lastRevisionInfo.put("time", localDateTime.format(timeFormat));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lastRevisionInfo;
    }
}
