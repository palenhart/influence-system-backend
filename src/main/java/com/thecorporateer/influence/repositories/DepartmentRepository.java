package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.thecorporateer.influence.objects.Department;
@RepositoryRestResource
public interface DepartmentRepository extends JpaRepository<Department, Long> {
	
	public Department findByName(String name);

}
