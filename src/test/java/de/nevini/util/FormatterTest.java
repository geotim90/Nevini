package de.nevini.util;

import org.junit.Assert;
import org.junit.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class FormatterTest {

    @Test
    public void formatLargeInteger() {
        Assert.assertEquals("1234", Formatter.formatLargeInteger(1234));
        Assert.assertEquals("12k", Formatter.formatLargeInteger(12345));
        Assert.assertEquals("123m", Formatter.formatLargeInteger(123456789));
        // issue #94
        Assert.assertEquals("1m", Formatter.formatLargeInteger(1000000));
        Assert.assertEquals("1b", Formatter.formatLargeInteger(1000000000));
        Assert.assertEquals("1k", Formatter.formatLargeInteger(1000));
        // issue #98
        Assert.assertEquals("0", Formatter.formatLargeInteger(0));
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

    @Test
    public void formatShortUnitsBetween() {
        OffsetDateTime earlier = OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime later = OffsetDateTime.of(2000, 1, 1, 1, 14, 33, 0, ZoneOffset.UTC);
        Assert.assertEquals("1h 14m", Formatter.formatShortUnitsBetween(earlier, later));
    }

}
