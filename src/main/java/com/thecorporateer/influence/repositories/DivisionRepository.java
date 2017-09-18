package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.thecorporateer.influence.objects.Department;
import com.thecorporateer.influence.objects.Division;

/**
 * @author Zollak
 * 
 *         Repository for Division objects
 *         
 */
@RepositoryRestResource(exported = false)
public interface DivisionRepository extends JpaRepository<Division, Long> {

	public Division findByName(@Param("name") String name);
	
	public Division findByNameAndDepartment(@Param("name") String name, @Param("department") Department department);

}
