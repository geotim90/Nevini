package de.nevini.bot.util;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formatter {

    public static String formatFloat(float value) {
        return new DecimalFormat("#0.##", DecimalFormatSymbols.getInstance(Locale.US)).format(value);
    }

    public static String formatInteger(int value) {
        return NumberFormat.getIntegerInstance(Locale.US).format(value);
    }

    public static String formatLargestUnitAgo(long epochMilli) {
        if (epochMilli <= 0) {
            return "unknown";
        } else {
            return formatLargestUnitAgo(ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneOffset.UTC));
        }
    }

    public static String formatLargestUnitAgo(ZonedDateTime value) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

        if (now.isBefore(value)) {
            return "some time in the future";
        }

        long years = value.until(now, ChronoUnit.YEARS);
        if (years > 1) {
            return years + " years ago";
        } else if (years > 0) {
            return years + " year ago";
        }

        long months = value.until(now, ChronoUnit.MONTHS);
        if (months > 1) {
            return months + " months ago";
        } else if (months > 0) {
            return months + " month ago";
        }

        long weeks = value.until(now, ChronoUnit.WEEKS);
        if (weeks > 1) {
            return weeks + " weeks ago";
        } else if (weeks > 0) {
            return weeks + " week ago";
        }

        long days = value.until(now, ChronoUnit.DAYS);
        if (days > 1) {
            return days + " days ago";
        } else if (days > 0) {
            return days + " day ago";
        }

        long hours = value.until(now, ChronoUnit.HOURS);
        if (hours > 1) {
            return hours + " hours ago";
        } else if (hours > 0) {
            return hours + " hour ago";
        }

        long minutes = value.until(now, ChronoUnit.MINUTES);
        if (minutes > 1) {
            return minutes + " minutes ago";
        } else {
            return "just now";
        }
    }

    public static String formatLong(long value) {
        return NumberFormat.getIntegerInstance(Locale.US).format(value);
    }

    public static String formatOsuDisplayHtml(String value) {
        String markdown = value.replaceAll("<img src='/images/(\\w+)_small.png'/>", "**$1**") // resolve rank images
                .replaceAll("<b><a href='(/u/\\d+)'>([^<]+)</a></b>", "$2") // resolve user references
                .replaceAll("<a href='(/b/\\d+\\?m=\\d)'>([^<]+)</a>", "$2") // resolve beatmap references
                // resolve HTML formatting
                .replaceAll("<b>([^<]+)</b>", "**$1**") // bold text emphasis
                // resolve HTML entities
                .replaceAll("&amp;", "&")
                .replaceAll("&gt;", ">")
                .replaceAll("&lt;", "<")
                .replaceAll("&quot;", "\"");
        Matcher rankMatcher = Pattern.compile("\\*\\*([ABCDF]|(?:SS?|X)[H+]?)\\*\\*").matcher(markdown);
        if (rankMatcher.find()) {
            return Formatter.formatOsuRank(rankMatcher.group(1)) + markdown.substring(rankMatcher.end());
        } else {
            return markdown;
        }
    }

    public static String formatOsuRank(@NonNull String value) {
        switch (value) {
            case "A":
                return "<:rankA:581903653078695936>";
            case "B":
                return "<:rankB:581903653200330756>";
            case "C":
                return "<:rankC:581903653200330768>";
            case "D":
            case "F":
                return "<:rankD:581903653049204799>";
            case "S":
                return "<:rankS:581903652831232003>";
            case "SH":
            case "S+":
                return "<:rankSH:581903653070438423>";
            case "SS":
            case "X":
                return "<:rankSS:581903653112381470>";
            case "SSH":
            case "SS+":
            case "XH":
            case "X+":
                return "<:rankSSH:581903653607178280>";
            default:
                return value.isEmpty() ? value : "**" + value + "**";
        }
    }

    public static String formatPercent(double value) {
        return new DecimalFormat("#0.##%", DecimalFormatSymbols.getInstance(Locale.US)).format(value);
    }

    public static String formatSeconds(int seconds) {
        if (seconds < 0) {
            return '-' + formatSeconds(-seconds);
        }

        if (seconds < 60) {
            return Integer.toString(seconds);
        }

        int minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + ":" + seconds % 60;
        }

        int hours = minutes / 60;
        return hours + ":" + minutes % 60 + ":" + seconds % 60;
    }

    public static String formatTimestamp(long epochMilli) {
        return formatTimestamp(ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneOffset.UTC));
    }

    public static String formatTimestamp(ZonedDateTime value) {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(value).replace('T', ' ');
    }

    public static String formatUnits(long millis) {
        if (millis < 0) {
            return "negative";
        }

        String result = "";

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        if (days > 1) {
            result += days + " days ";
        } else if (days > 0) {
            result += days + " day ";
        }

        long remainingMillis = millis - TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(remainingMillis);
        if (hours > 1) {
            result += hours + " hours ";
        } else if (hours > 0) {
            result += hours + " hour ";
        }

        remainingMillis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMillis);
        if (minutes > 1) {
            result += minutes + " minutes ";
        } else if (minutes > 0) {
            result += minutes + " minute ";
        }

        remainingMillis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(remainingMillis);
        if (seconds > 1) {
            result += seconds + " seconds ";
        } else if (seconds > 0) {
            result += seconds + " second ";
        }

        remainingMillis -= TimeUnit.SECONDS.toMillis(seconds);
        if (remainingMillis > 1) {
            result += remainingMillis + " milliseconds ";
        } else if (remainingMillis > 0) {
            result += remainingMillis + " millisecond ";
        }

        return result.trim();
    }

    public static String join(String[] items, String glue, String lastGlue) {
        if (items.length == 0) {
            return StringUtils.EMPTY;
        } else if (items.length == 1) {
            return items[0];
        } else {
            return StringUtils.join(Arrays.copyOf(items, items.length - 1), glue)
                    + lastGlue + items[items.length - 1];
        }
    }

    public static String summarize(String content) {
        return StringUtils.abbreviate(StringUtils.defaultString(content, "null"), 32)
                .replace("\n", "\\");
    }
}
