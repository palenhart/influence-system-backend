package com.thecorporateer.influence.test.transactions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.InfluenceType;
import com.thecorporateer.influence.repositories.CorporateerRepository;
import com.thecorporateer.influence.repositories.DepartmentRepository;
import com.thecorporateer.influence.repositories.DivisionRepository;
import com.thecorporateer.influence.repositories.InfluenceRepository;
import com.thecorporateer.influence.repositories.TransactionRepository;
import com.thecorporateer.influence.services.TransactionService;

public class TestCreateTransaction {

	@Mock
	private InfluenceRepository influenceRepository;
	@Mock
	private TransactionRepository transactionRepository;
	@Mock
	private CorporateerRepository corporateerRepository;
	@Mock
	private DepartmentRepository departmentRepository;
	@Mock
	private DivisionRepository divisionRepository;
	@Mock
	private Corporateer sender;
	@Mock
	private Corporateer receiver;
	@Mock
	private InfluenceType type;

	private String message = "";
	private int amount = 1;

	@InjectMocks
	private TransactionService transactionService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidatorWithFalseAmount() {
		when(sender.getTributes()).thenReturn(5);
		amount = 10;
		assertFalse("Amount too high was not detected!",
				transactionService.transfer(sender, receiver, message, amount, type));
	}

	@Test
	public void testValidatorWithIdenticalUser() {
		when(sender.getId()).thenReturn(1L);
		when(receiver.getId()).thenReturn(1L);
		assertFalse("Identical user was not detected!",
				transactionService.transfer(sender, receiver, message, amount, type));
	}

}
