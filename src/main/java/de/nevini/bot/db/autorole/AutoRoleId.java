package de.nevini.bot.db.autorole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutoRoleId implements Serializable {

    private Long guild;

    private String type;

    private Long id;

}
