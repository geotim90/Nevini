package de.nevini.db.legacy.activity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegacyActivityId implements Serializable {

    private Long guild;

    private Long user;

    private Byte type;

    private Long id;

}
