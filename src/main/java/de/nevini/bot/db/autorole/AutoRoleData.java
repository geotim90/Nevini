package de.nevini.bot.db.autorole;

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
@IdClass(AutoRoleId.class)
@Table(name = "auto_role")
public class AutoRoleData {

    @Id
    private Long guild;

    @Id
    private String type;

    @Id
    private Long id;

    private Long role;

}
