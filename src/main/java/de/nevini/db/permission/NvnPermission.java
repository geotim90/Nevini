package de.nevini.db.permission;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(NvnPermissionId.class)
@Entity
public class NvnPermission
{
	
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
