package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Transaction;

@RepositoryRestResource(exported = false)
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	
	@RestResource(exported = false)
	public <S extends Transaction> S save(Transaction transaction);

	@Override
	@RestResource(exported = false)
	public void delete(Transaction transaction);

	public Transaction findBySender(@Param("sender") Corporateer corporateer);

}
