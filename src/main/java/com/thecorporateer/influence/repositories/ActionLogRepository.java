package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.thecorporateer.influence.objects.ActionLog;

/**
 * @author Zollak
 * 
 *         Repository for ActionLog entities
 *
 */
@RepositoryRestResource(exported = false)
public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {

}
