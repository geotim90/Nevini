package de.nevini.db.legacy.contribution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegacyContributionId implements Serializable {

    private Long guild;

    private Long user;

}
