package de.nevini.modules.warframe.api.wfw.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class UnexpandedListArticleResultSet {

    @SerializedName("items")
    List<UnexpandedArticle> items;

    @SerializedName("basepath")
    String basepath;

}
