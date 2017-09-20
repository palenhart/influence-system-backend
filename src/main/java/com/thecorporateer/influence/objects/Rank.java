package com.thecorporateer.influence.objects;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Rank extends JpaEntity {

	@NotNull
	@NotBlank
	private String name;
	@NotNull
	private int level;
	@NotNull
	@Min(1)
	private int tributesPerWeek;
	@NotNull
	private int influenceToBuy;
	@NotNull
	private boolean buyingAllowed;
	@OneToMany(mappedBy = "rank")
	@JsonIgnore
	private List<Corporateer> corporateer;

}
