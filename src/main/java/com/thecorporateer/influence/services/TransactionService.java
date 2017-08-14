package com.thecorporateer.influence.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Department;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.objects.InfluenceType;
import com.thecorporateer.influence.objects.Transaction;
import com.thecorporateer.influence.repositories.CorporateerRepository;
import com.thecorporateer.influence.repositories.DepartmentRepository;
import com.thecorporateer.influence.repositories.DivisionRepository;
import com.thecorporateer.influence.repositories.InfluenceRepository;
import com.thecorporateer.influence.repositories.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private InfluenceRepository influenceRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private CorporateerRepository corporateerRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private DivisionRepository divisionRepository;

	@Autowired
	CorporateerHandlingService corporateerHandlingService;

	public boolean transfer(Corporateer sender, Corporateer receiver, String message, int amount, InfluenceType type) {
		if (!validate(sender, receiver, amount)) {
			return false;
		}
		Division senderMainDivision = sender.getMainDivision();

		Department influenceDepartment;
		Division influenceDivision;

		if (senderMainDivision.getId().equals(receiver.getMainDivision().getId())) {
			influenceDivision = senderMainDivision;
			influenceDepartment = influenceDivision.getDepartment();
		} else if (senderMainDivision.getDepartment().getId()
				.equals(receiver.getMainDivision().getDepartment().getId())) {
			influenceDivision = divisionRepository.findOne(1L);
			influenceDepartment = senderMainDivision.getDepartment();
		} else {
			influenceDivision = divisionRepository.findOne(1L);
			influenceDepartment = departmentRepository.findOne(1L);
		}

		List<Influence> influence = new ArrayList<>();
		influence.add(influenceRepository.findByCorporateerAndDepartmentAndDivisionAndType(receiver,
				influenceDepartment, influenceDivision, type));
		influence.add(influenceRepository.findByCorporateerAndDepartmentAndDivisionAndType(sender, influenceDepartment,
				influenceDivision, type));
		influence.get(0).setAmount(influence.get(0).getAmount() + amount);
		influence.get(1).setAmount(influence.get(1).getAmount() + amount);
		influenceRepository.save(influence);

		Transaction trans = new Transaction();
		trans.setTimestamp(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
		trans.setType(type);
		trans.setAmount(amount);
		trans.setMessage(message);
		trans.setSender(sender);
		trans.setReceiver(receiver);
		trans.setDivision(influenceDivision);
		trans.setDepartment(influenceDepartment);
		transactionRepository.save(trans);

		sender.setTributes(sender.getTributes() - amount);
		if (type.getId().equals(1L)) {
			sender.setTotalInfluence(corporateerHandlingService.getTotalInfluence(sender));
			receiver.setTotalInfluence(corporateerHandlingService.getTotalInfluence(receiver));
			receiver = corporateerRepository.save(receiver);
		}
		sender = corporateerRepository.save(sender);

		return true;
	}

	private boolean validate(Corporateer sender, Corporateer receiver, int amount) {
		if (amount < 1) {
			return false;
		}
		if (sender.getTributes() < amount) {
			return false;
		} else if (sender.getId() == receiver.getId()) {
			return false;
		}

		return true;
	}

}
