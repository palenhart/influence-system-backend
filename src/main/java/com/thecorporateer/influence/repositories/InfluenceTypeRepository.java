package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.thecorporateer.influence.objects.InfluenceType;

@RepositoryRestResource(exported = false)
public interface InfluenceTypeRepository extends JpaRepository<InfluenceType, Long> {

	public InfluenceType findByName(@Param("name") String name);

}
