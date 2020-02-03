package de.karasuma.discordbot.conannews;

import de.karasuma.discordbot.commandhandling.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandWiki implements Command {

    private String conanWikiBaseURL = "https://conanwiki.org/wiki/";
    private long coolDownTimer = 10000;
    private WikiBot wikibot;
    private WebsiteIDSearcher websiteIDSearcher = new WebsiteIDSearcher();

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
                    indicatorTag = websiteIDSearcher.searchForID(urls);
                    if (!indicatorTag.equals("")) {
                        indicatorTag = "#" + indicatorTag;
                    }
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
