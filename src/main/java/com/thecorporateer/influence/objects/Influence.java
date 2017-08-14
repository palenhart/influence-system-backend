package com.thecorporateer.influence.objects;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "corporateer_id", "department_id", "division_id",
		"type_id" }) })

@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Influence extends JpaEntity {

	public Influence(Corporateer corporateer, Department department, Division division, InfluenceType type,
			int amount) {
		this.corporateer = corporateer;
		this.department = department;
		this.division = division;
		this.type = type;
		this.amount = amount;
	}

	@NotNull
	@ManyToOne
	private Corporateer corporateer;
	@NotNull
	@ManyToOne
	private Division division;
	@NotNull
	@ManyToOne
	private Department department;
	@NotNull
	@ManyToOne
	private InfluenceType type;
	@NotNull
	@Min(0)
	private int amount;

}
