package de.nevini.modules.guild.feed.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedId implements Serializable {

    private Long guild;

    private String type;

    private Long id;

}
