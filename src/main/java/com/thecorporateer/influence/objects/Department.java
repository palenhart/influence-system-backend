package com.thecorporateer.influence.objects;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.thecorporateer.influence.controllers.Views;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Zollak
 * 
 *         Entity to store departments
 *
 */
@Getter
@Setter
@NoArgsConstructor

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Department extends JpaEntity {

	public Department(String name) {
		this.name = name;
	}

	@NotNull
	@NotBlank
	@JsonView(Views.Corporateer.class)
	private String name;
	@OneToMany(mappedBy = "department")
	private List<Division> divisions;
	@OneToMany(mappedBy = "department")
	private List<Influence> influence;

}
