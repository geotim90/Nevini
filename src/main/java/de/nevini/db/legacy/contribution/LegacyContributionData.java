package de.nevini.db.legacy.contribution;

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
@IdClass(LegacyContributionId.class)
@Table(name = "legacy_contribution")
public class LegacyContributionData {

    @Id
    private Long guild;

    @Id
    private Long user;

    private Byte flag;

}
