package de.nevini.db.legacy.timeout;

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
@IdClass(LegacyTimeoutId.class)
@Table(name = "legacy_timeout")
public class LegacyTimeoutData {

    @Id
    private Long guild;

    @Id
    private Byte type;

    @Id
    private Long id;

    private Long value;

}
