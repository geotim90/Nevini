package de.nevini.api.wfw.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class UnexpandedArticle {

    @SerializedName("title")
    String title;

    @SerializedName("url")
    String url;

}
