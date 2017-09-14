package com.thecorporateer.influence.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.exceptions.InfluenceNotFoundException;
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

	public Influence updateInfluence(Influence influence) {

		return influenceRepository.save(influence);
	}

	public List<Influence> updateInfluences(List<Influence> influences) {

		return influenceRepository.save(influences);
	}

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

			Influence generalInfluence = getInfluenceByCorporateerAndDivisionAndType(influence.getCorporateer(),
					objectService.getDefaultDivision(), influence.getType());

			createConversion(influence, generalInfluence, amount);

			generalInfluence.setAmount(generalInfluence.getAmount() + amount);
			influence.setAmount(influence.getAmount() - amount);
			updateInfluence(generalInfluence);
			updateInfluence(influence);

			return true;

		}
		// convert division influence to department influence
		Influence departmentInfluence = getInfluenceByCorporateerAndDivisionAndType(influence.getCorporateer(),
				objectService.getDivisionByNameAndDepartment("none", influence.getDivision().getDepartment()),
				influence.getType());

		createConversion(influence, departmentInfluence, amount);

		departmentInfluence.setAmount(departmentInfluence.getAmount() + amount);
		influence.setAmount(influence.getAmount() - amount);
		updateInfluence(departmentInfluence);
		updateInfluence(influence);

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
		objectService.saveConversion(conversion);
	}

}
