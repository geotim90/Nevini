package de.nevini.db.permission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionId implements Serializable {

	private Long guild;

	private Long channel;

	private Byte type;

	private Long id;

	private String node;

}
