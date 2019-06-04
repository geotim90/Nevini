package de.nevini.bot.db.permission;

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
@IdClass(PermissionId.class)
@Table(name = "permission")
public class PermissionData {

    @Id
    private Long guild;

    @Id
    private Long channel;

    @Id
    private Byte type;

    @Id
    private Long id;

    @Id
    private String node;

    private Byte flag;

}
