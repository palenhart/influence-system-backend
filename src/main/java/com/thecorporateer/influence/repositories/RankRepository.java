package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.thecorporateer.influence.objects.Rank;

@RepositoryRestResource(exported = false)
public interface RankRepository extends JpaRepository<Rank, Long> {

}
