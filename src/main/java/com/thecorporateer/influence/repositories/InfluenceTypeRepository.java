package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.thecorporateer.influence.objects.InfluenceType;

@RepositoryRestResource
public interface InfluenceTypeRepository extends JpaRepository<InfluenceType, Long> {

	@RestResource(exported = false)
	public <S extends InfluenceType> S save(InfluenceType type);

	@Override
	@RestResource(exported = false)
	public void delete(InfluenceType type);

	public InfluenceType findByName(@Param("name") String name);

}
