package de.karasuma.discordbot.conannews;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class WikiArticleChecker {

    /**
     *
     * @param doc, Jsoup Doument of article that is checked for content
     * @return  true, if article has content,
     *          false, if article is emtpy / does not exist.
     *          Returns false when fetching URL returns 404.
     */
    boolean articleExists(Document doc) {
        if (doc == null) {
            return false;
        }
        Elements elements = doc.getElementsByClass("noarticletext mw-content-ltr");
        return elements.isEmpty();
    }
}
