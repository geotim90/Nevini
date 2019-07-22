package de.nevini.util;

import java.util.function.Consumer;

/**
 * Utility class that provides some pre-defined implementations of functional interfaces.
 */
public class Functions {

    /**
     * Returns a {@link Consumer} that accepts anything and does nothing.
     *
     * @param <T> the type parameter for the {@link Consumer}
     * @return a {@link Consumer} that accepts anything and does nothing
     */
    public static <T> Consumer<T> ignore() {
        return e -> {
        };
    }

}
