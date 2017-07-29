package com.thecorporateer.influence.objects;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class Conversion extends JpaEntity {

	@NotNull
	@NotBlank
	private String timestamp;
	@ManyToOne
	private Corporateer corporateer;
	@ManyToOne
	private Department fromDepartment;
	@ManyToOne
	private Division fromDivision;
	@ManyToOne
	private Department toDepartment;
	@ManyToOne
	private Division toDivision;
	@ManyToOne
	private InfluenceType type;
	private int amount;

}
