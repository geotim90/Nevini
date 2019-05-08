package de.nevini.util;

import java.util.function.Consumer;

public class Functions {

    public static <T> Consumer<T> ignore() {
        return e -> {
        };
    }

}
