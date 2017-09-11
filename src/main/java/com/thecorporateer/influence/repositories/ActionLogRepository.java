package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thecorporateer.influence.objects.ActionLog;

/**
 * @author Zollak
 * 
 *         Repository for ActionLog entities
 *
 */
public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {

}
