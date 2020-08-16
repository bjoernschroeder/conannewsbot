package de.karasuma.discordbot.conannews.pagehandler;

public class PageHandlerFactory {

    public PageHandler getPageHandler(String searchTerm) {
        String lowerCaseSearchTerm = searchTerm.toLowerCase();
        if (isUserPage(lowerCaseSearchTerm)) {
            return new UserPageHandler();
        } else if (isStatisticsPage(lowerCaseSearchTerm)) {
            return new StatisticsPageHandler();
        } else if (isSpecialPage(lowerCaseSearchTerm)) {
            return new SpecialPageHandler();
        }

        return new ArticlePageHandler();
    }

    private boolean isSpecialPage(String searchTerm) {
        return searchTerm.startsWith("special") || searchTerm.startsWith("spezial");
    }

    private boolean isUserPage(String searchTerm) {
        return searchTerm.startsWith("user") || searchTerm.startsWith("benutzer");
    }

    private boolean isStatisticsPage(String searchTerm) {
        String searchTermWithoutSpecial = searchTerm.replace("special:", "")
                .replace("spezial:", "");
        return searchTermWithoutSpecial.startsWith("stats")
                || searchTermWithoutSpecial.startsWith("statistic")
                || searchTermWithoutSpecial.startsWith("statistik")
                || searchTermWithoutSpecial.startsWith("statistics")
                || searchTermWithoutSpecial.startsWith("statistiken");
    }

}
