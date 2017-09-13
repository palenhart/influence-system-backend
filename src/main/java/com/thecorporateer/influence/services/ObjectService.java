package com.thecorporateer.influence.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.objects.Conversion;
import com.thecorporateer.influence.objects.Department;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.InfluenceType;
import com.thecorporateer.influence.objects.Rank;
import com.thecorporateer.influence.objects.Transaction;
import com.thecorporateer.influence.repositories.ConversionRepository;
import com.thecorporateer.influence.repositories.DepartmentRepository;
import com.thecorporateer.influence.repositories.DivisionRepository;
import com.thecorporateer.influence.repositories.InfluenceTypeRepository;
import com.thecorporateer.influence.repositories.RankRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Service
public class ObjectService {

	@Autowired
	private DivisionRepository divisionRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private RankRepository rankRepository;
	@Autowired
	private InfluenceTypeRepository influenceTypeRepository;
	@Autowired
	private ConversionRepository conversionRepository;

	public Division getDivisionByName(String name) {

		return divisionRepository.findByName(name);
	}

	public Division getDivisionByNameAndDepartment(String name, Department department) {

		return divisionRepository.findByNameAndDepartment(name, department);
	}

	public Division getDefaultDivision() {

		return divisionRepository.findOne(1L);
	}

	public List<Division> getAllDivisions() {

		return divisionRepository.findAll();
	}
	
	public List<DivisionResponse> getDivisionsAsResponse() {

		List<DivisionResponse> divisions = new ArrayList<DivisionResponse>();

		for (Division division : getAllDivisions()) {
			int currentInfluence = 0;
			for (Transaction transaction : division.getIncomingTransactions()) {
				if (transaction.getType().equals(getInfluenceTypeId(1L))) {
					currentInfluence = currentInfluence + transaction.getAmount();
				}
			}
			divisions.add(new DivisionResponse(division, division.getCorporateers().size(), currentInfluence));
		}

		return divisions;
	}
	
	public Department getDepartmentByName(String name) {

		return departmentRepository.findByName(name);
	}

	public Rank getRankByName(String name) {

		return rankRepository.findByName(name);
	}
	
	public List<Rank> getAllRanks() {

		return rankRepository.findAll();
	}

	public Rank getLowestRank() {

		return rankRepository.findByLevel(0);
	}

	public InfluenceType getInfluenceTypeId(Long id) {

		return influenceTypeRepository.findOne(id);
	}
	
	public InfluenceType getInfluenceTypeByName(String name) {

		return influenceTypeRepository.findByName(name);
	}

	public List<InfluenceType> getAllInfluenceTypes() {

		return influenceTypeRepository.findAll();
	}

	public Conversion saveConversion(Conversion conversion) {

		return conversionRepository.save(conversion);
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