package de.karasuma.discordbot.conannews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class WikiArticleCheckerTest {

    @Test
    public void articleExists() {
        WikiArticleChecker wikiArticleChecker = new WikiArticleChecker();
        Document doc = null;
        try {
            doc = Jsoup.connect("https://conanwiki.org/wiki/kogoro").get();
            Assert.assertTrue(wikiArticleChecker.articleExists(doc));

            doc = Jsoup.connect("https://conanwiki.org/wiki/episoden").get();
            Assert.assertFalse(wikiArticleChecker.articleExists(doc));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
