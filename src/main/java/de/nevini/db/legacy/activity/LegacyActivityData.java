package de.nevini.db.legacy.activity;

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
@IdClass(LegacyActivityId.class)
@Table(name = "legacy_activity")
public class LegacyActivityData {

    @Id
    private Long guild;

    @Id
    private Long user;

    @Id
    private Byte type;

    @Id
    private Long id;

    private Long uts;

}
