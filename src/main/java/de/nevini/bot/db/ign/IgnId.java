package de.nevini.bot.db.ign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IgnId implements Serializable {

    private Long guild;

    private Long user;

    private Long game;

}
