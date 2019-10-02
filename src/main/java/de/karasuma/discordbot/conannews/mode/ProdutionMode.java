package de.karasuma.discordbot.conannews.mode;

import de.karasuma.discordbot.conannews.Token;

public class ProdutionMode implements Mode {
    @Override
    public String getWelcomeToken() {
        return Token.TOKEN_WELCOME;
    }

    @Override
    public String getWikiToken() {
        return Token.TOKEN_WIKI;
    }
}
