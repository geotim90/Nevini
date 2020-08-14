package de.nevini.util;

import org.simmetrics.StringMetric;
import org.simmetrics.metrics.StringMetrics;

import java.util.*;
import java.util.function.Function;

/**
 * Utility class for finding values in a collection or array by matching {@link String} values to a query
 * {@link String}. Matches are determined as follows:
 * <ol>
 * <li>If any values equal the query exactly (case-sensitive), such matches are returned.</li>
 * <li>If any values equal the query ignoring case, such matches are returned.</li>
 * <li>If any values start with the query (ignoring case), such matches are returned.</li>
 * <li>If any values contain the query (ignoring case), such matches are returned.</li>
 * </ol>
 * The order of values in provided an array or {@link Collection} is preserved as long as there are no duplicate values.
 * No value will be returned twice. In that sense the returned lists are also sets.
 */
@SuppressWarnings({"Duplicates"}) // reason: ridiculous method signature required
public class Finder {

    /**
     * Finds matches within the provided {@link String} array.
     *
     * @param values an array of {@link String} values
     * @param query  the query {@link String}
     * @return a list of matches
     * @throws IllegalArgumentException if {@code query} is empty
     * @throws NullPointerException     if {@code values} or {@code query} is {@code null}
     */
    public static List<String> find(String[] values, String query) {
        return find(values, Function.identity(), query);
    }

    /**
     * Finds matches within the provided {@link String} {@link Collection}.
     *
     * @param values a {@link Collection} of {@link String} values
     * @param query  the query {@link String}
     * @return a list of matches
     * @throws IllegalArgumentException if {@code query} is empty
     * @throws NullPointerException     if {@code values} or {@code query} is {@code null}
     */
    public static List<String> find(Collection<String> values, String query) {
        return find(values, Function.identity(), query);
    }

    /**
     * Finds matches within the provided array of values by checking each {@link String} identifier.
     *
     * @param values     an array of values
     * @param identifier a {@link Function} that returns the {@link String} identifier for a given value
     * @param query      the query {@link String}
     * @param <T>        the value type
     * @return a list of matches
     * @throws IllegalArgumentException if {@code query} is empty
     * @throws NullPointerException     if {@code values}, {@code identifier} or {@code query} is {@code null}
     *                                  or if any value returned by {@code identifier} is {@code null}
     */
    public static <T> List<T> find(T[] values, Function<T, String> identifier, String query) {
        return find(Arrays.asList(values), identifier, query);
    }

    /**
     * Finds matches within the provided {@link Collection} of values by checking each {@link String} identifier.
     *
     * @param values             a {@link Collection} of values
     * @param identifierFunction a {@link Function} that returns the {@link String} identifier for a given value
     * @param query              the query {@link String}
     * @param <T>                the value type
     * @return a list of matches
     * @throws IllegalArgumentException if {@code query} is empty
     * @throws NullPointerException     if {@code values}, {@code identifier} or {@code query} is {@code null}
     *                                  or if any value returned by {@code identifier} is {@code null}
     */
    public static <T> List<T> find(Collection<T> values, Function<T, String> identifierFunction, String query) {
        // check arguments
        if (query.isEmpty()) {
            throw new IllegalArgumentException("query must not be empty!");
        }
        // create lists for matches
        ArrayList<T> matchesEquals = new ArrayList<>();
        ArrayList<T> matchesIgnoreCase = new ArrayList<>();
        ArrayList<T> matchesStartsWith = new ArrayList<>();
        ArrayList<T> matchesContains = new ArrayList<>();
        // get the lower case query for comparisons that ignore case
        String lowerQuery = query.toLowerCase();
        // iterate over all values in order
        for (T e : values) {
            // get the identifier of the current value
            String identifier = identifierFunction.apply(e);
            // check for matches using equals
            if (identifier.equals(query)) {
                matchesEquals.add(e);
            } else if (matchesEquals.isEmpty()) {
                // check for matches using equalsIgnoreCase if no "better" matches were already found
                String lowerIdentifier = identifier.toLowerCase();
                if (lowerIdentifier.equals(lowerQuery)) {
                    matchesIgnoreCase.add(e);
                } else if (matchesIgnoreCase.isEmpty()) {
                    // check for matches using startsWith and contains if no "better" matches were already found
                    if (lowerIdentifier.startsWith(lowerQuery)) {
                        matchesStartsWith.add(e);
                    } else if (matchesStartsWith.isEmpty() && lowerIdentifier.contains(lowerQuery)) {
                        matchesContains.add(e);
                    }
                }
            }
        }
        // return the "best" list of matches
        if (!matchesEquals.isEmpty()) {
            return matchesEquals;
        } else if (!matchesIgnoreCase.isEmpty()) {
            return matchesIgnoreCase;
        } else if (!matchesStartsWith.isEmpty()) {
            return matchesStartsWith;
        } else {
            return matchesContains;
        }
    }

    /**
     * Finds matches within the provided array of values by checking each {@link String} identifier.
     *
     * @param values      an array of values
     * @param identifiers a {@link Function} that returns the {@link String} identifiers for a given value
     * @param query       the query {@link String}
     * @param <T>         the value type
     * @return a list of matches
     * @throws IllegalArgumentException if {@code query} is empty
     * @throws NullPointerException     if {@code values}, {@code identifiers} or {@code query} is {@code null}
     *                                  or if any value returned by {@code identifiers} is or contains {@code null}
     */
    public static <T> List<T> findAny(T[] values, Function<T, String[]> identifiers, String query) {
        return findAny(Arrays.asList(values), identifiers, query);
    }

    /**
     * Finds matches within the provided {@link Collection} of values by checking each {@link String} identifier.
     *
     * @param values             a {@link Collection} of values
     * @param identifierFunction a {@link Function} that returns the {@link String} identifiers for a given value
     * @param query              the query {@link String}
     * @param <T>                the value type
     * @return a list of matches
     * @throws IllegalArgumentException if {@code query} is empty
     * @throws NullPointerException     if {@code values}, {@code identifiers} or {@code query} is {@code null}
     *                                  or if any value returned by {@code identifiers} is or contains {@code null}
     */
    public static <T> List<T> findAny(Collection<T> values, Function<T, String[]> identifierFunction, String query) {
        // check arguments
        if (query.isEmpty()) {
            throw new IllegalArgumentException("query must not be empty!");
        }
        // create insert-ordered sets for matches
        LinkedHashSet<T> matchesEquals = new LinkedHashSet<>();
        LinkedHashSet<T> matchesIgnoreCase = new LinkedHashSet<>();
        LinkedHashSet<T> matchesStartsWith = new LinkedHashSet<>();
        LinkedHashSet<T> matchesContains = new LinkedHashSet<>();
        // get the lower case query for comparisons that ignore case
        String lowerQuery = query.toLowerCase();
        // iterate over all values in order
        for (T e : values) {
            // iterate over the identifiers of the current value
            for (String identifier : identifierFunction.apply(e)) {
                // check for matches using equals
                if (identifier.equals(query)) {
                    matchesEquals.add(e);
                } else if (matchesEquals.isEmpty()) {
                    // check for matches using equalsIgnoreCase if no "better" matches were already found
                    String lowerIdentifier = identifier.toLowerCase();
                    if (lowerIdentifier.equals(lowerQuery)) {
                        matchesIgnoreCase.add(e);
                    } else if (matchesIgnoreCase.isEmpty()) {
                        // check for matches using startsWith and contains if no "better" matches were already found
                        if (lowerIdentifier.startsWith(lowerQuery)) {
                            matchesStartsWith.add(e);
                        } else if (matchesStartsWith.isEmpty() && lowerIdentifier.contains(lowerQuery)) {
                            matchesContains.add(e);
                        }
                    }
                }
            }
        }
        // return the "best" list of matches
        if (!matchesEquals.isEmpty()) {
            return new ArrayList<>(matchesEquals);
        } else if (!matchesIgnoreCase.isEmpty()) {
            return new ArrayList<>(matchesIgnoreCase);
        } else if (!matchesStartsWith.isEmpty()) {
            return new ArrayList<>(matchesStartsWith);
        } else {
            return new ArrayList<>(matchesContains);
        }
    }

    /**
     * Finds matches within the provided array of values by checking each {@link String} identifier.
     * This method will return more matches than {@link #findAny(Object[], Function, String)}.
     *
     * @param values      an array of values
     * @param identifiers a {@link Function} that returns the {@link String} identifiers for a given value
     * @param query       the query {@link String}
     * @param <T>         the value type
     * @return a list of lenient matches
     * @throws IllegalArgumentException if {@code query} is empty
     * @throws NullPointerException     if {@code values}, {@code identifiers} or {@code query} is {@code null}
     *                                  or if any value returned by {@code identifiers} is or contains {@code null}
     */
    public static <T> List<T> findAnyLenient(T[] values, Function<T, String[]> identifiers, String query) {
        return findAnyLenient(Arrays.asList(values), identifiers, query);
    }

    /**
     * Finds matches within the provided {@link Collection} of values by checking each {@link String} identifier.
     * This method will return more matches than {@link #findAny(Object[], Function, String)}.
     *
     * @param values      a {@link Collection} of values
     * @param identifiers a {@link Function} that returns the {@link String} identifiers for a given value
     * @param query       the query {@link String}
     * @param <T>         the value type
     * @return a list of lenient matches
     * @throws IllegalArgumentException if {@code query} is empty
     * @throws NullPointerException     if {@code values}, {@code identifiers} or {@code query} is {@code null}
     *                                  or if any value returned by {@code identifiers} is or contains {@code null}
     */
    public static <T> List<T> findAnyLenient(Collection<T> values, Function<T, String[]> identifiers, String query) {
        // check arguments
        if (query.isEmpty()) {
            throw new IllegalArgumentException("query must not be empty!");
        }
        // create insert-ordered sets for matches
        LinkedHashSet<T> matchesEquals = new LinkedHashSet<>();
        LinkedHashSet<T> matchesIgnoreCase = new LinkedHashSet<>();
        LinkedHashSet<T> matchesStartsWith = new LinkedHashSet<>();
        LinkedHashSet<T> matchesContains = new LinkedHashSet<>();
        LinkedHashSet<T> matchesContainsLenient = new LinkedHashSet<>();
        LinkedHashSet<T> matchesMostSimilar = new LinkedHashSet<>();
        // prepare string metrics
        float mostSimilarScore = 0;
        StringMetric stringMetric = StringMetrics.jaroWinkler();
        // get the lower case query for comparisons that ignore case
        String lowerQuery = query.toLowerCase();
        // iterate over all values in order
        for (T e : values) {
            // iterate over the identifiers of the current value
            for (String identifier : identifiers.apply(e)) {
                // check for matches using equals
                if (identifier.equals(query)) {
                    matchesEquals.add(e);
                } else if (matchesEquals.isEmpty()) {
                    // check for matches using equalsIgnoreCase if no "better" matches were already found
                    String lowerIdentifier = identifier.toLowerCase();
                    if (lowerIdentifier.equals(lowerQuery)) {
                        matchesIgnoreCase.add(e);
                    } else if (matchesIgnoreCase.isEmpty()) {
                        // check for matches using startsWith if no "better" matches were already found
                        if (lowerIdentifier.startsWith(lowerQuery)) {
                            matchesStartsWith.add(e);
                        } else if (matchesStartsWith.isEmpty()) {
                            // check for matches using contains if no "better" matches were already found
                            if (lowerIdentifier.contains(lowerQuery)) {
                                matchesContains.add(e);
                            } else if (matchesContains.isEmpty()) {
                                // check for matches using containsLenient if no "better" matches were already found
                                if (containsLenient(lowerIdentifier, lowerQuery)) {
                                    matchesContainsLenient.add(e);
                                } else if (matchesContainsLenient.isEmpty()) {
                                    // look for most similar matches if no "better" matches were already found
                                    float score = stringMetric.compare(lowerIdentifier, lowerQuery);
                                    if (score > mostSimilarScore) {
                                        matchesMostSimilar.clear();
                                        matchesMostSimilar.add(e);
                                        mostSimilarScore = score;
                                    } else if (score == mostSimilarScore) {
                                        matchesMostSimilar.add(e);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // return the "best" list of matches
        if (!matchesEquals.isEmpty()) {
            return new ArrayList<>(matchesEquals);
        } else if (!matchesIgnoreCase.isEmpty()) {
            return new ArrayList<>(matchesIgnoreCase);
        } else if (!matchesStartsWith.isEmpty()) {
            return new ArrayList<>(matchesStartsWith);
        } else if (!matchesContains.isEmpty()) {
            return new ArrayList<>(matchesContains);
        } else if (!matchesContainsLenient.isEmpty()) {
            return new ArrayList<>(matchesContainsLenient);
        } else {
            return new ArrayList<>(matchesMostSimilar);
        }
    }

    /**
     * Returns {@code true} if all alphanumeric characters of {@code sequence} appear in {@code subject} in the same
     * order but not necessarily adjacent to each other.
     *
     * @see Character#isLetterOrDigit(char)
     */
    private static boolean containsLenient(String subject, String sequence) {
        int subjectIndex = 0;
        int sequenceIndex = 0;
        while (subjectIndex < subject.length() && sequenceIndex < sequence.length()) {
            char c = sequence.charAt(sequenceIndex);
            if (!Character.isLetterOrDigit(c)) {
                sequenceIndex++;
            } else if (subject.charAt(subjectIndex) == c) {
                subjectIndex++;
                sequenceIndex++;
            } else {
                subjectIndex++;
            }
        }
        return subjectIndex > 0 && sequenceIndex >= sequence.length();
    }

}
