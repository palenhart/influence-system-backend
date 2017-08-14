package com.thecorporateer.influence.objects;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.thecorporateer.influence.controllers.Views;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Transaction extends JpaEntity{

	@NotNull
	@NotBlank
	@JsonView(Views.Transactions.class)
	private String timestamp;
	@NotNull
	@Min(1)
	@JsonView(Views.Transactions.class)
	private int amount;
	@NotNull
	@ManyToOne
	@JsonView(Views.Transactions.class)
	private Corporateer sender;
	@NotNull
	@ManyToOne
	@JsonView(Views.Transactions.class)
	private Corporateer receiver;
	@NotNull
	@NotBlank
	@JsonView(Views.Transactions.class)
	private String message;
	@NotNull
	@ManyToOne
	@JsonView(Views.Transactions.class)
	private InfluenceType type;
	@NotNull
	@ManyToOne
	@JsonView(Views.Transactions.class)
	private Division division;
	@NotNull
	@ManyToOne
	private Department department;
	
}
