package com.thecorporateer.influence.objects;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Zollak
 * 
 *         Entity to store conversions from specialized influence to more
 *         general influence
 *
 */
@Getter
@Setter
@NoArgsConstructor

@Entity
public class Conversion extends JpaEntity {

	@NotNull
	@NotBlank
	private String timestamp;
	@NotNull
	@ManyToOne
	private Corporateer corporateer;
	@NotNull
	@ManyToOne
	private Division fromDivision;
	@NotNull
	@ManyToOne
	private Division toDivision;
	@NotNull
	@ManyToOne
	private InfluenceType type;
	@NotNull
	private int amount;

}
