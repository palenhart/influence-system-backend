package com.thecorporateer.influence.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.objects.Conversion;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.repositories.ConversionRepository;
import com.thecorporateer.influence.repositories.DivisionRepository;
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
	private DivisionRepository divisionRepository;
	@Autowired
	private ConversionRepository conversionRepository;

	public boolean convertInfluence(Influence influence, int amount, boolean toGeneral) {

		// do not convert general influence
		if (influence.getDivision().getDepartment().getId() == 1L) {
			return false;
		}

		// do not convert more influence than available
		if (influence.getAmount() < amount) {
			return false;
		}

		// convert department influence to general influence
		if (toGeneral || influence.getDivision().getId() <= 9L) {

			Influence generalInfluence = influenceRepository.findByCorporateerAndDivisionAndType(
					influence.getCorporateer(), divisionRepository.findOne(1L), influence.getType());

			createConversion(influence, generalInfluence, amount);

			generalInfluence.setAmount(generalInfluence.getAmount() + amount);
			influence.setAmount(influence.getAmount() - amount);
			influenceRepository.save(generalInfluence);
			influenceRepository.save(influence);

			return true;

		}
		// convert division influence to department influence
		Influence departmentInfluence = influenceRepository.findByCorporateerAndDivisionAndType(
				influence.getCorporateer(),
				divisionRepository.findByNameAndDepartment("none", influence.getDivision().getDepartment()),
				influence.getType());

		createConversion(influence, departmentInfluence, amount);

		departmentInfluence.setAmount(departmentInfluence.getAmount() + amount);
		influence.setAmount(influence.getAmount() - amount);
		influenceRepository.save(departmentInfluence);
		influenceRepository.save(influence);

		return true;

	}

	private void createConversion(Influence influence, Influence genralizedInfluence, int amount) {
		Conversion conversion = new Conversion();
		conversion.setTimestamp(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
		conversion.setCorporateer(influence.getCorporateer());
		conversion.setFromDivision(influence.getDivision());
		conversion.setToDivision(genralizedInfluence.getDivision());
		conversion.setType(influence.getType());
		conversion.setAmount(amount);
		conversionRepository.save(conversion);
	}

}
