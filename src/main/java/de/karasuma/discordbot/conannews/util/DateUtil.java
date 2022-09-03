package de.karasuma.discordbot.conannews.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtil {

    public String getDateFromIso(String isoTimeStamp) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(isoTimeStamp);
        LocalDateTime localDateTime = offsetDateTime.atZoneSameInstant(ZoneId.of("Europe/Berlin")).toLocalDateTime();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("d. MMM YYYY").withLocale(Locale.GERMAN);
        return localDateTime.format(dateFormat).replace("März", "Mär.").replace("Juni", "Jun.").replace("Juli", "Jul.");
    }

    public String getTimeFromIso(String isoTimeStamp) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(isoTimeStamp);
        LocalDateTime localDateTime = offsetDateTime.atZoneSameInstant(ZoneId.of("Europe/Berlin")).toLocalDateTime();
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss").withLocale(Locale.GERMAN);
        return localDateTime.format(timeFormat);
    }

}
