package com.thecorporateer.influence.objects;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Conversion extends JpaEntity {

	@NotNull
	@NotBlank
	private String timestamp;
	@ManyToOne
	private Corporateer corporateer;
	@ManyToOne
	private Division fromDivision;
	@ManyToOne
	private Division toDivision;
	@ManyToOne
	private InfluenceType type;
	private int amount;

}
