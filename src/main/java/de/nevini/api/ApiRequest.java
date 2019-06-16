package de.nevini.api;

import lombok.NonNull;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Interface for API requests that will produce an {@link ApiResponse} with the same type parameter.
 *
 * @param <T> the type for {@link ApiResponse#getResult()}
 */
public interface ApiRequest<T> {

    /**
     * Returns the endpoint URL for this {@link ApiRequest}.
     */
    @NonNull String getEndpoint();

    /**
     * Returns the {@link RequestBody} for this {@link ApiRequest}.
     *
     * @param builder a prepared {@link MultipartBody.Builder}
     */
    default @NonNull RequestBody getRequestBody(@NonNull MultipartBody.Builder builder) {
        return builder.build();
    }

    /**
     * Returns the {@link Request} for this {@link ApiRequest}.
     *
     * @param builder a prepared {@link Request.Builder}
     */
    default @NonNull Request getRequest(@NonNull Request.Builder builder) {
        return builder.build();
    }

    /**
     * Parses the {@link Response}.
     *
     * @param response the {@link Response} to parse
     * @return the result as an {@link ApiResponse}
     */
    @NonNull ApiResponse<T> parseResponse(@NonNull Response response);

}
