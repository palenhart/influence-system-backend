package com.thecorporateer.influence.objects;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Zollak
 * 
 *         Entity to store user roles
 *
 */
@Getter
@Setter

@Entity
@Table(name = "AUTHORITY")
public class UserRole extends JpaEntity {

	@Column(length = 20)
	@NotNull
	private String name;

	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<User> users;
}
