package com.thecorporateer.influence.objects;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
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
 *         Entity to store ranks
 *
 */
@Getter
@Setter
@NoArgsConstructor

@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Rank extends JpaEntity {

	@NotNull
	@NotBlank
	@JsonView(Views.Public.class)
	private String name;
	@NotNull
	@JsonView(Views.Public.class)
	private int level;
	@NotNull
	@Min(1)
	@JsonView(Views.Public.class)
	private int tributesPerWeek;
	@NotNull
	@JsonView(Views.Public.class)
	private int influenceToBuy;
	@NotNull
	@JsonView(Views.Public.class)
	private boolean buyingAllowed;
	@OneToMany(mappedBy = "rank")
	@JsonIgnore
	private List<Corporateer> corporateer;

}
