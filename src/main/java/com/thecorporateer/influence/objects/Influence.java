package com.thecorporateer.influence.objects;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "corporateer_id", "department_id", "division_id",
		"type_id" }) })

@Entity
public class Influence extends JpaEntity {

	public Influence(Corporateer corporateer, Department department, Division division, InfluenceType type,
			int amount) {
		this.corporateer = corporateer;
		this.department = department;
		this.division = division;
		this.type = type;
		this.amount = amount;
	}

	public Influence(Corporateer corporateer, Department department, InfluenceType type, int amount) {
		this(corporateer, department, null, type, amount);
	}

	public Influence(Corporateer corporateer, Division division, InfluenceType type, int amount) {
		this(corporateer, null, division, type, amount);
	}

	public Influence(Corporateer corporateer, InfluenceType type, int amount) {
		this(corporateer, null, null, type, amount);
	}

	@NotNull
	@ManyToOne
	private Corporateer corporateer;
	@ManyToOne
	private Division division;
	@ManyToOne
	private Department department;
	@NotNull
	@ManyToOne
	private InfluenceType type;
	@NotNull
	private int amount;

}
