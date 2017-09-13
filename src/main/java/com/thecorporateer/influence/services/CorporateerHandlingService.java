package com.thecorporateer.influence.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.exceptions.CorporateerNotFoundException;
import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.objects.InfluenceType;
import com.thecorporateer.influence.objects.Rank;
import com.thecorporateer.influence.repositories.CorporateerRepository;

/**
 * @author Zollak
 * 
 *         Service to handle actions affecting corporateer entities
 *
 */
@Service
public class CorporateerHandlingService {

	@Autowired
	private CorporateerRepository corporateerRepository;
	@Autowired
	private UserHandlingService userHandlingService;
	@Autowired
	private InfluenceHandlingService influenceHandlingService;
	@Autowired
	private ObjectService objectService;
	@Autowired
	private ActionLogService actionLogService;

	// private List<Rank> ranks = new ArrayList<>();
	private List<InfluenceType> types = new ArrayList<>();
	private List<Division> divisions = new ArrayList<>();
	private List<Corporateer> corporateers = new ArrayList<>();

	public Corporateer getCorporateerByName(String name) {

		Corporateer corporateer = corporateerRepository.findByName(name);

		if (corporateer == null) {
			throw new CorporateerNotFoundException();
		}

		return corporateerRepository.findByName(name);
	}
	
	public List<Corporateer> getAllCorporateers() {

		return corporateerRepository.findAll();
	}

	// TODO: Think about handling errors
	public Corporateer updateCorporateer(Corporateer corporateer) {

		return corporateerRepository.save(corporateer);
	}

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
		corporateer.setRank(objectService.getLowestRank());
		corporateer.setMainDivision(objectService.getDefaultDivision());
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
		influences = influenceHandlingService.updateInfluences(influences);
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

	public boolean setMainDivision(Authentication authentication, String divisionName) {
		Corporateer corporateer = userHandlingService.getUserByName(authentication.getName()).getCorporateer();
		Division division;

		// do nothing when there is no change
		if (divisionName.equals(corporateer.getMainDivision().getName())) {
			return false;
		}

		// "none" is also used for departments, choose the general division instead
		if (divisionName.equals("none")) {
			division = objectService.getDefaultDivision();
			actionLogService.logAction(authentication, "Removed main division");

		} else {
			division = objectService.getDivisionByName(divisionName);
			actionLogService.logAction(authentication, "Set main division to " + division.getName());
		}

		corporateer.setMainDivision(division);
		corporateerRepository.save(corporateer);
		return true;
	}

	public boolean buyRank(String username, String rankName) {

		// TODO: validations

		Corporateer corporateer = userHandlingService.getUserByName(username).getCorporateer();
		Rank rank = objectService.getRankByName(rankName);
		Influence generalInfluence = influenceHandlingService.getInfluenceByCorporateerAndDivisionAndType(corporateer,
				objectService.getDefaultDivision(), objectService.getInfluenceTypeId(1L));

		// only allow buying a higher rank
		if (corporateer.getRank().getLevel() >= rank.getLevel()) {
			System.out.println("level error");
			return false;
		}

		// only allow buying when corporateer has enough general influence
		if (generalInfluence.getAmount() < rank.getInfluenceToBuy()) {
			System.out.println("influence error");
			System.out.println(generalInfluence.getAmount());
			return false;
		}

		// set new rank
		corporateer.setRank(rank);

		// deduct general influence
		generalInfluence.setAmount(generalInfluence.getAmount() - rank.getInfluenceToBuy());
		corporateer.setTotalInfluence(getTotalInfluence(corporateer));
		corporateerRepository.save(corporateer);
		return true;
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
		types = objectService.getAllInfluenceTypes();
	}

	/**
	 * Refreshes list of division from repository
	 */
	private void refreshDivisions() {
		divisions = objectService.getAllDivisions();
	}

	/**
	 * Refreshes list of corporateers from repository
	 */
	private void refreshCorporateers() {
		corporateers = corporateerRepository.findAll();
	}

}
