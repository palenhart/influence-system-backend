package com.thecorporateer.influence.objects;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
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
 *         Entity to store corporateers
 *
 */
@Getter
@Setter
@NoArgsConstructor

@Entity
public class Corporateer extends JpaEntity {

	@NotEmpty
	@JsonView(Views.Public.class)
	private String name;
	@OneToOne(mappedBy = "corporateer")
	@JsonIgnore
	private User user;
	@NotNull
	@Min(0)
	@JsonView(Views.Private.class)
	private int tributes = 0;
	@NotNull
	@Min(0)
	@JsonView(Views.Public.class)
	private int totalInfluence = 0;
	@NotNull
	@Min(0)
	@JsonView(Views.Public.class)
	private int lifetimeInfluence = 0;
	@OneToMany(mappedBy = "corporateer")
	@JsonIgnore
	private List<Influence> influence;
	@NotNull
	@ManyToOne
	@JsonView(Views.Public.class)
	private Division mainDivision;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(joinColumns = {
			@JoinColumn(name = "CORPORATEER_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "DIVISION_ID", referencedColumnName = "ID") })
	@JsonView(Views.Private.class)
	private List<Division> memberOfDivisions;
	@NotNull
	@ManyToOne
	@JsonView(Views.Public.class)
	private Rank rank;
	@OneToMany(mappedBy = "sender")
	@JsonIgnore
	private List<Transaction> sentTransactions;
	@OneToMany(mappedBy = "receiver")
	@JsonIgnore
	private List<Transaction> receivedTransactions;

}
