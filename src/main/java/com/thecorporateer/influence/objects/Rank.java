package com.thecorporateer.influence.objects;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class Rank extends JpaEntity {

	public Rank(String name, int tributesPerWeek) {
		this.name = name;
		this.tributesPerWeek = tributesPerWeek;
	}

	@NotNull
	@NotBlank
	private String name;
	@NotNull
	@Min(1)
	private int tributesPerWeek;
	@OneToMany(mappedBy = "rank")
	private List<Corporateer> corporateer;

}
