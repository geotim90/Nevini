package de.nevini.modules.admin.game.data;

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
@Table(name = "game_name_map")
public class GameNameMapData {

    @Id
    private String name;

    private Long id;

}
