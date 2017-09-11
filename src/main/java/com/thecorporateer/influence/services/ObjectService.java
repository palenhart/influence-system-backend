package com.thecorporateer.influence.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.objects.Department;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.Transaction;
import com.thecorporateer.influence.repositories.DivisionRepository;
import com.thecorporateer.influence.repositories.InfluenceTypeRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Service
public class ObjectService {

	@Autowired
	private DivisionRepository divisionRepository;
	@Autowired
	private InfluenceTypeRepository influenceTypeRepository;

	public List<DivisionResponse> getDivisions() {

		List<DivisionResponse> divisions = new ArrayList<DivisionResponse>();

		for (Division division : divisionRepository.findAll()) {
			int currentInfluence = 0;
			for (Transaction transaction : division.getIncomingTransactions()) {
				if (transaction.getType().equals(influenceTypeRepository.findOne(1L))) {
					currentInfluence = currentInfluence + transaction.getAmount();
				}
			}
			divisions.add(new DivisionResponse(division, division.getCorporateers().size(), currentInfluence));
		}

		return divisions;
	}

}

@Getter
@AllArgsConstructor
class DivisionResponse {

	public DivisionResponse(Division division, int membercount, int currentInfluence) {
		this.name = division.getName();
		this.department = division.getDepartment();
		this.membercount = membercount;
		this.currentInfluence = currentInfluence;
	}

	String name;
	Department department;
	int membercount;
	int currentInfluence;
}