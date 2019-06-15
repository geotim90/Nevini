package de.nevini.bot.data.osu;

import java.util.Date;

public class OsuMapperUtils {

    public static Date convertDate(Long value) {
        return value == null ? null : new Date(value);
    }

    public static Float convertFloat(Double value) {
        return value == null ? null : value.floatValue();
    }

    public static Long convertDate(Date value) {
        return value == null ? null : value.getTime();
    }

    public static Double convertFloat(Float value) {
        return value == null ? null : value.doubleValue();
    }

}
