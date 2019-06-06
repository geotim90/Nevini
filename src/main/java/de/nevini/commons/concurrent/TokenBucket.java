package de.nevini.commons.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * A thread-safe implementation of a token bucket with an average rate (at which tokens are generated) and a burst size
 * (the limit to the number of tokens the bucket can hold). This can be useful for interacting with rate limited APIs.
 * However, note that the time interval of the token bucket and that of the API will most likely be offset. This means
 * that you may end up sending {@code averageRate + burstSize} requests within one time unit. Take this into account
 * when choosing the values for these parameters.
 */
public class TokenBucket {

    private final int averageRate;
    private final int burstSize;
    private final TimeUnit timeUnit;

    private int tokens;
    private long uts;

    /**
     * Creates a new (empty) token bucket with the provided parameters.
     *
     * @param averageRate the average rate (at which tokens are generated)
     * @param burstSize   the burst size (the limit to the number of tokens the bucket can hold)
     * @param timeUnit    the time unit for the first two arguments
     * @throws IllegalArgumentException if {@code averageRate} or {@code burstSize} is zero or less
     * @throws NullPointerException     if {@code timeUnit} is {@code null}
     */
    public TokenBucket(int averageRate, int burstSize, TimeUnit timeUnit) {
        this(0, averageRate, burstSize, timeUnit);
    }

    /**
     * Creates a new token bucket with the provided parameters.
     *
     * @param initialTokens the number of tokens to start with (cannot be higher than {@code burstSize})
     * @param averageRate   the average rate (at which tokens are generated)
     * @param burstSize     the burst size (the limit to the number of tokens the bucket can hold)
     * @param timeUnit      the time unit for the first two arguments
     * @throws IllegalArgumentException if {@code initialTokens} is less than zero or
     *                                  if {@code averageRate} or {@code burstSize} is zero or less
     * @throws NullPointerException     if {@code timeUnit} is {@code null}
     */
    public TokenBucket(int initialTokens, int averageRate, int burstSize, TimeUnit timeUnit) {
        // check arguments
        if (initialTokens < 0 || averageRate <= 0 || burstSize <= 0) {
            throw new IllegalArgumentException("averageRate and burstSize must be greater than zero!");
        } else if (timeUnit == null) {
            throw new NullPointerException("timeUnit must not be null!");
        } else {
            // initialise fields
            this.averageRate = averageRate;
            this.burstSize = burstSize;
            this.timeUnit = timeUnit;
            this.tokens = Math.min(burstSize, initialTokens);
            this.uts = System.currentTimeMillis();
        }
    }

    /**
     * Checks if there is a token available and consumes it.
     *
     * @return {@code true} if a token was available and consumed; {@code false} otherwise
     */
    public synchronized boolean requestToken() {
        updateTokens(1);
        // check if there is a token available
        if (tokens > 0) {
            // consume a token
            tokens--;
            return true;
        } else {
            // not enough tokens
            return false;
        }
    }

    /**
     * Checks if there are {@code n} tokens available and consumes them.
     *
     * @param n the requested number of tokens
     * @return {@code true} if {@code n} tokens were available and consumed; {@code false} otherwise
     * @throws IllegalArgumentException if {@code n} is zero or less
     */
    public synchronized boolean requestTokens(int n) {
        // check argument
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than zero!");
        } else {
            updateTokens(n);
            // check if there are n tokens available
            if (tokens >= n) {
                // consume n tokens
                tokens -= n;
                return true;
            } else {
                // not enough tokens
                return false;
            }
        }
    }

    /**
     * Updates the number of tokens if there are less than {@code n} tokens left in the bucket.
     *
     * @param n the threshold for the number of tokens
     */
    private synchronized void updateTokens(int n) {
        // only update tokens if there are not enough tokens available
        if (tokens < n) {
            long now = System.currentTimeMillis();
            // calculate the number of full time units since uts
            long delta = timeUnit.convert(now - uts, TimeUnit.MILLISECONDS);
            // increase the number of tokens, but not above the burst size (the maximum capacity)
            tokens = (int) Math.min(burstSize, tokens + delta * averageRate);
            // update timestamp
            uts = now;
        }
    }

}
