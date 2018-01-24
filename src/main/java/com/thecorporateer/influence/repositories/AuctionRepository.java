package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.thecorporateer.influence.objects.Auction;

/**
 * @author Zollak
 * 
 *         Repository for Auction entities
 *
 */
@RepositoryRestResource(exported = false)
public interface AuctionRepository extends JpaRepository<Auction, Long> {

}
