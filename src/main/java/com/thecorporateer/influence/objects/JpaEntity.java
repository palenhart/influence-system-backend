package com.thecorporateer.influence.objects;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@MappedSuperclass
public class JpaEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
}
