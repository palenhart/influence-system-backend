package com.thecorporateer.influence.objects;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class User extends JpaEntity {

	@Column(length = 50, unique = true)
	@NotNull
	@Size(min = 3, max = 50)
	private String username;

	@Column(length = 100)
	@NotNull
	@Size(min = 8, max = 100)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@Column(length = 50, unique = true)
	@NotNull
	@Size(min = 4, max = 50)
	private String email;

	@NotNull
	private Boolean enabled;

//	@Temporal(TemporalType.TIMESTAMP)
//	@NotNull
//	private Date lastPasswordResetDate;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "USER_AUTHORITY", joinColumns = {
			@JoinColumn(name = "USER_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID") })
	private List<UserRole> roles;

	@OneToOne
	private Corporateer corporateer;
}
