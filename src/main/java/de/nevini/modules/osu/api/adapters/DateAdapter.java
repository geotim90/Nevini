package de.nevini.modules.osu.api.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateAdapter extends TypeAdapter<Date> {

    private static final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);

    public static String convertDateToString(Date date) {
        return date == null ? null : dateFormatter.format(date.toInstant().atZone(ZoneOffset.UTC));
    }

    private static Date convertStringToDate(String string) {
        return StringUtils.isEmpty(string) ? null : Date.from(ZonedDateTime.parse(string, dateFormatter).toInstant());
    }

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        out.value(convertDateToString(value));
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            return convertStringToDate(in.nextString());
        }
    }

}
