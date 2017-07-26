package com.thecorporateer.influence.objects;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class InfluenceType extends JpaEntity{
	
	public InfluenceType(String name) {
		this.name = name;
	}

	@NotNull
	@NotBlank
	private String name;
	
}
