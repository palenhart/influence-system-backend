package com.thecorporateer.influence.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.exceptions.IllegalInfluenceConversionException;
import com.thecorporateer.influence.exceptions.InfluenceNotFoundException;
import com.thecorporateer.influence.exceptions.NotEnoughInfluenceException;
import com.thecorporateer.influence.objects.Conversion;
import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.objects.InfluenceType;
import com.thecorporateer.influence.repositories.InfluenceRepository;

/**
 * @author Zollak
 * 
 *         Service handling action concerning influence entities
 *
 */
@Service
public class InfluenceHandlingService {

	@Autowired
	private InfluenceRepository influenceRepository;

	@Autowired
	private UserHandlingService userHandlingService;
	@Autowired
	private ObjectService objectService;

	public Influence getInfluenceByCorporateerAndDivisionAndType(Corporateer corporateer, Division division,
			InfluenceType influencetype) {

		Influence influence = influenceRepository.findByCorporateerAndDivisionAndType(corporateer, division,
				influencetype);

		if (influence == null) {
			throw new InfluenceNotFoundException();
		}

		return influence;
	}

	private void updateInfluence(Influence influence) {

		influenceRepository.save(influence);
	}

	public List<Influence> updateInfluences(List<Influence> influences) {

		return influenceRepository.save(influences);
	}

	public void convertInfluence(Authentication authentication, String divisionName, String departmentName, int amount,
			boolean toGeneral) {

		Influence influenceToConvert = getInfluenceByCorporateerAndDivisionAndType(
				userHandlingService.getUserByName(authentication.getName()).getCorporateer(),
				objectService.getDivisionByNameAndDepartment(divisionName,
						objectService.getDepartmentByName(departmentName)),
				objectService.getInfluenceTypeById(1L));

		// do not convert demerits
		if (influenceToConvert.getType().getId() != 1L) {
			throw new IllegalInfluenceConversionException();
		}

		// do not convert general influence
		if (influenceToConvert.getDivision().getDepartment().getId() == 1L) {
			throw new IllegalInfluenceConversionException();
		}

		// do not convert more influence than available
		if (influenceToConvert.getAmount() < amount) {
			throw new NotEnoughInfluenceException();
		}

		// convert department influence to general influence
		if (toGeneral || influenceToConvert.getDivision().getId() <= 8L) {

			Influence generalInfluence = getInfluenceByCorporateerAndDivisionAndType(influenceToConvert.getCorporateer(),
					objectService.getDefaultDivision(), influenceToConvert.getType());

			createConversion(influenceToConvert, generalInfluence, amount);

			generalInfluence.setAmount(generalInfluence.getAmount() + amount);
			influenceToConvert.setAmount(influenceToConvert.getAmount() - amount);
			updateInfluence(generalInfluence);
			updateInfluence(influenceToConvert);

			return;

		}

		// convert division influence to department influence
		Influence departmentInfluence = getInfluenceByCorporateerAndDivisionAndType(influenceToConvert.getCorporateer(),
				objectService.getDivisionByNameAndDepartment("none", influenceToConvert.getDivision().getDepartment()),
				influenceToConvert.getType());

		createConversion(influenceToConvert, departmentInfluence, amount);

		departmentInfluence.setAmount(departmentInfluence.getAmount() + amount);
		influenceToConvert.setAmount(influenceToConvert.getAmount() - amount);
		updateInfluence(departmentInfluence);
		updateInfluence(influenceToConvert);

	}

	private void createConversion(Influence influence, Influence genralizedInfluence, int amount) {
		
		Conversion conversion = new Conversion();
		conversion.setTimestamp(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
		conversion.setCorporateer(influence.getCorporateer());
		conversion.setFromDivision(influence.getDivision());
		conversion.setToDivision(genralizedInfluence.getDivision());
		conversion.setType(influence.getType());
		conversion.setAmount(amount);
		
		objectService.saveConversion(conversion);
	}

}
