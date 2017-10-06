package com.thecorporateer.influence.objects;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
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
public class UserRole {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
	@SequenceGenerator(name = "role_seq", sequenceName = "role_seq", allocationSize = 1)
	private Long id;

	@Column(length = 20)
	@NotNull
	private String name;

	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<User> users;
}
