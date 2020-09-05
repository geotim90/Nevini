package de.nevini.modules.guild.inactivity.data;

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
@IdClass(InactivityId.class)
@Table(name = "inactivity")
public class InactivityData {

    @Id
    private Long guild;

    @Id
    private Byte type;

    @Id
    private Long id;

    private Integer days;

}
