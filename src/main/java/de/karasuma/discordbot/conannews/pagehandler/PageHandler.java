package de.karasuma.discordbot.conannews.pagehandler;

import de.karasuma.discordbot.conannews.CoolDownHandler;
import de.karasuma.discordbot.conannews.util.NoSearchResultMessageCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

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

    JSONObject getRequest(URL url) {
        JSONObject jsonObject = new JSONObject();
        try {
            System.out.println(url);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder response = new StringBuilder();
            while (bufferedReader.ready()) {
                response.append(bufferedReader.readLine());
            }
            urlConnection.disconnect();
            jsonObject = new JSONObject(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

}
