package de.karasuma.discordbot.conannews.pagehandler;

import com.cedarsoftware.util.StringUtilities;
import de.karasuma.discordbot.conannews.CoolDownHandler;
import de.karasuma.discordbot.conannews.WikiArticleChecker;
import de.karasuma.discordbot.conannews.util.DateUtil;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashSet;
import java.util.Locale;

public class UserPageHandler extends PageHandler {

    private static final String LIST_USERS = "list=users";
    private static final String USUSERS = "ususers=";
    private static final String USPROPS = "usprop=blockinfo" +
            "|groups" +
            "|groupmemberships" +
            "|implicitgroups" +
            "|editcount" +
            "|registration" +
            "|emailable" +
            "|gender" +
            "|centralids" +
            "|cancreate";
    private static final String SUCCESS_USER_SEARCH_COLOR = "#00FF7F";


    @Override
    public void handlePage(MessageReceivedEvent event, String searchTerm, CoolDownHandler coolDownHandler) {
        WikiArticleChecker wikiArticleChecker = new WikiArticleChecker();

        Document doc;
        String title = searchTerm;
        try {
            doc = Jsoup.connect(conanWikiBaseURL + searchTerm).get();
            System.out.println(doc.baseUri());
            if (wikiArticleChecker.articleExists(doc)) {
                Element titleElement = doc.getElementById("firstHeading");
                title = titleElement.wholeText();
                System.out.println(title);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String searchedTitle = searchForPageTitle(title);
        if (!searchedTitle.isEmpty()) {
            title = searchedTitle;
        }
        sendUserMessage(title, event, coolDownHandler);
    }

    private void sendUserMessage(String title, MessageReceivedEvent event, CoolDownHandler coolDownHandler) {
        String username = title.replace("Benutzer:", "");
        try {
            URL url = new URL(CONANWIKI_API_BASE_URL +
                    "?" + ACTION_QUERY +
                    "&" + LIST_USERS +
                    "&" + USUSERS + username +
                    "&" + USPROPS +
                    "&" + FORMAT + "json");

            JSONObject response = new HTTPUtil().getRequest(url);
            JSONObject userInfoJSONObject = response
                    .getJSONObject("query")
                    .getJSONArray("users")
                    .getJSONObject(0);

            if (userInfoJSONObject.isNull("registration")) {
                sendNoSearchResultsMessage(event, coolDownHandler);
                return;
            }

            DecimalFormat decimalFormat = new DecimalFormat();
            DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.GERMAN);
            decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);

            DateUtil dateUtil = new DateUtil();
            String registrationInfo = userInfoJSONObject.getString("registration");
            String registerDate = dateUtil.getDateFromIso(registrationInfo);
            String registerTime = dateUtil.getTimeFromIso(registrationInfo);

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .addField("Anzahl der Bearbeitungen", decimalFormat.format(userInfoJSONObject.getInt("editcount")), true)
                    .addField("Anmeldezeitpunkt", registerDate + ", " + registerTime + " Uhr", true)
                    .setDescription(conanWikiBaseURL + "Benutzer:" + userInfoJSONObject.getString("name"))
                    .setAuthor("Benutzer:" + userInfoJSONObject.getString("name"),
                            conanWikiBaseURL + "Benutzer:" + userInfoJSONObject.getString("name"),
                            "https://conanwiki.org/favicon.png")
                    .setColor(Color.decode(SUCCESS_USER_SEARCH_COLOR));
            sendMessage(event, embedBuilder, coolDownHandler);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    String searchForPageTitle(String title) {
        URL url = null;
        try {
            url = new URL(CONANWIKI_API_BASE_URL +
                    "?" + ACTION_QUERY +
                    "&" + LIST_SEARCH +
                    "&" + SRSEARCH + title.replace(" ", "%20") +
                    "&" + PROP_REVISIONS +
                    "&" + FORMAT + "json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JSONObject request = new HTTPUtil().getRequest(url);
        System.out.println(request);
        if (request.getJSONObject("query").getJSONObject("searchinfo").getInt("totalhits") == 0) {
            return "";
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
        return result.getString("title");
    }
}
