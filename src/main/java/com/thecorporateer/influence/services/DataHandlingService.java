package com.thecorporateer.influence.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Department;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.objects.InfluenceType;
import com.thecorporateer.influence.objects.Rank;
import com.thecorporateer.influence.repositories.CorporateerRepository;
import com.thecorporateer.influence.repositories.DepartmentRepository;
import com.thecorporateer.influence.repositories.DivisionRepository;
import com.thecorporateer.influence.repositories.InfluenceRepository;
import com.thecorporateer.influence.repositories.InfluenceTypeRepository;
import com.thecorporateer.influence.repositories.RankRepository;

@Service
public class DataHandlingService {

	@Autowired
	private RankRepository rankRepository;
	@Autowired
	private InfluenceTypeRepository influenceTypeRepository;
	@Autowired
	private CorporateerRepository corporateerRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private DivisionRepository divisionRepository;
	@Autowired
	private InfluenceRepository influenceRepository;

	private List<Rank> ranks = new ArrayList<>();
	private List<InfluenceType> types = new ArrayList<>();
	private List<Department> departments = new ArrayList<>();
	private List<Division> divisions = new ArrayList<>();
	private List<Corporateer> corporateers = new ArrayList<>();

	public void initializeInfluenceTable(Corporateer corporateer) {
		refreshInfluenceTypes();
		refreshDepartments();
		refreshDivisions();

		List<Influence> influences = new ArrayList<>();
		for (InfluenceType type : types) {
			for (Department department : departments) {
				influences.add(new Influence(corporateer, department, divisions.get(0), type, 0));
			}
			for (Division division : divisions) {
				if (division.getId().longValue() == 1L) {
					continue;
				}
				influences.add(new Influence(corporateer, division.getDepartment(), division, type, 0));
			}
		}
		influences = influenceRepository.save(influences);
	}

	public void refreshAll() {
		refreshRanks();
		refreshInfluenceTypes();
		refreshDepartments();
		refreshDivisions();
		refreshCorporateers();
	}

	private void refreshRanks() {
		ranks = rankRepository.findAll();
	}

	private void refreshInfluenceTypes() {
		types = influenceTypeRepository.findAll();
	}

	private void refreshDepartments() {
		departments = departmentRepository.findAll();
	}

	private void refreshDivisions() {
		divisions = divisionRepository.findAll();
	}

	private void refreshCorporateers() {
		corporateers = corporateerRepository.findAll();
	}
}
