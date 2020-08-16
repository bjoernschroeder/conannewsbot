package de.karasuma.discordbot.conannews.pagehandler;

public class PageHandlerFactory {

    public PageHandler getPageHandler(String searchTerm) {
        String lowerCaseSearchTerm = searchTerm.toLowerCase();
        if (isPageType("user", "benutzer", lowerCaseSearchTerm)) {
            return new UserPageHandler();
        } else if (isPageType("special", "spezial", lowerCaseSearchTerm)) {
            return new SpecialPageHandler();
        }

        return new ArticlePageHandler();
    }

    private boolean isPageType(String englishType, String germanType, String searchTerm) {
        return searchTerm.startsWith(englishType) || searchTerm.startsWith(germanType);
    }

}
