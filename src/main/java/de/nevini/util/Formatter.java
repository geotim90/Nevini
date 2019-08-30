package de.nevini.util;

import de.nevini.api.osu.model.OsuBeatmap;
import de.nevini.api.osu.model.OsuRank;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formatter {

    public static String formatDouble(double value) {
        return new DecimalFormat("#0.##", DecimalFormatSymbols.getInstance(Locale.US)).format(value);
    }

    public static String formatFloat(float value) {
        return new DecimalFormat("#0.##", DecimalFormatSymbols.getInstance(Locale.US)).format(value);
    }

    public static String formatInteger(int value) {
        return NumberFormat.getIntegerInstance(Locale.US).format(value);
    }

    public static String formatLargestUnitAgo(long epochMilli) {
        if (epochMilli <= 0) {
            return "unknown";
        } else if (System.currentTimeMillis() < epochMilli) {
            return "some time in the future";
        } else {
            return formatLargestUnitBetween(OffsetDateTime.now(),
                    OffsetDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneOffset.UTC)) + " ago";
        }
    }

    public static String formatLargestUnitBetween(@NonNull OffsetDateTime earlier, @NonNull OffsetDateTime later) {
        if (earlier.isAfter(later)) {
            return formatLargestUnitBetween(later, earlier);
        }

        long years = earlier.until(later, ChronoUnit.YEARS);
        if (years > 1) {
            return years + " years";
        } else if (years > 0) {
            return years + " year";
        }

        long months = earlier.until(later, ChronoUnit.MONTHS);
        if (months > 1) {
            return months + " months";
        } else if (months > 0) {
            return months + " month";
        }

        long weeks = earlier.until(later, ChronoUnit.WEEKS);
        if (weeks > 1) {
            return weeks + " weeks";
        } else if (weeks > 0) {
            return weeks + " week";
        }

        long days = earlier.until(later, ChronoUnit.DAYS);
        if (days > 1) {
            return days + " days";
        } else if (days > 0) {
            return days + " day";
        }

        long hours = earlier.until(later, ChronoUnit.HOURS);
        if (hours > 1) {
            return hours + " hours";
        } else if (hours > 0) {
            return hours + " hour";
        }

        long minutes = earlier.until(later, ChronoUnit.MINUTES);
        if (minutes > 1) {
            return minutes + " minutes";
        } else if (minutes > 0) {
            return minutes + " minute";
        } else {
            return "less than a minute";
        }
    }

    public static String formatLong(long value) {
        return NumberFormat.getIntegerInstance(Locale.US).format(value);
    }

    public static String formatOsuBeatmap(@NonNull OsuBeatmap beatmap) {
        return beatmap.getArtist() + " - " + beatmap.getTitle()
                + " [" + beatmap.getVersion() + "] (" + beatmap.getMode().getName() + ")";
    }

    public static String formatOsuDisplayHtml(@NonNull String value) {
        String markdown = value.replaceAll("<img src='/images/(\\w+)_small.png'/>", "**$1**") // resolve rank images
                .replaceAll("<b><a href='(/u(?:sers)?/\\d+)'>([^<]+)</a></b>", "$2") // resolve user references
                .replaceAll("<a href='(/[bs]/\\d+(?:\\?m=\\d)?)'>([^<]+)</a>", "$2") // resolve beatmap references
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

    public static String formatOsuRank(OsuRank rank) {
        return rank == null ? "" : formatOsuRank(rank.getId());
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
            return String.format("0:00:%02d", seconds);
        }

        int minutes = seconds / 60;
        if (minutes < 60) {
            return String.format("0:%02d:%02d", minutes, seconds % 60);
        }

        int hours = minutes / 60;
        return String.format("%d:%02d:%02d", hours, minutes % 60, seconds % 60);
    }

    public static String formatTimestamp(long epochMilli) {
        return formatTimestamp(ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneOffset.UTC));
    }

    public static String formatTimestamp(@NonNull TemporalAccessor value) {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneOffset.UTC).format(value)
                .replace('T', ' ') + " UTC";
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

    public static String join(@NonNull String[] items, @NonNull String glue, @NonNull String lastGlue) {
        if (items.length == 0) {
            return StringUtils.EMPTY;
        } else if (items.length == 1) {
            return items[0];
        } else {
            return StringUtils.join(Arrays.copyOf(items, items.length - 1), glue)
                    + lastGlue + items[items.length - 1];
        }
    }

    public static @NonNull OffsetDateTime parseTimestamp(@NonNull String timestamp) {
        if ("now".equalsIgnoreCase(timestamp)) {
            return OffsetDateTime.now(ZoneOffset.UTC);
        } else {
            String padding = "0000-01-01T00:00:00";
            String padded = timestamp + padding.substring(Math.min(padding.length(), timestamp.length()));
            return LocalDateTime.parse(padded).atOffset(ZoneOffset.UTC);
        }
    }

    public static String summarize(String content) {
        return StringUtils.abbreviate(StringUtils.defaultString(content, "null"), 32)
                .replace("\n", "\\");
    }

}
