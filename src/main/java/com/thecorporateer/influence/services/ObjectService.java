package com.thecorporateer.influence.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.exceptions.DepartmentNotFoundException;
import com.thecorporateer.influence.exceptions.DivisionNotFoundException;
import com.thecorporateer.influence.exceptions.InfluenceTypeNotFoundException;
import com.thecorporateer.influence.exceptions.RankNotFoundException;
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

		Division division = divisionRepository.findByName(name);

		if (division == null) {
			throw new DivisionNotFoundException();
		}

		return division;
	}

	public Division getDivisionByNameAndDepartment(String name, Department department) {

		Division division = divisionRepository.findByNameAndDepartment(name, department);

		if (division == null) {
			throw new DivisionNotFoundException();
		}

		return division;
	}

	public Division getDefaultDivision() {

		Division division = divisionRepository.findOne(1L);

		if (division == null) {
			throw new DivisionNotFoundException();
		}

		return division;
	}

	public List<Division> getAllDivisions() {

		List<Division> divisions = divisionRepository.findAll();

		if (divisions == null || divisions.size() == 0) {
			throw new DivisionNotFoundException();
		}

		return divisions;
	}

	public List<DivisionResponse> getDivisionsAsResponse() {

		List<DivisionResponse> divisions = new ArrayList<DivisionResponse>();

		for (Division division : getAllDivisions()) {
			int currentInfluence = 0;
			for (Transaction transaction : division.getIncomingTransactions()) {
				if (transaction.getType().equals(getInfluenceTypeById(1L))) {
					currentInfluence = currentInfluence + transaction.getAmount();
				}
			}
			divisions.add(new DivisionResponse(division, division.getCorporateers().size(), currentInfluence));
		}

		return divisions;
	}

	public Department getDepartmentByName(String name) {

		Department department = departmentRepository.findByName(name);

		if (department == null) {
			throw new DepartmentNotFoundException();
		}

		return department;
	}

	public Rank getRankByName(String name) {

		Rank rank = rankRepository.findByName(name);

		if (rank == null) {
			throw new RankNotFoundException();
		}

		return rank;
	}

	public List<Rank> getAllRanks() {

		List<Rank> ranks = rankRepository.findAll();

		if (ranks == null) {
			throw new RankNotFoundException();
		}

		return ranks;
	}

	public Rank getLowestRank() {

		Rank rank = rankRepository.findByLevel(0);

		if (rank == null) {
			throw new RankNotFoundException();
		}

		return rank;
	}

	public InfluenceType getInfluenceTypeById(Long id) {

		InfluenceType influenceType = influenceTypeRepository.findOne(id);

		if (influenceType == null) {
			throw new InfluenceTypeNotFoundException();
		}

		return influenceType;
	}

	public InfluenceType getInfluenceTypeByName(String name) {

		InfluenceType influenceType = influenceTypeRepository.findByName(name);

		if (influenceType == null) {
			throw new InfluenceTypeNotFoundException();
		}

		return influenceType;
	}

	public List<InfluenceType> getAllInfluenceTypes() {

		List<InfluenceType> influenceTypes = influenceTypeRepository.findAll();

		if (influenceTypes == null) {
			throw new InfluenceTypeNotFoundException();
		}

		return influenceTypes;
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