package de.nevini.db.module;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(NvnModuleId.class)
@Entity
public class NvnModule
{
	
	@Id
	private Long guild;
	
	@Id
	private String module;
	
	private Byte flag;
	
}
