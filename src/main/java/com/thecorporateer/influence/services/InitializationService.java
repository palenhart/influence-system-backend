package com.thecorporateer.influence.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Department;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.InfluenceType;
import com.thecorporateer.influence.objects.Rank;
import com.thecorporateer.influence.repositories.CorporateerRepository;
import com.thecorporateer.influence.repositories.DepartmentRepository;
import com.thecorporateer.influence.repositories.DivisionRepository;
import com.thecorporateer.influence.repositories.InfluenceRepository;
import com.thecorporateer.influence.repositories.InfluenceTypeRepository;
import com.thecorporateer.influence.repositories.RankRepository;

@Service
public class InitializationService {

	@Autowired
	private CorporateerHandlingService corporateerHandlingService;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private InfluenceHandlingService influenceHandlingService;

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

	private void initializeRanks() {

		ranks.add(new Rank("Junior Associate", 50));
		ranks.add(new Rank("Associate", 100));
		ranks.add(new Rank("Senior Associate", 200));
		ranks.add(new Rank("Manager", 400));
		ranks.add(new Rank("Director", 800));
		ranks.add(new Rank("Board Member", 1600));
		ranks.add(new Rank("CEO", 1600));

		ranks = rankRepository.save(ranks);
	}

	private void initializeInfluence() {

		types.add(new InfluenceType("INFLUENCE"));
		types.add(new InfluenceType("DEMERIT"));

		types = influenceTypeRepository.save(types);
	}

	private void initializeDepartments() {

		departments.add(new Department("none"));
		departments.add(new Department("Exploration"));
		departments.add(new Department("Business"));
		departments.add(new Department("Security"));
		departments.add(new Department("Resources"));
		departments.add(new Department("Social"));
		departments.add(new Department("Support"));
		departments.add(new Department("Public Relations"));

		departments = departmentRepository.save(departments);
	}

	private void initializeDivisions() {
		divisions.add(new Division("none", departments.get(0)));
		divisions.add(new Division("Cartograhy", departments.get(1)));
		divisions.add(new Division("Prospecting", departments.get(1)));
		divisions.add(new Division("Research", departments.get(1)));

		divisions.add(new Division("Contracts", departments.get(2)));
		divisions.add(new Division("Finance", departments.get(2)));
		divisions.add(new Division("Trade", departments.get(2)));

		divisions.add(new Division("CSOC", departments.get(3)));
		divisions.add(new Division("Ground", departments.get(3)));
		divisions.add(new Division("Repossesion", departments.get(3)));
		divisions.add(new Division("Space", departments.get(3)));

		divisions.add(new Division("Development", departments.get(4)));
		divisions.add(new Division("Extraction", departments.get(4)));
		divisions.add(new Division("Transport", departments.get(4)));

		divisions.add(new Division("Diplomacy", departments.get(5)));
		divisions.add(new Division("HR", departments.get(5)));
		divisions.add(new Division("Training", departments.get(5)));

		divisions.add(new Division("CSAR", departments.get(6)));
		divisions.add(new Division("Engineering", departments.get(6)));
		divisions.add(new Division("IT", departments.get(6)));

		divisions.add(new Division("e-Sports", departments.get(7)));
		divisions.add(new Division("Media", departments.get(7)));

		divisions = divisionRepository.save(divisions);
	}

	public void initialize() {
		initializeRanks();
		initializeInfluence();
		initializeDepartments();
		initializeDivisions();

		corporateerHandlingService.createCorporateer("Peter");
		corporateerHandlingService.createCorporateer("Max");

		List<Corporateer> corporateers = corporateerRepository.findAll();
		corporateers.get(0).setRank(ranks.get(6));
		corporateers.get(0).setMainDivision(divisionRepository.findOne(2L));
		corporateers.get(1).setMainDivision(divisionRepository.findOne(2L));
		corporateers = corporateerRepository.save(corporateers);

		corporateerHandlingService.distributeTributes();

		transactionService.transfer(corporateerRepository.findOne(1L), corporateerRepository.findOne(2L), "Banane", 20, types.get(0));
		influenceHandlingService.convertInfluenceToDepartment(
				influenceRepository.findByCorporateerAndDepartmentAndDivisionAndType(corporateers.get(1),
						corporateers.get(1).getMainDivision().getDepartment(), corporateers.get(1).getMainDivision(),
						types.get(0)));
		
		influenceHandlingService.convertInfluenceToGeneral(
				influenceRepository.findByCorporateerAndDepartmentAndDivisionAndType(corporateers.get(1),
						corporateers.get(1).getMainDivision().getDepartment(), divisionRepository.findOne(1L),
						types.get(0)));

	}
}
