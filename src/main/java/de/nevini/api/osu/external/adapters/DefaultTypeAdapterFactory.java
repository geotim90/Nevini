package de.nevini.api.osu.external.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

// see https://stackoverflow.com/questions/10596667/how-to-invoke-default-deserialize-with-gson

/**
 * Interprets {@code 0} as {@code null} in case of errors.
 */
public class DefaultTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        return new TypeAdapter<T>() {

            @Override
            public void write(JsonWriter out, T value) throws IOException {
                try {
                    delegate.write(out, value);
                } catch (IOException e) {
                    delegate.write(out, null);
                }
            }

            @Override
            public T read(JsonReader in) throws IOException {
                try {
                    return delegate.read(in);
                } catch (IOException | IllegalStateException | JsonSyntaxException e) {
                    if (e.getMessage().contains("Expected BEGIN_OBJECT but was NUMBER")) {
                        in.skipValue();
                        return null;
                    } else {
                        throw e;
                    }
                }
            }

        };
    }

}
