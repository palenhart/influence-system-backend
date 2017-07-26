package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Transaction;
@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	
	public Transaction findBySender(Corporateer corporateer);

}
