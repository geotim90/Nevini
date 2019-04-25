package de.nevini.db.prefix;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NvnPrefix
{
	
	@Id
	private Long guild;
	
	private String prefix;
	
}
