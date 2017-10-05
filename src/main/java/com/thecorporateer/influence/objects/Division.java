package com.thecorporateer.influence.objects;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
 *         Entity to store divisions
 *
 */
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
	@JsonView(Views.Public.class)
	private String name;
	@NotNull
	@ManyToOne
	@JsonView(Views.Public.class)
	private Department department;
	@ManyToMany(mappedBy = "memberOfDivisions", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Corporateer> members;
	@OneToMany(mappedBy = "mainDivision")
	@JsonIgnore
	private List<Corporateer> corporateers;
	@OneToMany(mappedBy = "division")
	@JsonIgnore
	private List<Influence> influence;
	@OneToMany(mappedBy = "receivingDivision")
	@JsonIgnore
	private List<Transaction> incomingTransactions;

}
