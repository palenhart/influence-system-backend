package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Department;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.objects.InfluenceType;

@RepositoryRestResource
public interface InfluenceRepository extends JpaRepository<Influence, Long> {

	@RestResource(exported = false)
	public <S extends Influence> S save(Influence influence);

	@Override
	@RestResource(exported = false)
	public void delete(Influence influence);

	public Influence findByCorporateerAndDepartmentAndDivisionAndType(@Param("corporateer") Corporateer corporateer,
			@Param("department") Department department, @Param("division") Division division,
			@Param("influencetype") InfluenceType influencetype);

}
