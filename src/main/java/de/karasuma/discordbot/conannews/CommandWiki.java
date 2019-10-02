package de.karasuma.discordbot.conannews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import de.karasuma.discordbot.commandhandling.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandWiki implements Command {

    private String conanWikiBaseURL = "https://conanwiki.org/wiki/";
    private long coolDownTimer = 10000;
    private WikiBot wikibot;

    public CommandWiki(WikiBot wikiBot) {
        this.wikibot = wikiBot;
    }

    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    public void action(String[] args, MessageReceivedEvent event) {

        if (!wikibot.isCooldown()) {
            StringBuilder builder = new StringBuilder();
            String[] urls = null;
            builder.append(conanWikiBaseURL);
            String indicatorTag = "";

            if (args.length > 0) {
                builder.append(args[0]);
                for (int i = 1; i < args.length; i++) {
                    builder.append("_" + args[i]);
                }
                String urlTemp = builder.toString();
                urls = urlTemp.split("#");

                // remove last char if it is _
                String lastChar = urls[0].substring(urls[0].length() - 1);
                System.out.println(lastChar);
                if (lastChar.equals("_")) {
                    String temp = urls[0].substring(0, urls[0].length() - 1);
                    System.out.println(temp);
                    urls[0] = temp;
                }


                if (urls.length > 1) {
                    indicatorTag = searchForIDInWebsite(urls);
                }
            }
            event.getTextChannel().sendMessage(urls[0] + indicatorTag).queue();

            Thread cooldownThread = new Thread(new CoolDownRunnable());
            cooldownThread.start();
        }
    }

    @Override
    public void executed(boolean b, MessageReceivedEvent messageReceivedEvent) {

    }

    @Override
    public String help() {
        return null;
    }

    private String searchForIDInWebsite(String[] urls) {
        BufferedReader br = null;
        String indicatorTag = "";

        try {
            URL url = new URL(urls[0]);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0");
            urlConnection.connect();

            //when redirected get redirected URL
            URL urlRedirect = url;

            if (urlConnection.getResponseCode() == 301) {
                String urlRedirectString = urlConnection.getHeaderField("Location");
                urlRedirect = new URL (urlRedirectString);
                HttpURLConnection urlConnectionRedirect = (HttpURLConnection) urlRedirect.openConnection();
                urlConnectionRedirect.connect();
            }

            br = new BufferedReader(new InputStreamReader(urlRedirect.openStream(), "UTF-8"));
            String line = br.readLine();
            line = br.readLine();

            while (line != null) {
                if (line.contains("id=")
                        && line.toLowerCase().contains(urls[1].toLowerCase())
                        && line.contains("class=\"mw-headline\"")){
                    int indexOfID = line.indexOf("id=");
                    int indexOfFirstQuote = line.indexOf("\"", indexOfID);
                    int indexOfSecondQuote = line.indexOf("\"", indexOfFirstQuote + 1);
                    indicatorTag = line.substring(indexOfFirstQuote + 1, indexOfSecondQuote);
                    return "#" + indicatorTag;
                }

                line = br.readLine();
            }
            return indicatorTag;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return indicatorTag;
    }

    public class CoolDownRunnable implements Runnable {

        @Override
        public void run() {
            wikibot.setCooldown(true);
            try {
                Thread.sleep(coolDownTimer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wikibot.setCooldown(false);
        }

    }

}
