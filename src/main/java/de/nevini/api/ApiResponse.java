package de.nevini.api;

import lombok.Value;

/**
 * Wrapper for the result of an API call.
 *
 * @param <T> the type for {@link #getResult()}
 */
@Value
public class ApiResponse<T> {

    /**
     * Creates a new {@link ApiResponse} for a successful API call with no result.
     *
     * @param <R> the type for {@link #getResult()}
     */
    public static <R> ApiResponse<R> empty() {
        return new ApiResponse<>(null, null, false);
    }

    /**
     * Creates a new {@link ApiResponse} for a successful API call with the provided {@code result}.
     *
     * @param result the result of the API call
     * @param <R>    the type for {@link #getResult()}
     */
    public static <R> ApiResponse<R> ok(R result) {
        return new ApiResponse<>(result, null, false);
    }

    /**
     * Creates a new {@link ApiResponse} for a failed API call with the provided {@link Throwable}.
     *
     * @param throwable the result of the API call
     * @param <R>       the type for {@link #getResult()}
     */
    public static <R> ApiResponse<R> error(Throwable throwable) {
        return new ApiResponse<>(null, throwable, false);
    }

    /**
     * Creates a new {@link ApiResponse} for a rate limited API call.
     *
     * @param <R> the type for {@link #getResult()}
     */
    public static <R> ApiResponse<R> rateLimited() {
        return new ApiResponse<>(null, null, true);
    }

    /**
     * The result of the API call.
     * Will be {@code null} if not {@link #isOk()}, although {@code null} can still be a valid result.
     */
    private final T result;

    /**
     * The {@link Throwable} if once was caught.
     * Will be {@code null} unless {@link #isError()}.
     */
    private final Throwable throwable;

    /**
     * Returns {@code true} if the request was rate limited and thus not processed.
     * Returns {@code false} if {@link #isOk()} or {@link #isError()}.
     */
    private final boolean rateLimited;

    /**
     * Returns {@code true} if the API call was successful.
     * Returns {@code false} if {@link #isRateLimited()} or {@link #isError()}.
     */
    public boolean isOk() {
        return throwable == null && !rateLimited;
    }

    /**
     * Returns {@code true} if the API call failed.
     * Returns {@code false} if {@link #isOk()} or {@link #isRateLimited()}.
     */
    public boolean isError() {
        return throwable != null && !rateLimited;
    }

}
