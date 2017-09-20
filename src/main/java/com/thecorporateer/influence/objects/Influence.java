package com.thecorporateer.influence.objects;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Zollak
 * 
 *         Entity to store current influence of members
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = { "corporateer_id", "division_id", "type_id" }) })

@Entity
public class Influence extends JpaEntity {

	@NotNull
	@ManyToOne
	private Corporateer corporateer;
	@NotNull
	@ManyToOne
	private Division division;
	@NotNull
	@ManyToOne
	private InfluenceType type;
	@NotNull
	@Min(0)
	private int amount;

}
