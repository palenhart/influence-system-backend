package com.thecorporateer.influence.objects;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
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
public class Department extends JpaEntity {

	public Department(String name) {
		this.name = name;
	}

	@NotNull
	@NotBlank
	@JsonView(Views.Public.class)
	private String name;
	@OneToMany(mappedBy = "department")
	@JsonIgnore
	private List<Division> divisions;

}
