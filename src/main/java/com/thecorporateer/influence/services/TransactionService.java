package com.thecorporateer.influence.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.exceptions.IllegalTransferRequestException;
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
	private UserHandlingService userHandlingService;
	@Autowired
	private InfluenceHandlingService influenceHandlingService;
	@Autowired
	private ObjectService objectService;

	public List<Transaction> getAllTransactions() {

		return transactionRepository.findAll();
	}

	/**
	 * 
	 * Transfer influence from one corporateer to another
	 * 
	 * @param senderAuth
	 *            Authentication of the user sending influence
	 * @param receiverName
	 *            Name of the corporateer receiving influence
	 * @param message
	 *            A message to be sent with the influence
	 * @param amount
	 *            The amount of influence to be sent
	 * @param influenceTypeName
	 *            The type of influence to be sent
	 */
	public void transfer(Corporateer sender, String receiverName, String message, int amount,
			String influenceTypeName) {

		Corporateer receiver = corporateerHandlingService.getCorporateerByName(receiverName);
		InfluenceType type = objectService.getInfluenceTypeByName(influenceTypeName);

		validate(sender, receiver, amount, message);

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
			corporateerHandlingService.increaseRank(sender);
			corporateerHandlingService.increaseRank(receiver);
			return;
		}

		corporateerHandlingService.updateCorporateer(sender);
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
	private void validate(Corporateer sender, Corporateer receiver, int amount, String message) {

		if (amount < 1) {
			throw new IllegalTransferRequestException("Illegal transfer amount.");
		}

		if (sender.getTributes() < amount) {
			throw new IllegalTransferRequestException("Not enough tributes.");
		}

		if (sender.getId() == receiver.getId()) {
			throw new IllegalTransferRequestException("Sender and receiver cannot be the same.");
		}

		if (message == null || message.trim().isEmpty()) {
			throw new IllegalTransferRequestException("No message set.");
		}
	}

	public void botTransfer(String senderName, String receiverName, Integer amount, String influenceTypeName) {
		Corporateer sender = corporateerHandlingService.getCorporateerByName(senderName);

		transfer(sender, receiverName, "Transaction handled by bot.", amount, influenceTypeName);

	}

	public void userTransfer(Authentication authentication, String receiverName, String message, Integer amount,
			String influenceTypeName) {
		Corporateer sender = userHandlingService.getUserByName(authentication.getName()).getCorporateer();

		transfer(sender, receiverName, message, amount, influenceTypeName);

	}

}
