package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.thecorporateer.influence.objects.Corporateer;

@RepositoryRestResource
public interface CorporateerRepository extends JpaRepository<Corporateer, Long> {

	@RestResource(exported = false)
	public <S extends Corporateer> S save(Corporateer corp);

	@Override
	@RestResource(exported = false)
	public void delete(Corporateer corp);

	public Corporateer findByName(@Param("name") String name);

}
