package de.nevini.jpa.feed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(FeedId.class)
@Table(name = "feed")
public class FeedData {

    @Id
    private Long guild;

    @Id
    private String type;

    @Id
    private Long id;

    private Long channel;

    private Long uts;

}
