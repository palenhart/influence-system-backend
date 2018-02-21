package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.thecorporateer.influence.objects.Rank;

/**
 * @author Zollak
 * 
 *         Repository for Rank objects
 * 
 */
@RepositoryRestResource(exported = false)
public interface RankRepository extends JpaRepository<Rank, Long> {
	Rank findByName(String name);

	Rank findByRankLevel(int rankLevel);

}
