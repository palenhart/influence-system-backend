package com.thecorporateer.influence.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.exceptions.IllegalBuyRequestException;
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
			corporateer.setTributes(currentTributes + tributesToAdd);
			corporateersToSave.add(corporateer);
		}

		corporateerRepository.save(corporateersToSave);
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

	// TODO: Only allow a subset of divisions if user is able to do change himself
	public void setMainDivision(Authentication authentication, String divisionName) {

		Corporateer corporateer = userHandlingService.getUserByName(authentication.getName()).getCorporateer();
		Division division;

		// do not change division when there is other division selected
		if (divisionName.equals(corporateer.getMainDivision().getName())) {
			throw new IllegalDivisionChangeRequestException("Division was not changed.");
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
	}
	
	private void addCorporateerToDivision(Corporateer corporateer, Division division) {
		
		// don't do anything if corporateer is already a member of division
		if (corporateer.getMemberOfDivisions().contains(division)) {
			throw new IllegalMembershipChangeException(corporateer.getName() + " already is a member of division " + division.getName());
		}
		
		else {
			corporateer.getMemberOfDivisions().add(division);
		}
	}

	private void removeCorporateerFromDivision(Corporateer corporateer, Division division) {
		
		// don't do anything if corporateer is NOT a member of division
		if (!corporateer.getMemberOfDivisions().contains(division)) {
			throw new IllegalMembershipChangeException(corporateer.getName() + " is not a member of division " + division.getName());
		}
		
		else {
			corporateer.getMemberOfDivisions().remove(division);
		}
	}
	
	public void changeCorporateerDivisionMembership(Authentication authentication, String corporateerName, String divisionName, Boolean add) {
		
		if (accessHandlingService.accessPermissionVerifier(authentication, objectService.getDivisionByName(divisionName))) {
			
			// If 'add' boolean is 1, try to add corporateer to division
			if (add) {
				addCorporateerToDivision(getCorporateerByName(corporateerName), objectService.getDivisionByName(divisionName));
			}
			
			// If 'add boolean is 0, try to remove corporateer from division
			else {
				removeCorporateerFromDivision(getCorporateerByName(corporateerName), objectService.getDivisionByName(divisionName));
			}
		}
	}
	
	public void buyRank(Authentication authentication, String rankName) {

		// TODO: validations

		Corporateer corporateer = userHandlingService.getUserByName(authentication.getName()).getCorporateer();
		Rank rank = objectService.getRankByName(rankName);
		Influence generalInfluence = influenceHandlingService.getInfluenceByCorporateerAndDivisionAndType(corporateer,
				objectService.getDefaultDivision(), objectService.getInfluenceTypeById(1L));

		// only allow buying a higher rank
		if (corporateer.getRank().getLevel() >= rank.getLevel()) {
			throw new IllegalBuyRequestException("Lower rank cannot be bought.");
		}

		// only allow buying the next higher rank
		if (corporateer.getRank().getLevel() - rank.getLevel() > 1) {
			throw new IllegalBuyRequestException("Only the next rank can be bought.");
		}

		// only allow buying when corporateer has enough general influence
		if (generalInfluence.getAmount() < rank.getInfluenceToBuy()) {
			throw new IllegalBuyRequestException("Not enough influence to buy rank.");
		}

		// set new rank
		corporateer.setRank(rank);

		// deduct general influence
		generalInfluence.setAmount(generalInfluence.getAmount() - rank.getInfluenceToBuy());
		corporateer.setTotalInfluence(getTotalInfluence(corporateer));
		corporateerRepository.save(corporateer);
	}

	public void setRank(String corporateerName, String rankName) {

		Corporateer corporateer = getCorporateerByName(corporateerName);
		corporateer.setRank(objectService.getRankByName(rankName));
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

		corporateers = corporateerRepository.findAll();
	}

}
