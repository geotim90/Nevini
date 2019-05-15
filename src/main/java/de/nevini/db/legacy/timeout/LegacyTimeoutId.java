package de.nevini.db.legacy.timeout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegacyTimeoutId implements Serializable {

    private Long guild;

    private Byte type;

    private Long id;

}
