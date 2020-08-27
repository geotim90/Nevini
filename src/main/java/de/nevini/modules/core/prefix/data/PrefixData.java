package de.nevini.modules.core.prefix.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "prefix")
public class PrefixData {

    @Id
    private Long guild;

    private String prefix;

}
