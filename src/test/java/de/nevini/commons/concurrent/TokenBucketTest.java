package de.nevini.commons.concurrent;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TokenBucketTest {

    /**
     * Blocks for 10ms unless an {@link InterruptedException} is thrown.
     */
    private static void pause() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException ignore) {
        }
    }

    @Test
    public void testEmptyBucket() {
        // use a slow token bucket that will not have any tokens for the duration of the test
        TokenBucket tokenBucket = new TokenBucket(1, 1, TimeUnit.DAYS);
        // negative test cases (below or at capacity)
        pause();
        Assert.assertFalse(tokenBucket.requestToken());
        pause();
        Assert.assertFalse(tokenBucket.requestTokens(1));
        pause();
        Assert.assertFalse(tokenBucket.requestTokens(10));
        pause();
        Assert.assertFalse(tokenBucket.requestTokens(100));
        pause();
        Assert.assertFalse(tokenBucket.requestTokens(1000));
        // negative test cases (above capacity)
        pause();
        Assert.assertFalse(tokenBucket.requestTokens(1001));
        pause();
        Assert.assertFalse(tokenBucket.requestTokens(Integer.MAX_VALUE));
    }

    @Test
    public void testFullBucket() {
        // use a fast token bucket that will be full for each step of the test
        TokenBucket tokenBucket = new TokenBucket(1000, 1000, TimeUnit.MILLISECONDS);
        // positive test cases (below or at capacity)
        pause();
        Assert.assertTrue(tokenBucket.requestToken());
        pause();
        Assert.assertTrue(tokenBucket.requestTokens(1));
        pause();
        Assert.assertTrue(tokenBucket.requestTokens(10));
        pause();
        Assert.assertTrue(tokenBucket.requestTokens(100));
        pause();
        Assert.assertTrue(tokenBucket.requestTokens(1000));
        // negative test cases (above capacity)
        pause();
        Assert.assertFalse(tokenBucket.requestTokens(1001));
        pause();
        Assert.assertFalse(tokenBucket.requestTokens(Integer.MAX_VALUE));
    }

    @Test
    public void testPartiallyFullBucket() {
        // use a token bucket that will have tokens for each step of the test but will not fill to capacity
        TokenBucket tokenBucket = new TokenBucket(1, 1000, TimeUnit.MILLISECONDS);
        // positive test cases (below average rate for 10ms)
        pause();
        Assert.assertTrue(tokenBucket.requestToken());
        pause();
        Assert.assertTrue(tokenBucket.requestTokens(1));
        pause();
        Assert.assertTrue(tokenBucket.requestTokens(10));
        // negative test cases (above average rate for 10ms but below or at capacity)
        pause();
        Assert.assertFalse(tokenBucket.requestTokens(100));
        pause();
        Assert.assertFalse(tokenBucket.requestTokens(1000));
        // negative test cases (above capacity)
        pause();
        Assert.assertFalse(tokenBucket.requestTokens(1001));
        pause();
        Assert.assertFalse(tokenBucket.requestTokens(Integer.MAX_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestTokensZero() {
        new TokenBucket(1, 1, TimeUnit.SECONDS).requestTokens(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestTokensNegativeOne() {
        new TokenBucket(1, 1, TimeUnit.SECONDS).requestTokens(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestTokensNegativeMinValue() {
        new TokenBucket(1, 1, TimeUnit.SECONDS).requestTokens(Integer.MIN_VALUE);
    }

    @Test
    public void testConstructorAverageRate() {
        new TokenBucket(1, 1, TimeUnit.SECONDS);
        new TokenBucket(10, 1, TimeUnit.SECONDS);
        new TokenBucket(100, 1, TimeUnit.SECONDS);
        new TokenBucket(1000, 1, TimeUnit.SECONDS);
        new TokenBucket(1001, 1, TimeUnit.SECONDS);
        new TokenBucket(Integer.MAX_VALUE, 1, TimeUnit.SECONDS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorAverageRateZero() {
        new TokenBucket(0, 1, TimeUnit.SECONDS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorAverageRateNegativeOne() {
        new TokenBucket(-1, 1, TimeUnit.SECONDS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorAverageRateNegativeMinValue() {
        new TokenBucket(Integer.MIN_VALUE, 1, TimeUnit.SECONDS);
    }

    @Test
    public void testConstructorBurstSize() {
        new TokenBucket(1, 1, TimeUnit.SECONDS);
        new TokenBucket(1, 10, TimeUnit.SECONDS);
        new TokenBucket(1, 100, TimeUnit.SECONDS);
        new TokenBucket(1, 1000, TimeUnit.SECONDS);
        new TokenBucket(1, 1001, TimeUnit.SECONDS);
        new TokenBucket(1, Integer.MAX_VALUE, TimeUnit.SECONDS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorBurstSizeZero() {
        new TokenBucket(1, 0, TimeUnit.SECONDS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorBurstSizeNegativeOne() {
        new TokenBucket(1, -1, TimeUnit.SECONDS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorBurstSizeNegativeMinValue() {
        new TokenBucket(1, Integer.MIN_VALUE, TimeUnit.SECONDS);
    }

    @Test
    public void testConstructorTimeUnit() {
        new TokenBucket(1, 1, TimeUnit.NANOSECONDS);
        new TokenBucket(1, 1, TimeUnit.MICROSECONDS);
        new TokenBucket(1, 1, TimeUnit.MILLISECONDS);
        new TokenBucket(1, 1, TimeUnit.SECONDS);
        new TokenBucket(1, 1, TimeUnit.MINUTES);
        new TokenBucket(1, 1, TimeUnit.HOURS);
        new TokenBucket(1, 1, TimeUnit.DAYS);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorTimeUnitNull() {
        new TokenBucket(1, 1, null);
    }

}
