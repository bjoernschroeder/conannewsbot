package de.karasuma.discordbot.conannews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebsiteIDSearcher {

    /**
     * @param urls urls[0]: website to be searched
     *             urls[1]: id to search for on provided website
     * @return value of attribute id
     *
     * This method looks up all headlines of paragraphs of an article of conanwiki.org
     * and searches for a headline which contains the selected provided string in urls[1]
     *
     * Example: When searching on the website https://conannews.org/wiki/kogoro for a paragraph containing
     * "verg", searchForID returns "Vergangenheit".
     */
    public String searchForID(String[] urls) {

        // when no url or id is passed to searchForID return empty string to prevent ArrayIndexOutOfBoundsException
        if (urls.length < 2) {
            return "";
        }

        // null check
        if (urls[0] == null || urls[1] == null) {
            return "";
        }

        //when no id to search for is provided return emtpy string
        if (urls[1].isEmpty()) {
            return "";
        }

        try {
            Document doc = Jsoup.connect(urls[0]).get();
            Elements mwHeadlines = doc.getElementsByClass("mw-headline");
            for (Element element : mwHeadlines) {
                String id = element.attr("id");
                if (id.toLowerCase().contains(urls[1])) {
                    return id;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}
