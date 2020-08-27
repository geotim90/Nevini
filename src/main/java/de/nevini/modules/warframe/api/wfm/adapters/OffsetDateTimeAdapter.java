package de.nevini.modules.warframe.api.wfm.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.OffsetDateTime;

public class OffsetDateTimeAdapter extends TypeAdapter<OffsetDateTime> {

    @Override
    public void write(JsonWriter out, OffsetDateTime value) throws IOException {
        out.value(value == null ? null : value.toString());
    }

    @Override
    public OffsetDateTime read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            return OffsetDateTime.parse(in.nextString());
        }
    }

}
