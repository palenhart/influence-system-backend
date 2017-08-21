package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.thecorporateer.influence.objects.Rank;

@RepositoryRestResource(exported = false)
public interface RankRepository extends JpaRepository<Rank, Long> {

	@RestResource(exported = false)
	public <S extends Rank> S save(Rank rank);

	@Override
	@RestResource(exported = false)
	public void delete(Rank rank);

}
