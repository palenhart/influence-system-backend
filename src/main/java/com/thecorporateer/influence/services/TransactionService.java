package com.thecorporateer.influence.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.objects.InfluenceType;
import com.thecorporateer.influence.objects.Transaction;
import com.thecorporateer.influence.repositories.TransactionRepository;

/**
 * @author Zollak
 * 
 *         Service handling influence transfers
 *
 */
@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private CorporateerHandlingService corporateerHandlingService;
	@Autowired
	private InfluenceHandlingService influenceHandlingService;
	@Autowired
	private ObjectService objectService;
	
	public List<Transaction> getAllTransactions(){
		
		return transactionRepository.findAll();
	}

	/**
	 * 
	 * Transfer influence from one corporateer to another
	 * 
	 * @param sender
	 *            The corporateer sending influence
	 * @param receiver
	 *            The corporateer receiving influence
	 * @param message
	 *            A message to be sent with the influence
	 * @param amount
	 *            The amount of influence to be sent
	 * @param type
	 *            The type of influence to be sent
	 * @return <code>true</code> if the transaction was successful;
	 *         <code>false</code> otherwise
	 */
	public boolean transfer(Corporateer sender, Corporateer receiver, String message, int amount, InfluenceType type) {
		if (!validate(sender, receiver, amount)) {
			return false;
		}
		Division senderMainDivision = sender.getMainDivision();

		Division influenceDivision;

		if (senderMainDivision.getId().equals(receiver.getMainDivision().getId())) {
			influenceDivision = senderMainDivision;
		} else if (senderMainDivision.getDepartment().getId()
				.equals(receiver.getMainDivision().getDepartment().getId())) {
			influenceDivision = objectService.getDivisionByNameAndDepartment("none",
					senderMainDivision.getDepartment());
		} else {
			influenceDivision = objectService.getDefaultDivision();
		}

		List<Influence> influences = new ArrayList<>();
		influences.add(influenceHandlingService.getInfluenceByCorporateerAndDivisionAndType(receiver, influenceDivision,
				type));
		influences.add(
				influenceHandlingService.getInfluenceByCorporateerAndDivisionAndType(sender, influenceDivision, type));
		influences.get(0).setAmount(influences.get(0).getAmount() + amount);
		influences.get(1).setAmount(influences.get(1).getAmount() + amount);
		influenceHandlingService.updateInfluences(influences);

		Transaction trans = new Transaction();
		trans.setTimestamp(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
		trans.setType(type);
		trans.setAmount(amount);
		trans.setMessage(message);
		trans.setSender(sender);
		trans.setReceiver(receiver);
		trans.setDivision(influenceDivision);
		trans.setReceivingDivision(receiver.getMainDivision());
		transactionRepository.save(trans);

		sender.setTributes(sender.getTributes() - amount);
		if (type.getId().equals(1L)) {
			sender.setTotalInfluence(corporateerHandlingService.getTotalInfluence(sender));
			sender.setLifetimeInfluence(sender.getLifetimeInfluence() + amount);
			receiver.setTotalInfluence(corporateerHandlingService.getTotalInfluence(receiver));
			receiver.setLifetimeInfluence(receiver.getLifetimeInfluence() + amount);
			receiver = corporateerHandlingService.updateCorporateer(receiver);
		}
		sender = corporateerHandlingService.updateCorporateer(sender);

		return true;
	}

	/**
	 * 
	 * Validate transaction request
	 * 
	 * @param sender
	 *            The corporateer sending influence
	 * @param receiver
	 *            The corporateer receiving influence
	 * @param amount
	 *            The amount of influence to be sent
	 * @return <code>true</code> if the transaction request does not violate any
	 *         constraints; <code>false</code> otherwise
	 */
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
