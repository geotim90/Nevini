package de.nevini.db.game;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NvnGame
{
	
	@Id
	private Long id;
	
	private String name;
	
}
