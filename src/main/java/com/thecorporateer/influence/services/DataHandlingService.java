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
import com.thecorporateer.influence.repositories.CorporateerRepository;
import com.thecorporateer.influence.repositories.DepartmentRepository;
import com.thecorporateer.influence.repositories.DivisionRepository;
import com.thecorporateer.influence.repositories.InfluenceRepository;
import com.thecorporateer.influence.repositories.InfluenceTypeRepository;
import com.thecorporateer.influence.repositories.RankRepository;

/**
 * @author patric.lenhart
 *
 */
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

//	private List<Rank> ranks = new ArrayList<>();
	private List<InfluenceType> types = new ArrayList<>();
	private List<Department> departments = new ArrayList<>();
	private List<Division> divisions = new ArrayList<>();
	private List<Corporateer> corporateers = new ArrayList<>();

	/**
	 * Create a new corporateer with the provided name. The coporateer will have the
	 * lowest rank and no main division set.
	 * 
	 * @param name
	 *            the name of the corporateer
	 */
	public void createCorporateer(String name) {
		Corporateer corporateer = new Corporateer();
		corporateer.setName(name);
		corporateer.setRank(rankRepository.findOne(1L));
		corporateer.setMainDivision(divisionRepository.findOne(1L));
		corporateer = corporateerRepository.save(corporateer);
		initializeInfluenceTable(corporateer);
	}

	/**
	 * Distribute influence to corporateers according to their rank
	 */
	public void distributeInfluence() {
		refreshCorporateers();

		List<Corporateer> corporateersToSave = new ArrayList<>();

		for (Corporateer corporateer : corporateers) {
			int currentTributes = corporateer.getTributes();
			int tributesToAdd = corporateer.getRank().getTributesPerWeek();
			corporateer.setTributes(currentTributes + tributesToAdd);
			corporateersToSave.add(corporateer);
		}
		corporateerRepository.save(corporateersToSave);
	}

	/**
	 * Initialize the influence table for a new corporateer to track influence
	 * received
	 * 
	 * @param corporateer
	 *            the corporateer for whom the influence table should be initialized
	 */
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

	/**
	 * Refreshes all lists used in DataHandlingService
	 */
	public void refreshAll() {
//		refreshRanks();
		refreshInfluenceTypes();
		refreshDepartments();
		refreshDivisions();
		refreshCorporateers();
	}

//	/**
//	 * Refreshes list of ranks from repository
//	 */
//	private void refreshRanks() {
//		ranks = rankRepository.findAll();
//	}

	/**
	 * Refreshes list of influence types from repository
	 */
	private void refreshInfluenceTypes() {
		types = influenceTypeRepository.findAll();
	}

	/**
	 * Refreshes list of departments from repository
	 */
	private void refreshDepartments() {
		departments = departmentRepository.findAll();
	}

	/**
	 * Refreshes list of division from repository
	 */
	private void refreshDivisions() {
		divisions = divisionRepository.findAll();
	}

	/**
	 * Refreshes list of corporateers from repository
	 */
	private void refreshCorporateers() {
		corporateers = corporateerRepository.findAll();
	}
}
