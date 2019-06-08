package de.nevini.commons.util;

import org.junit.Test;

public class FunctionsTest {

    @Test
    public void testIgnore() {
        // just make sure no exceptions are thrown
        Functions.ignore().accept(null);
        Functions.ignore().accept(0);
        Functions.ignore().accept("");
        Functions.ignore().accept(new Object());
        Functions.ignore().accept(new Object[0]);
    }

}
