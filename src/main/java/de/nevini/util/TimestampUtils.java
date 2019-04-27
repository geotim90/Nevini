package de.nevini.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class TimestampUtils {

    public static String formatLargestUnitAgo(long uts) {
        if (uts <= 0) {
            return "unknown";
        }

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime then = LocalDateTime.ofInstant(Instant.ofEpochMilli(uts), ZoneOffset.UTC);

        long years = then.until(now, ChronoUnit.YEARS);
        if (years > 1) {
            return years + " years ago";
        } else if (years > 0) {
            return years + " year ago";
        }

        long months = then.until(now, ChronoUnit.MONTHS);
        if (months > 1) {
            return months + " months ago";
        } else if (months > 0) {
            return months + " month ago";
        }

        long weeks = then.until(now, ChronoUnit.WEEKS);
        if (weeks > 1) {
            return weeks + " weeks ago";
        } else if (weeks > 0) {
            return weeks + " week ago";
        }

        long days = then.until(now, ChronoUnit.DAYS);
        if (days > 1) {
            return days + " days ago";
        } else if (days > 0) {
            return "yesterday";
        }

        long hours = then.until(now, ChronoUnit.HOURS);
        if (hours > 1) {
            return hours + " hours ago";
        } else if (hours > 0) {
            return hours + " hour ago";
        }

        long minutes = then.until(now, ChronoUnit.MINUTES);
        if (minutes > 1) {
            return minutes + " minutes ago";
        } else {
            return "just now";
        }
    }

}
