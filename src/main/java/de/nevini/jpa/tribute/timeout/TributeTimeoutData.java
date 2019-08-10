package de.nevini.jpa.tribute.timeout;

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
@Table(name = "tribute_timeout")
public class TributeTimeoutData {

    @Id
    private Long guild;

    private Long timeout;

}
