package de.nevini.bot.db.inactivity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InactivityId implements Serializable {

    private Long guild;

    private Byte type;

    private Long id;

}
