package de.nevini.util;

import java.util.*;
import java.util.function.Function;

public class Finder {

    public static <T> List<T> find(T[] values, Function<T, String> identifier, String query) {
        return find(Arrays.asList(values), identifier, query);
    }

    public static <T> List<T> find(Collection<T> values, Function<T, String> identifier, String query) {
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

    public static <T> List<T> findAny(T[] values, Function<T, String[]> identifier, String query) {
        return findAny(Arrays.asList(values), identifier, query);
    }

    public static <T> List<T> findAny(Collection<T> values, Function<T, String[]> identifier, String query) {
        LinkedHashSet<T> exact = new LinkedHashSet<>();
        LinkedHashSet<T> wrongCase = new LinkedHashSet<>();
        LinkedHashSet<T> startsWith = new LinkedHashSet<>();
        LinkedHashSet<T> contains = new LinkedHashSet<>();
        String lowerQuery = query.toLowerCase();
        for (T e : values) {
            for (String s : identifier.apply(e)) {
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
        }
        if (!exact.isEmpty()) {
            return new ArrayList<>(exact);
        } else if (!wrongCase.isEmpty()) {
            return new ArrayList<>(wrongCase);
        } else if (!startsWith.isEmpty()) {
            return new ArrayList<>(startsWith);
        } else {
            return new ArrayList<>(contains);
        }
    }

}
