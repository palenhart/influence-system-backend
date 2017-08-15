package com.thecorporateer.influence.objects;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.thecorporateer.influence.controllers.Views;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Zollak
 * 
 *         Entity to store users
 *
 */
@Getter
@Setter

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User extends JpaEntity {

	@Column(length = 50, unique = true)
	@NotNull
	@Size(min = 3, max = 50)
	@JsonView(Views.UserProfile.class)
	private String username;

	@Column(length = 100)
	@NotNull
	@Size(min = 8, max = 100)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@Column(length = 50)
	@NotNull
	@Size(min = 4, max = 50)
	@JsonView(Views.UserProfile.class)
	private String email;

	@NotNull
	private Boolean enabled;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date lastPasswordResetDate;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "USER_AUTHORITY", joinColumns = {
			@JoinColumn(name = "USER_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID") })
	@JsonView(Views.UserProfile.class)
	private List<UserRole> roles;

	@OneToOne
	private Corporateer corporateer;
}
