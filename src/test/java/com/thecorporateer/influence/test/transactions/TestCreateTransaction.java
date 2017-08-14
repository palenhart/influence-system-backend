package com.thecorporateer.influence.test.transactions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
import com.thecorporateer.influence.services.TransactionService;

public class TestCreateTransaction {

	@Mock
	private InfluenceRepository mockInfluenceRepository;
	@Mock
	private TransactionRepository mockTransactionRepository;
	@Mock
	private CorporateerRepository mockCorporateerRepository;
	@Mock
	private DepartmentRepository mockDepartmentRepository;
	@Mock
	private DivisionRepository mockDivisionRepository;
	@Mock
	private Corporateer mockSender;
	@Mock
	private Corporateer mockReceiver;
	@Mock
	private InfluenceType mockType;
	@Mock
	private Department mockDepartment;
	@Mock
	private Division mockDivision;

	@Captor
	private ArgumentCaptor<Transaction> transactionCaptor;

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

	/**
	 * Test whether validator function detects wrong transaction amounts
	 */
	@Test
	public void testValidatorWithFalseAmount() {
		// sender has not enough tributes
		when(mockSender.getTributes()).thenReturn(5);
		amount = 10;

		// sender and receiver are not the same user
		when(mockSender.getId()).thenReturn(1L);
		when(mockReceiver.getId()).thenReturn(2L);

		assertFalse("Amount too high was not detected!",
				transactionService.transfer(mockSender, mockReceiver, message, amount, mockType));

		// transaction amount is zero
		amount = 0;

		assertFalse("Amount zero was not detected!",
				transactionService.transfer(mockSender, mockReceiver, message, amount, mockType));

		// transaction amount is negative
		amount = -1;

		assertFalse("Amount negative was not detected!",
				transactionService.transfer(mockSender, mockReceiver, message, amount, mockType));

		// verify no interactions with repositories happen
		verifyZeroInteractions(mockDivisionRepository);
		verifyZeroInteractions(mockDepartmentRepository);
		verifyZeroInteractions(mockInfluenceRepository);
		verifyZeroInteractions(mockTransactionRepository);
		verifyZeroInteractions(mockCorporateerRepository);
	}

	/**
	 * Test whether validator function detects sending to the sender user
	 */
	@Test
	public void testValidatorWithIdenticalUser() {
		// sender has enough tributes
		when(mockSender.getTributes()).thenReturn(5);
		amount = 5;

		// sender and receiver are the same user
		when(mockSender.getId()).thenReturn(1L);
		when(mockReceiver.getId()).thenReturn(1L);

		assertFalse("Identical user was not detected!",
				transactionService.transfer(mockSender, mockReceiver, message, amount, mockType));

		// verify no interactions with repositories happen
		verifyZeroInteractions(mockDivisionRepository);
		verifyZeroInteractions(mockDepartmentRepository);
		verifyZeroInteractions(mockInfluenceRepository);
		verifyZeroInteractions(mockTransactionRepository);
		verifyZeroInteractions(mockCorporateerRepository);
	}

	/**
	 * Test transaction between members of the same division
	 */
	@Test
	public void testTransactionSameDivision() {
		// sender has enough tributes
		when(mockSender.getTributes()).thenReturn(5);
		amount = 5;

		// sender and receiver are not the same user
		when(mockSender.getId()).thenReturn(1L);
		when(mockReceiver.getId()).thenReturn(2L);

		// sender and receiver are in the same division
		when(mockSender.getMainDivision()).thenReturn(mockDivision);
		when(mockReceiver.getMainDivision()).thenReturn(mockDivision);
		when(mockDivision.getDepartment()).thenReturn(mockDepartment);

		// return mock influence to get changed
		when(mockInfluenceRepository.findByCorporateerAndDepartmentAndDivisionAndType(any(), any(), any(), any()))
				.thenReturn(mock(Influence.class));
		assertTrue(transactionService.transfer(mockSender, mockReceiver, message, amount, mockType));

		// verify correct influence is accessed
		verify(mockInfluenceRepository).findByCorporateerAndDepartmentAndDivisionAndType(mockReceiver, mockDepartment,
				mockDivision, mockType);
		verify(mockInfluenceRepository).findByCorporateerAndDepartmentAndDivisionAndType(mockSender, mockDepartment,
				mockDivision, mockType);

		// verify transaction gets saved correctly
		verify(mockTransactionRepository).save(transactionCaptor.capture());
		assertEquals("Wrong influence type saved in transaction!", mockType, transactionCaptor.getValue().getType());
		assertEquals("Wrong amount saved in transaction!", amount, transactionCaptor.getValue().getAmount());
		assertEquals("Wrong message saved in transaction!", message, transactionCaptor.getValue().getMessage());
		assertEquals("Wrong sender saved in transaction!", mockSender, transactionCaptor.getValue().getSender());
		assertEquals("Wrong receiver saved in transaction!", mockReceiver, transactionCaptor.getValue().getReceiver());
		assertEquals("Wrong division saved in transaction!", mockDivision, transactionCaptor.getValue().getDivision());
		assertEquals("Wrong department saved in transaction!", mockDepartment,
				transactionCaptor.getValue().getDepartment());

		// verify sender gets saved
		verify(mockCorporateerRepository).save(mockSender);
	}

	/**
	 * Test transaction between members of different divisions in the same
	 * department
	 */
	@Test
	public void testTransactionSameDepartmentDifferentDivision() {
		// sender has enough tributes
		when(mockSender.getTributes()).thenReturn(5);
		amount = 5;

		// sender and receiver are not the same user
		when(mockSender.getId()).thenReturn(1L);
		when(mockReceiver.getId()).thenReturn(2L);

		// sender and receiver are not the same division but in the same department
		Division mockAnotherDivision = mock(Division.class);
		when(mockSender.getMainDivision()).thenReturn(mockDivision);
		when(mockDivision.getId()).thenReturn(1L);
		when(mockReceiver.getMainDivision()).thenReturn(mockAnotherDivision);
		when(mockAnotherDivision.getId()).thenReturn(2L);
		when(mockDivision.getDepartment()).thenReturn(mockDepartment);
		when(mockAnotherDivision.getDepartment()).thenReturn(mockDepartment);
		Division mockNoDivision = mock(Division.class);
		when(mockDivisionRepository.findOne(1L)).thenReturn(mockNoDivision);

		// return mock influence to get changed
		when(mockInfluenceRepository.findByCorporateerAndDepartmentAndDivisionAndType(any(), any(), any(), any()))
				.thenReturn(mock(Influence.class));

		// transfer influence
		assertTrue(transactionService.transfer(mockSender, mockReceiver, message, amount, mockType));

		// verify correct influence is accessed
		verify(mockInfluenceRepository).findByCorporateerAndDepartmentAndDivisionAndType(mockReceiver, mockDepartment,
				mockNoDivision, mockType);
		verify(mockInfluenceRepository).findByCorporateerAndDepartmentAndDivisionAndType(mockSender, mockDepartment,
				mockNoDivision, mockType);

		// verify transaction gets saved correctly
		verify(mockTransactionRepository).save(transactionCaptor.capture());
		assertEquals("Wrong influence type saved in transaction!", mockType, transactionCaptor.getValue().getType());
		assertEquals("Wrong amount saved in transaction!", amount, transactionCaptor.getValue().getAmount());
		assertEquals("Wrong message saved in transaction!", message, transactionCaptor.getValue().getMessage());
		assertEquals("Wrong sender saved in transaction!", mockSender, transactionCaptor.getValue().getSender());
		assertEquals("Wrong receiver saved in transaction!", mockReceiver, transactionCaptor.getValue().getReceiver());
		assertEquals("Wrong division saved in transaction!", mockNoDivision,
				transactionCaptor.getValue().getDivision());
		assertEquals("Wrong department saved in transaction!", mockDepartment,
				transactionCaptor.getValue().getDepartment());

		// verify sender gets saved
		verify(mockCorporateerRepository).save(mockSender);
	}

	/**
	 * Test transaction between members of different departments
	 */
	@Test
	public void testTransactionDifferentDepartment() {
		// sender has enough tributes
		when(mockSender.getTributes()).thenReturn(5);
		amount = 5;

		// sender and receiver are not the same user
		when(mockSender.getId()).thenReturn(1L);
		when(mockReceiver.getId()).thenReturn(2L);

		// sender and receiver are not the same department
		Division mockAnotherDivision = mock(Division.class);
		Department mockAnotherDepartment = mock(Department.class);
		when(mockSender.getMainDivision()).thenReturn(mockDivision);
		when(mockDivision.getId()).thenReturn(1L);
		when(mockReceiver.getMainDivision()).thenReturn(mockAnotherDivision);
		when(mockAnotherDivision.getId()).thenReturn(2L);
		when(mockDivision.getDepartment()).thenReturn(mockDepartment);
		when(mockDepartment.getId()).thenReturn(1L);
		when(mockAnotherDivision.getDepartment()).thenReturn(mockAnotherDepartment);
		when(mockAnotherDepartment.getId()).thenReturn(2L);
		Division mockNoDivision = mock(Division.class);
		when(mockDivisionRepository.findOne(1L)).thenReturn(mockNoDivision);
		Department mockNoDepartment = mock(Department.class);
		when(mockDepartmentRepository.findOne(1L)).thenReturn(mockNoDepartment);

		// return mock influence to get changed
		when(mockInfluenceRepository.findByCorporateerAndDepartmentAndDivisionAndType(any(), any(), any(), any()))
				.thenReturn(mock(Influence.class));

		// transfer influence
		assertTrue(transactionService.transfer(mockSender, mockReceiver, message, amount, mockType));

		// verify correct influence is accessed
		verify(mockInfluenceRepository).findByCorporateerAndDepartmentAndDivisionAndType(mockReceiver, mockNoDepartment,
				mockNoDivision, mockType);
		verify(mockInfluenceRepository).findByCorporateerAndDepartmentAndDivisionAndType(mockSender, mockNoDepartment,
				mockNoDivision, mockType);

		// verify transaction gets saved correctly
		verify(mockTransactionRepository).save(transactionCaptor.capture());
		assertEquals("Wrong influence type saved in transaction!", mockType, transactionCaptor.getValue().getType());
		assertEquals("Wrong amount saved in transaction!", amount, transactionCaptor.getValue().getAmount());
		assertEquals("Wrong message saved in transaction!", message, transactionCaptor.getValue().getMessage());
		assertEquals("Wrong sender saved in transaction!", mockSender, transactionCaptor.getValue().getSender());
		assertEquals("Wrong receiver saved in transaction!", mockReceiver, transactionCaptor.getValue().getReceiver());
		assertEquals("Wrong division saved in transaction!", mockNoDivision,
				transactionCaptor.getValue().getDivision());
		assertEquals("Wrong department saved in transaction!", mockNoDepartment,
				transactionCaptor.getValue().getDepartment());

		// verify sender gets saved
		verify(mockCorporateerRepository).save(mockSender);
	}

}
