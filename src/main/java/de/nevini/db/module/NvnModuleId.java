package de.nevini.db.module;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NvnModuleId implements Serializable
{
	
	private Long guild;
	
	private String module;
	
}
