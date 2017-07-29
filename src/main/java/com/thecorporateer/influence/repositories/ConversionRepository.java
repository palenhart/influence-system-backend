package com.thecorporateer.influence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thecorporateer.influence.objects.Conversion;

public interface ConversionRepository extends JpaRepository<Conversion, Long> {

}
