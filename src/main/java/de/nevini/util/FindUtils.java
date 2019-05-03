package de.nevini.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

public class FindUtils {

    public static <T> Collection<T> find(T[] value, Function<T, String> identifier, String query) {
        return find(Arrays.asList(value), identifier, query);
    }

    public static <T> Collection<T> find(Collection<T> values, Function<T, String> identifier, String query) {
        ArrayList<T> exact = new ArrayList<>();
        ArrayList<T> wrongCase = new ArrayList<>();
        ArrayList<T> startsWith = new ArrayList<>();
        ArrayList<T> contains = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (T e : values) {
            String s = identifier.apply(e);
            if (s.equals(query)) {
                exact.add(e);
            } else if (exact.isEmpty() && s.equalsIgnoreCase(query)) {
                wrongCase.add(e);
            } else if (wrongCase.isEmpty() && s.toLowerCase().startsWith(lowerQuery)) {
                startsWith.add(e);
            } else if (startsWith.isEmpty() && s.toLowerCase().contains(lowerQuery)) {
                contains.add(e);
            }
        }
        if (!exact.isEmpty()) {
            return exact;
        } else if (!wrongCase.isEmpty()) {
            return wrongCase;
        } else if (!startsWith.isEmpty()) {
            return startsWith;
        } else {
            return contains;
        }
    }

}
