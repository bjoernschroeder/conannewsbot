package de.karasuma.discordbot.conannews;

import junit.framework.Assert;
import org.junit.Test;

public class WebsiteIDSearcherTest {

    /*
    Test WebsiteIDSearcher
    Search on Website "https://conanwiki.org/wiki/kogoro"
    for an ID containing "verg" (not case sensitive)
     */
    @Test
    public void testWebsiteIDSearcher() {
        WebsiteIDSearcher websiteIDSearcher = new WebsiteIDSearcher();

        //check for existing id
        String[] urls = new String[]{"https://conanwiki.org/wiki/kogoro", "verg"};
        String indicatorTag = websiteIDSearcher.searchForID(urls);
        Assert.assertEquals("Vergangenheit", indicatorTag);

        //check for empty id
        urls[1] = "";
        indicatorTag = websiteIDSearcher.searchForID(urls);
        Assert.assertEquals("", indicatorTag);

        //check for non existing id
        urls[1] = "zuk";
        indicatorTag = websiteIDSearcher.searchForID(urls);
        Assert.assertEquals("", indicatorTag);

        //check for null id
        urls[1] = null;
        indicatorTag = websiteIDSearcher.searchForID(urls);
        Assert.assertEquals("", indicatorTag);

        //check for null url
        urls[0] = null;
        indicatorTag = websiteIDSearcher.searchForID(urls);
        Assert.assertEquals("", indicatorTag);
    }

}
