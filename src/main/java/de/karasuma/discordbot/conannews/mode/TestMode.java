package de.karasuma.discordbot.conannews.mode;

import de.karasuma.discordbot.conannews.Token;

public class TestMode implements Mode {
    @Override
    public String getWelcomeToken() {
        return Token.TOKEN_WELCOME_TEST;
    }

    @Override
    public String getWikiToken() {
        return Token.TOKEN_WIKI_TEST;
    }
}
