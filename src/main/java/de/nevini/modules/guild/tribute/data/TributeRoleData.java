package de.nevini.modules.guild.tribute.data;

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
@Table(name = "tribute_role")
public class TributeRoleData {

    @Id
    private Long guild;

    private Long role;

}
