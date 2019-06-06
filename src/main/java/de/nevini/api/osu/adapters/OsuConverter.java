package de.nevini.api.osu.adapters;

import de.nevini.api.osu.model.OsuMod;
import org.apache.commons.lang3.StringUtils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class OsuConverter {

    private static final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);

    public static String convertDateToString(Date date) {
        return date == null ? null : dateFormatter.format(date.toInstant().atZone(ZoneOffset.UTC));
    }

    public static Date convertStringToDate(String string) {
        return StringUtils.isEmpty(string) ? null : Date.from(ZonedDateTime.parse(string, dateFormatter).toInstant());
    }

    public static String convertModsToString(OsuMod[] mods) {
        if (mods == null) {
            return null;
        } else {
            int value = 0;
            for (OsuMod mod : mods) {
                value |= mod.getId();
            }
            return Integer.toString(value);
        }
    }
}
