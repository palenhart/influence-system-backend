package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.thecorporateer.influence.objects.RoleName;
import com.thecorporateer.influence.objects.UserRole;

/**
 * @author Zollak
 * 
 *         Repository for UserRole entities
 *
 */
@RepositoryRestResource(exported = false)
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
	
	public UserRole findByName(@Param("name") RoleName name);

}
