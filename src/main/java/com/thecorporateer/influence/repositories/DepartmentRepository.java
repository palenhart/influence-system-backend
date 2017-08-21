package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.thecorporateer.influence.objects.Department;

@RepositoryRestResource(exported = false)
public interface DepartmentRepository extends JpaRepository<Department, Long> {
	
	@RestResource(exported = false)
	public <S extends Department> S save(Department department);

	@Override
	@RestResource(exported = false)
	public void delete(Department department);

	public Department findByName(@Param("name") String name);

}
