package de.nevini.util;

import org.junit.Assert;
import org.junit.Test;

public class FormatterTest {

    @Test
    public void formatLargeInteger() {
        Assert.assertEquals("123m", Formatter.formatLargeInteger(123456789));
    }

    @Test
    public void formatOsuDisplayHtml() {
        // issue #32
        Assert.assertEquals("ABC has once again chosen to support osu! - thanks for your generosity!",
                Formatter.formatOsuDisplayHtml("<b><a href='/users/123456789'>ABC</a></b> has once again chosen to support osu! - thanks for your generosity!"));
        Assert.assertEquals("ABC has submitted a new beatmap \"XYZ - JKL\"",
                Formatter.formatOsuDisplayHtml("ABC has submitted a new beatmap \"<a href='/s/123456'>XYZ - JKL</a>\""));
        Assert.assertEquals("ABC has updated the beatmap \"XYZ - JKL\"",
                Formatter.formatOsuDisplayHtml("ABC has updated the beatmap \"<a href='/s/123456'>XYZ - JKL</a>\""));
        // simple regression
        Assert.assertEquals("<:rankS:581903652831232003> ABC achieved rank #123 on XYZ - JKL [123] (osu!)",
                Formatter.formatOsuDisplayHtml("<img src='/images/S_small.png'/> <b><a href='/u/123456789'>ABC</a></b> achieved rank #123 on <a href='/b/123456?m=0'>XYZ - JKL [123]</a> (osu!)"));
    }

}
