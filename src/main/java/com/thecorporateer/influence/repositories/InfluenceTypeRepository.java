package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.thecorporateer.influence.objects.InfluenceType;
@RepositoryRestResource
public interface InfluenceTypeRepository extends JpaRepository<InfluenceType, Long> {

	public InfluenceType findByName(String name);

}
