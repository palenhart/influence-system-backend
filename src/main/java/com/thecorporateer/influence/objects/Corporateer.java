package com.thecorporateer.influence.objects;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class Corporateer extends JpaEntity {

	@NotNull
	@NotBlank
	private String name;
	@JsonIgnore
	private String password;
	private int tributes = 0;
	//TODO: create when creating Corporateer (eventhandler after save)
	@OneToMany(mappedBy="corporateer")
	private List<Influence> influence;
	@NotNull
	@ManyToOne
	private Division mainDivision;
	@NotNull
	@ManyToOne
	private Rank rank;
	@OneToMany(mappedBy="sender")
	private List<Transaction> sentTransaction;
	@OneToMany(mappedBy="receiver")
	private List<Transaction> receivedTransaction;
	// private Position position;

}
