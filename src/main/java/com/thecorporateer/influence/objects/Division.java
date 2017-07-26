package com.thecorporateer.influence.objects;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class Division extends JpaEntity {
	
	public Division(String name, Department department) {
		this.name = name;
		this.department = department;
	}

	@NotNull
	@NotBlank
	private String name;
	@NotNull
	@ManyToOne
	private Department department;
	@OneToMany(mappedBy = "mainDivision")
	private List<Corporateer> corporateers;
	@OneToMany(mappedBy = "division")
	private List<Influence> influence;

}
