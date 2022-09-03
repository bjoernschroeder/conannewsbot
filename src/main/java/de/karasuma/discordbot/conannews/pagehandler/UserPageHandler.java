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
import java.util.Iterator;
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
        String title = searchTerm;

        try {
            Document doc = Jsoup.connect("https://conanwiki.org/wiki/" + searchTerm).get();
            System.out.println(doc.baseUri());
            if (wikiArticleChecker.articleExists(doc)) {
                Element titleElement = doc.getElementById("firstHeading");
                title = titleElement.wholeText();
                System.out.println(title);
            }
        } catch (IOException var8) {
            var8.printStackTrace();
        }

        JSONObject userPageInfo = this.searchForPage(title);
        if (userPageInfo.isEmpty()) {
            userPageInfo.put("title", title);
        }

        this.sendUserMessage(userPageInfo, event, coolDownHandler);
    }

    private void sendUserMessage(JSONObject userPageInfo, MessageReceivedEvent event, CoolDownHandler coolDownHandler) {
        System.out.println(userPageInfo);
        String username = userPageInfo.getString("title").replace("Benutzer:", "");

        try {
            URL url = new URL("https://conanwiki.org/api.php?action=query&list=users&ususers=" + username + "&usprop=blockinfo|groups|groupmemberships|implicitgroups|editcount|registration|emailable|gender|centralids|cancreate&format=json");
            JSONObject response = (new HTTPUtil()).getRequest(url);
            JSONObject userInfoJSONObject = response.getJSONObject("query").getJSONArray("users").getJSONObject(0);
            if (userInfoJSONObject.isNull("registration")) {
                this.sendNoSearchResultsMessage(event, coolDownHandler);
                return;
            }

            DecimalFormat decimalFormat = new DecimalFormat();
            DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.GERMAN);
            decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
            DateUtil dateUtil = new DateUtil();
            String registrationInfo = userInfoJSONObject.getString("registration");
            String registerDate = dateUtil.getDateFromIso(registrationInfo);
            String registerTime = dateUtil.getTimeFromIso(registrationInfo);
            EmbedBuilder embedBuilder = (new EmbedBuilder()).addField("Anzahl der Bearbeitungen", decimalFormat.format((long)userInfoJSONObject.getInt("editcount")), true).addField("Anmeldezeitpunkt", registerDate + ", " + registerTime + " Uhr", true).setDescription("https://conanwiki.org/wiki/Benutzer:" + userInfoJSONObject.getString("name")).setAuthor("Benutzer:" + userInfoJSONObject.getString("name"), "https://conanwiki.org/wiki/Benutzer:" + userInfoJSONObject.getString("name"), "https://conanwiki.org/favicon.png").setColor(Color.decode("#00FF7F"));
            if (!userPageInfo.isNull("timestamp")) {
                String var10002 = userPageInfo.getString("title");
                URL pageSearchUrl = new URL("https://conanwiki.org/api.php?action=query&prop=revisions&titles=" + var10002.replace(" ", "%20") + "&format=json");
                JSONObject request = (new HTTPUtil()).getRequest(pageSearchUrl);
                JSONArray revisions = request.getJSONObject("query").getJSONObject("pages").getJSONObject(String.valueOf(userPageInfo.getInt("pageid"))).getJSONArray("revisions");
                if (!revisions.isEmpty()) {
                    JSONObject revision = revisions.getJSONObject(0);
                    String var10001 = revision.getString("user");
                    embedBuilder.setFooter("Letzte Änderung von " + var10001 + " am " + dateUtil.getDateFromIso(revision.getString("timestamp")) + ", " + dateUtil.getTimeFromIso(revision.getString("timestamp")) + " Uhr");
                }
            } else {
                embedBuilder.setFooter("Keine Benutzerseite für " + userInfoJSONObject.getString("name"));
            }

            this.sendMessage(event, embedBuilder, coolDownHandler);
        } catch (MalformedURLException var19) {
            var19.printStackTrace();
        }

    }

    JSONObject searchForPage(String title) {
        URL url = null;

        try {
            url = new URL("https://conanwiki.org/api.php?action=query&list=search&srsearch=" + title.replace(" ", "%20") + "&prop=revisions&format=json");
        } catch (MalformedURLException var13) {
            var13.printStackTrace();
        }

        System.out.println(url);
        JSONObject request = (new HTTPUtil()).getRequest(url);
        System.out.println(request);
        if (request.getJSONObject("query").getJSONObject("searchinfo").getInt("totalhits") == 0) {
            return new JSONObject();
        } else {
            JSONObject result = null;
            JSONArray search = request.getJSONObject("query").getJSONArray("search");
            HashSet<JSONObject> results = new HashSet();

            for(int i = 0; i < search.length(); ++i) {
                result = search.getJSONObject(i);
                if (!result.getString("snippet").contains("REDIRECT") && !result.getString("snippet").contains("WEITERLEITUNG")) {
                    results.add(result);
                }
            }

            String searchTermString = title.replace("%20", " ");
            int lowestDistance = Integer.MAX_VALUE;
            Iterator var9 = results.iterator();

            while(var9.hasNext()) {
                JSONObject resultJSONObject = (JSONObject)var9.next();
                String retrievedTitle = resultJSONObject.getString("title");
                int distance = StringUtilities.levenshteinDistance(retrievedTitle, searchTermString);
                if (lowestDistance > distance) {
                    lowestDistance = distance;
                    result = resultJSONObject;
                }
            }

            return result;
        }
    }
}
