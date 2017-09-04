package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Department;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.objects.InfluenceType;

/**
 * @author Zollak
 * 
 *         Repository for Influence objects
 *         
 */
@RepositoryRestResource(exported = false)
public interface InfluenceRepository extends JpaRepository<Influence, Long> {

	public Influence findByCorporateerAndDepartmentAndDivisionAndType(@Param("corporateer") Corporateer corporateer,
			@Param("department") Department department, @Param("division") Division division,
			@Param("influencetype") InfluenceType influencetype);

}
