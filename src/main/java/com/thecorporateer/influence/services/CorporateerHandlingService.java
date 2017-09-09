package com.thecorporateer.influence.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.objects.InfluenceType;
import com.thecorporateer.influence.repositories.CorporateerRepository;
import com.thecorporateer.influence.repositories.DivisionRepository;
import com.thecorporateer.influence.repositories.InfluenceRepository;
import com.thecorporateer.influence.repositories.InfluenceTypeRepository;
import com.thecorporateer.influence.repositories.RankRepository;

/**
 * @author Zollak
 * 
 *         Service to handle actions affecting corporateer entities
 *
 */
@Service
public class CorporateerHandlingService {

	@Autowired
	private RankRepository rankRepository;
	@Autowired
	private InfluenceTypeRepository influenceTypeRepository;
	@Autowired
	private CorporateerRepository corporateerRepository;
	@Autowired
	private DivisionRepository divisionRepository;
	@Autowired
	private InfluenceRepository influenceRepository;

	// private List<Rank> ranks = new ArrayList<>();
	private List<InfluenceType> types = new ArrayList<>();
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
	 * Initialize the influence table for a new corporateer to track influence
	 * received
	 * 
	 * @param corporateer
	 *            the corporateer for whom the influence table should be initialized
	 */
	private void initializeInfluenceTable(Corporateer corporateer) {
		refreshInfluenceTypes();
		refreshDivisions();

		List<Influence> influences = new ArrayList<>();
		for (InfluenceType type : types) {
			for (Division division : divisions) {
				influences.add(new Influence(corporateer, division.getDepartment(), division, type, 0));
			}
		}
		influences = influenceRepository.save(influences);
	}

	/**
	 * Distribute influence to corporateers according to their rank
	 */
	public void distributeTributes() {
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

	public int getTotalInfluence(Corporateer corporateer) {
		int total = 0;
		for (Influence influence : corporateer.getInfluence()) {
			total = total + influence.getAmount();
		}
		return total;
	}

	public void setMainDivision(Corporateer corporateer, Division division) {
		corporateer.setMainDivision(division);
		corporateerRepository.save(corporateer);
	}

	// /**
	// * Refreshes list of ranks from repository
	// */
	// private void refreshRanks() {
	// ranks = rankRepository.findAll();
	// }

	/**
	 * Refreshes list of influence types from repository
	 */
	private void refreshInfluenceTypes() {
		types = influenceTypeRepository.findAll();
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
