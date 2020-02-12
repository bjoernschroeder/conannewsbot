package de.karasuma.discordbot.conannews;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebsiteIDSearcher {

    /**
     * @param doc: Jsoup Document to search
     * @param id: id to search for
     * @return value of attribute id
     * <p>
     * This method looks up all headlines of paragraphs of an article of conanwiki.org
     * and searches for a headline which contains the selected provided string in id
     * <p>
     * Example: When searching on the website https://conannews.org/wiki/kogoro for a paragraph containing
     * "verg", searchForID returns "Vergangenheit".
     */
    String searchForID(Document doc, String id) {

        // null check, empty id check
        if (doc == null || id == null || id.isEmpty()) {
            return "";
        }

        Elements mwHeadlines = doc.getElementsByClass("mw-headline");
        for (Element element : mwHeadlines) {
            String indicatorTag = element.attr("id");
            if (indicatorTag.toLowerCase().contains(id) || element.text().toLowerCase().contains(id)) {
                return indicatorTag;
            }
        }

        return "";
    }
}
