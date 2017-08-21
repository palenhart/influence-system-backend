package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.thecorporateer.influence.objects.Division;

@RepositoryRestResource
public interface DivisionRepository extends JpaRepository<Division, Long> {

	@RestResource(exported = false)
	public <S extends Division> S save(Division division);

	@Override
	@RestResource(exported = false)
	public void delete(Division division);

	public Division findByName(@Param("name") String name);

}
