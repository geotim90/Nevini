package de.nevini.modules.guild.activity.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityId implements Serializable {

    private Long user;

    private Byte type;

    private Long id;

    private Long source;

}
