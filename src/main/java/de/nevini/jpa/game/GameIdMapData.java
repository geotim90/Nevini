package de.nevini.jpa.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "game_id_map")
public class GameIdMapData {

    @Id
    private Long id;

    private String name;

    private String icon;

    private Boolean multi;

}
