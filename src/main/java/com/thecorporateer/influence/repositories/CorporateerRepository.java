package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.thecorporateer.influence.objects.Corporateer;

@RepositoryRestResource(exported = false)
public interface CorporateerRepository extends JpaRepository<Corporateer, Long> {

	public Corporateer findByName(@Param("name") String name);

}
