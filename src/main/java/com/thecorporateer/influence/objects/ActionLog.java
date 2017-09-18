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
 *         Entity used to log actions performed by users
 *
 */
@Getter
@Setter
@NoArgsConstructor

@Entity
public class ActionLog extends JpaEntity {

	@NotNull
	@NotBlank
	private String timestamp;
	@ManyToOne
	private User user;
	@NotNull
	@NotBlank
	private String action;

}
