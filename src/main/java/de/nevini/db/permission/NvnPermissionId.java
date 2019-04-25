package de.nevini.db.permission;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NvnPermissionId implements Serializable
{
	
	private Long guild;
	
	private Long channel;
	
	private Byte type;
	
	private Long id;
	
	private String node;
	
}
