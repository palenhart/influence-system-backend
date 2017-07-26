package com.thecorporateer.influence.objects;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class Transaction extends JpaEntity{
	
	@NotNull
	@Min(1)
	private int amount;
	@NotNull
	@ManyToOne
	private Corporateer sender;
	@NotNull
	@ManyToOne
	private Corporateer receiver;
	private String message;
	@NotNull
	@ManyToOne
	private InfluenceType type;
	@ManyToOne
	private Division division;
	
}
