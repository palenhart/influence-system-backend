package com.thecorporateer.influence.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.exceptions.IllegalDivisionChangeRequestException;
import com.thecorporateer.influence.exceptions.IllegalMembershipChangeException;
import com.thecorporateer.influence.exceptions.RepositoryNotFoundException;
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
	@Autowired
	private AccessHandlingService accessHandlingService;

	// private List<Rank> ranks = new ArrayList<>();
	private List<InfluenceType> types = new ArrayList<>();
	private List<Division> divisions = new ArrayList<>();
	private List<Corporateer> corporateers = new ArrayList<>();

	public Corporateer getCorporateerByName(String name) {

		Corporateer corporateer = corporateerRepository.findByName(name);

		if (corporateer == null) {
			throw new RepositoryNotFoundException("Corporateer not found.");
		}

		return corporateer;
	}

	public List<Corporateer> getAllCorporateers() {

		List<Corporateer> corporateers = corporateerRepository.findAll();

		if (corporateers == null) {
			throw new RepositoryNotFoundException("Corporateers not found.");
		}

		return corporateers;
	}

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

		List<Division> divisions = new ArrayList<Division>();
		divisions.add(objectService.getDefaultDivision());
		corporateer.setMemberOfDivisions(divisions);

		corporateer = updateCorporateer(corporateer);

		initializeInfluenceTable(corporateer);
	}

	public void createCorporateerWithDivisions(String name, Division mainDivision, List<String> divisionNames) {

		Corporateer corporateer = new Corporateer();
		corporateer.setName(name);
		corporateer.setRank(objectService.getLowestRank());
		corporateer.setMainDivision(mainDivision);

		List<Division> divisions = new ArrayList<Division>();
		for (String divName : divisionNames) {
			divisions.add(objectService.getDivisionByName(divName));
		}
		corporateer.setMemberOfDivisions(divisions);

		corporateer.setTributes(50);

		corporateer = updateCorporateer(corporateer);

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
				influences.add(new Influence(corporateer, division, type, 0));
			}
		}

		influenceHandlingService.updateInfluences(influences);
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
			corporateer.setTributes((int) Math.floor(Math.min(1.5 * tributesToAdd, currentTributes + tributesToAdd)));
			corporateersToSave.add(corporateer);
		}

		corporateerRepository.saveAll(corporateersToSave);
	}

	public int getTotalInfluence(Corporateer corporateer) {

		int total = 0;

		for (Influence influence : corporateer.getInfluence()) {
			if (influence.getType().getId().equals(1L)) {
				total = total + influence.getAmount();
			}
		}

		return total;
	}

	public void setMainDivision(Authentication authentication, String divisionName) {

		Corporateer corporateer = userHandlingService.getUserByName(authentication.getName()).getCorporateer();
		Division division;

		// do not change division when there is no other division selected
		//
		// or when there is already one selected (fix for elections)
		//
		// if (divisionName.equals(corporateer.getMainDivision().getName())
		// || !corporateer.getMainDivision().equals(objectService.getDefaultDivision()))
		// {
		// throw new IllegalDivisionChangeRequestException("Division was not changed.");
		// }
		if (divisionName.equals(corporateer.getMainDivision().getName())) {
			throw new IllegalDivisionChangeRequestException("Division was not changed.");
		}

		// "none" is also used for departments, choose the general division instead
		if (divisionName.equals("none")) {
			division = objectService.getDefaultDivision();
			actionLogService.logAction(authentication, "Removed main division");
		}

		else {
			division = objectService.getDivisionByName(divisionName);
			// do not change division to a division that corporateer is not a member of
			if (!corporateer.getMemberOfDivisions().contains(division)) {
				throw new IllegalDivisionChangeRequestException(
						corporateer.getName() + " is not a member of division " + division.getName());
			}
			actionLogService.logAction(authentication, "Set main division to " + division.getName());
		}

		corporateer.setMainDivision(division);
		updateCorporateer(corporateer);
	}

	private void addCorporateerToDivision(Corporateer corporateer, Division division) {

		// don't do anything if corporateer is already a member of division
		if (corporateer.getMemberOfDivisions().contains(division)) {
			throw new IllegalMembershipChangeException(
					corporateer.getName() + " already is a member of division " + division.getName());
		}

		else {
			corporateer.getMemberOfDivisions().add(division);
		}
	}

	private void removeCorporateerFromDivision(Corporateer corporateer, Division division) {

		// don't do anything if corporateer is NOT a member of division
		if (!corporateer.getMemberOfDivisions().contains(division)) {
			throw new IllegalMembershipChangeException(
					corporateer.getName() + " is not a member of division " + division.getName());
		}

		else {
			corporateer.getMemberOfDivisions().remove(division);
		}
	}

	public void changeCorporateerDivisionMembership(Authentication authentication, String corporateerName,
			String divisionName, Boolean add) {

		if (accessHandlingService.accessPermissionVerifier(authentication,
				objectService.getDivisionByName(divisionName))) {

			// If 'add' boolean is 1, try to add corporateer to division
			if (add) {
				addCorporateerToDivision(getCorporateerByName(corporateerName),
						objectService.getDivisionByName(divisionName));
			}

			// If 'add boolean is 0, try to remove corporateer from division
			else {
				removeCorporateerFromDivision(getCorporateerByName(corporateerName),
						objectService.getDivisionByName(divisionName));
			}
		}
	}

	public boolean increaseRank(Corporateer corporateer) {

		// only increase when a higher rank exists
		if (corporateer.getRank().getRankLevel() >= objectService.getHighestRank().getRankLevel()) {
			return false;
		}

		// get next higher rank
		Rank nextRank = objectService.getRankByLevel(corporateer.getRank().getRankLevel() + 1);

		// only allow obtaining next rank when corporateer has enough lifetime influence
		if (corporateer.getLifetimeInfluence() < nextRank.getInfluenceToObtain()) {
			return false;
		}

		// set new rank
		corporateer.setRank(nextRank);
		updateCorporateer(corporateer);

		actionLogService.logAction(null, "Set rank of " + corporateer.getName() + " to " + nextRank.getName() + ".");

		return true;
	}

	public void setRank(String corporateerName, String rankName) {

		Corporateer corporateer = getCorporateerByName(corporateerName);
		Rank rank = objectService.getRankByName(rankName);
		setRank(corporateer, rank);
	}

	private void setRank(Corporateer corporateer, Rank rank) {

		corporateer.setRank(rank);
		updateCorporateer(corporateer);
	}

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

		corporateers = getAllCorporateers();
	}

}
