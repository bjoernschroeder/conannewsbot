package de.karasuma.discordbot.conannews;

import de.karasuma.discordbot.commandhandling.Command;
import de.karasuma.discordbot.conannews.pagehandler.PageHandler;
import de.karasuma.discordbot.conannews.pagehandler.PageHandlerFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public class CommandWiki implements Command {

    private final CoolDownHandler coolDownHandler = new CoolDownHandler();
    private final String conanWikiBaseURL = "https://conanwiki.org/wiki/";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    public void action(String[] args, MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || coolDownHandler.isOnCoolDown() || args.length == 0) {
            return;
        }

        String searchTerm = parseSearchTerm(args);
        System.out.println(searchTerm);

        PageHandlerFactory pageHandlerFactory = new PageHandlerFactory();
        PageHandler pageHandler = pageHandlerFactory.getPageHandler(searchTerm);
        pageHandler.handlePage(event, searchTerm, coolDownHandler);
    }

    private String parseSearchTerm(String[] args) {
        StringBuilder searchTerm = new StringBuilder(args[0]);
        for (int i = 1; i < args.length; i++) {
            searchTerm.append(" ")
                    .append(args[i].trim());
        }
        return searchTerm.toString();
    }

    @Override
    public void executed(boolean b, MessageReceivedEvent messageReceivedEvent) {

    }

    @Override
    public String help() {
        return null;
    }

}
