package de.nevini.api;

import lombok.NonNull;

/**
 * Implementation of an API that can work with {@link ApiRequest} and {@link ApiResponse}.
 */
public interface Api {

    /**
     * Makes a call to the API that will block this thread until it is completed.
     *
     * @param request the {@link ApiRequest}
     * @param <T>     the type for {@link ApiResponse#getResult()}
     * @return the {@link ApiResponse}
     * @throws NullPointerException if {@code request} is {@code null}
     */
    <T> @NonNull ApiResponse<T> call(@NonNull ApiRequest<T> request);

}
