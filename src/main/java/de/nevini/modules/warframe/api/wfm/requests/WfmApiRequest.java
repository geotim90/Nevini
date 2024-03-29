package de.nevini.modules.warframe.api.wfm.requests;

import de.nevini.core.api.ApiResponse;
import lombok.NonNull;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interface for API requests that will produce an {@link ApiResponse} with the same type parameter.
 *
 * @param <T> the type for {@link ApiResponse#getResult()}
 */
public interface WfmApiRequest<T> {

    /**
     * Returns the endpoint URL for this {@link WfmApiRequest}.
     */
    @NonNull String getEndpoint();

    /**
     * Returns the {@link Request} for this {@link WfmApiRequest}.
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
