package de.nevini.modules.core.module.data;

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
@IdClass(ModuleId.class)
@Table(name = "module")
public class ModuleData {

    @Id
    private Long guild;

    @Id
    private String module;

    private Byte flag;

}
