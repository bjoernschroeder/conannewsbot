package de.karasuma.discordbot.conannews;

import junit.framework.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;

public class WebsiteIDSearcherTest {

    /*
    Test WebsiteIDSearcher
    Search on Website "https://conanwiki.org/wiki/kogoro"
    for an ID containing "verg" (not case sensitive)
     */
    @Test
    public void testWebsiteIDSearcher() {
        WebsiteIDSearcher websiteIDSearcher = new WebsiteIDSearcher();

        try {
            Document doc = Jsoup.connect("https://conanwiki.org/wiki/kogoro").get();
            //check for existing id
            String indicatorTag = websiteIDSearcher.searchForID(doc, "verg");
            Assert.assertEquals("Vergangenheit", indicatorTag);

            //check for empty id
            indicatorTag = websiteIDSearcher.searchForID(doc, "");
            Assert.assertEquals("", indicatorTag);

            //check for non existing id
            indicatorTag = websiteIDSearcher.searchForID(doc, "zuk");
            Assert.assertEquals("", indicatorTag);

            //check for null id
            indicatorTag = websiteIDSearcher.searchForID(doc, null);
            Assert.assertEquals("", indicatorTag);

            //check for null doc
            indicatorTag = websiteIDSearcher.searchForID(null, "");
            Assert.assertEquals("", indicatorTag);

            //check for special chars
            doc = Jsoup.connect("https://conanwiki.org/wiki/shinichi").get();
            indicatorTag = websiteIDSearcher.searchForID(doc, "pers");
            Assert.assertEquals("Erscheinung_und_Pers.C3.B6nlichkeit", indicatorTag);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
