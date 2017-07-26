package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Department;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.objects.InfluenceType;
@RepositoryRestResource
public interface InfluenceRepository extends JpaRepository<Influence, Long> {
	
	public Influence findByCorporateerAndDepartmentAndDivisionAndType(Corporateer corporateer, Department department, Division division, InfluenceType influencetype);

}
