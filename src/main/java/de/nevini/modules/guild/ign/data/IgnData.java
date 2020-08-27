package de.nevini.modules.guild.ign.data;

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
@IdClass(IgnId.class)
@Table(name = "ign")
public class IgnData {

    @Id
    private Long guild;

    @Id
    private Long user;

    @Id
    private Long game;

    private String name;

}
