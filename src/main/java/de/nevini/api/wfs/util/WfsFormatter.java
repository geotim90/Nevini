package de.nevini.api.wfs.util;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

public class WfsFormatter {

    public static String formatEta(OffsetDateTime expiry) {
        long seconds = OffsetDateTime.now().until(expiry, ChronoUnit.SECONDS);

        if (seconds < 0) {
            return "";
        }

        if (seconds < 60) {
            return seconds + "s";
        }

        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + "m " + (seconds % 60) + "s";
        }

        long hours = minutes / 60;
        if (hours < 24) {
            return hours + "h " + (minutes % 60) + "m " + (seconds % 60) + "s";
        }

        long days = hours / 24;
        return days + "d " + (hours % 24) + "h " + (minutes % 60) + "m " + (seconds % 60) + "s";
    }

}
