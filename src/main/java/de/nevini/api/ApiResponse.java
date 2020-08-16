package de.nevini.api;

import lombok.NonNull;
import lombok.Value;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

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
    T result;

    /**
     * The {@link Throwable} if one was caught.
     * Will be {@code null} if not {@link #isError()} or no {@link Throwable} was caught.
     */
    Throwable throwable;

    /**
     * Returns {@code true} if the request was rate limited and thus not processed.
     * Returns {@code false} if {@link #isOk()} or {@link #isError()}.
     */
    boolean rateLimited;

    /**
     * Returns {@code true} if the API call was successful.
     * Returns {@code false} if {@link #isRateLimited()} or {@link #isError()}.
     */
    public boolean isOk() {
        return throwable == null && !rateLimited;
    }

    /**
     * Returns {@code true} if {@link #getResult()} is {@code null} or is an empty {@link String} or {@link Collection}.
     * Returns {@code false} if {@link #getResult()} is not {@code null} or is a non-empty {@link String} or {@link Collection}.
     */
    public boolean isEmpty() {
        return result == null
                || (result instanceof String && ((String) result).isEmpty())
                || (result instanceof Collection && ((Collection) result).isEmpty());
    }

    /**
     * Returns {@code true} if the API call failed.
     * Returns {@code false} if {@link #isOk()} or {@link #isRateLimited()}.
     */
    public boolean isError() {
        return throwable != null && !rateLimited;
    }

    /**
     * Returns a copy of {@code this} {@link ApiResponse} with a mapped result.
     * Note that the {@code mapper} will not be called if {@link #isEmpty()}.
     *
     * @param mapper the {@link Function} to map {@link #getResult()} with
     * @param <V>    the target result type
     */
    public <V> @NonNull ApiResponse<V> map(@NonNull Function<T, V> mapper) {
        if (isEmpty()) {
            return new ApiResponse<>(null, throwable, rateLimited);
        } else {
            return new ApiResponse<>(mapper.apply(result), throwable, rateLimited);
        }
    }

    /**
     * Returns {@code this} {@link ApiResponse} if {@link #isOk()} and not {@link #isEmpty()}.
     * Returns the provided alternative if {@code this} {@link ApiResponse} not {@link #isOk()} or {@link #isEmpty()}.
     *
     * @param alternative the {@link ApiResponse} to return in the latter case
     */
    public @NonNull ApiResponse<T> orElse(@NonNull ApiResponse<T> alternative) {
        return isOk() && !isEmpty() ? this : alternative;
    }

    /**
     * Returns {@code this} {@link ApiResponse} if {@link #isOk()} and not {@link #isEmpty()}.
     * Returns the provided alternative if {@code this} {@link ApiResponse} not {@link #isOk()} or {@link #isEmpty()}.
     *
     * @param alternative the {@link Supplier} to call in the latter case
     */
    public @NonNull ApiResponse<T> orElse(@NonNull Supplier<ApiResponse<T>> alternative) {
        return isOk() && !isEmpty() ? this : alternative.get();
    }

    /**
     * Returns {@code this} {@link ApiResponse} if {@link #isOk()} and not {@link #isEmpty()}.
     * Returns the provided alternative if {@code this} {@link ApiResponse} not {@link #isOk()} or {@link #isEmpty()}.
     *
     * @param alternative the {@link Function} to call with {@code this} {@link ApiResponse} in the latter case
     */
    public @NonNull ApiResponse<T> orElse(@NonNull Function<ApiResponse<T>, ApiResponse<T>> alternative) {
        return isOk() && !isEmpty() ? this : alternative.apply(this);
    }

}
