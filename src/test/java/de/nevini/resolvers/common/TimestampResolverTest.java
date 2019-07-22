package de.nevini.resolvers.common;

import org.junit.Assert;
import org.junit.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class TimestampResolverTest {

    @Test
    public void testDate() {
        Assert.assertEquals(
                OffsetDateTime.of(2019, 7, 5, 0, 0, 0, 0, ZoneOffset.UTC),
                Resolvers.TIMESTAMP.findSorted(null, "2019-07-05").get(0)
        );
    }

    @Test
    public void testDateTime() {
        Assert.assertEquals(
                OffsetDateTime.of(2019, 7, 5, 23, 59, 59, 0, ZoneOffset.UTC),
                Resolvers.TIMESTAMP.findSorted(null, "2019-07-05T23:59:59").get(0)
        );
    }

    @Test
    public void testDateTimeMillis() {
        Assert.assertEquals(
                OffsetDateTime.of(2019, 7, 5, 23, 59, 59, 999000000, ZoneOffset.UTC),
                Resolvers.TIMESTAMP.findSorted(null, "2019-07-05T23:59:59.999").get(0)
        );
    }

    @Test
    public void testNow() {
        OffsetDateTime before = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime timestamp = Resolvers.TIMESTAMP.findSorted(null, "now").get(0);
        OffsetDateTime after = OffsetDateTime.now(ZoneOffset.UTC);
        Assert.assertFalse(before.isAfter(timestamp));
        Assert.assertFalse(after.isBefore(timestamp));
    }

    @Test
    public void test5DaysAgo() {
        OffsetDateTime before = OffsetDateTime.now(ZoneOffset.UTC).minusDays(5);
        OffsetDateTime timestamp = Resolvers.TIMESTAMP.findSorted(null, "5 days ago").get(0);
        OffsetDateTime after = OffsetDateTime.now(ZoneOffset.UTC).minusDays(5);
        Assert.assertFalse(before.isAfter(timestamp));
        Assert.assertFalse(after.isBefore(timestamp));
    }

    @Test
    public void testMinus24H() {
        OffsetDateTime before = OffsetDateTime.now(ZoneOffset.UTC).minusHours(24);
        OffsetDateTime timestamp = Resolvers.TIMESTAMP.findSorted(null, "-24h").get(0);
        OffsetDateTime after = OffsetDateTime.now(ZoneOffset.UTC).minusHours(24);
        Assert.assertFalse(before.isAfter(timestamp));
        Assert.assertFalse(after.isBefore(timestamp));
    }

}
