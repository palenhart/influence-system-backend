package com.thecorporateer.influence.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.objects.Conversion;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.repositories.ConversionRepository;
import com.thecorporateer.influence.repositories.DepartmentRepository;
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
	private DepartmentRepository departmentRepository;
	@Autowired
	private DivisionRepository divisionRepository;
	@Autowired
	private ConversionRepository conversionRepository;

	/**
	 * @param influence
	 * @return
	 */
	public boolean convertInfluenceToGeneral(Influence influence) {
		// do not convert demerits
		if (influence.getType().getId() != 1L) {
			return false;
		}

		// do not convert influence which is already general
		if (influence.getDepartment().getId() == 1L) {
			return false;
		}

		Influence generalInfluence = influenceRepository.findByCorporateerAndDepartmentAndDivisionAndType(
				influence.getCorporateer(), departmentRepository.findOne(1L), divisionRepository.findOne(1L),
				influence.getType());

		createConversion(influence, generalInfluence);

		generalInfluence.setAmount(generalInfluence.getAmount() + influence.getAmount());
		influence.setAmount(0);
		influenceRepository.save(generalInfluence);
		influenceRepository.save(influence);

		return true;
	}

	/**
	 * @param influence
	 * @return
	 */
	public boolean convertInfluenceToDepartment(Influence influence) {
		// do not convert demerits
		if (influence.getType().getId() != 1L) {
			return false;
		}

		// do not convert influence which is not division specific
		if (influence.getDivision().getId() == 1L) {
			return false;
		}

		Influence departmentInfluence = influenceRepository.findByCorporateerAndDepartmentAndDivisionAndType(
				influence.getCorporateer(), influence.getDepartment(), divisionRepository.findOne(1L),
				influence.getType());

		createConversion(influence, departmentInfluence);

		departmentInfluence.setAmount(departmentInfluence.getAmount() + influence.getAmount());
		influence.setAmount(0);
		influenceRepository.save(departmentInfluence);
		influenceRepository.save(influence);

		return true;
	}

	private void createConversion(Influence influence, Influence genralizedInfluence) {
		Conversion conversion = new Conversion();
		conversion.setTimestamp(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
		conversion.setCorporateer(influence.getCorporateer());
		conversion.setFromDepartment(influence.getDepartment());
		conversion.setFromDivision(influence.getDivision());
		conversion.setToDepartment(genralizedInfluence.getDepartment());
		conversion.setToDivision(genralizedInfluence.getDivision());
		conversion.setType(influence.getType());
		conversion.setAmount(influence.getAmount());
		conversionRepository.save(conversion);
	}

}
