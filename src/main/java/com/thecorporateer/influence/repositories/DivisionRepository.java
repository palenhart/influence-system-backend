package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.thecorporateer.influence.objects.Division;
@RepositoryRestResource
public interface DivisionRepository extends JpaRepository<Division, Long> {

}
