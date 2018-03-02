package com.thecorporateer.influence.test.transactions;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import com.thecorporateer.influence.exceptions.IllegalTransferRequestException;
import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Department;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.objects.InfluenceType;
import com.thecorporateer.influence.objects.Transaction;
import com.thecorporateer.influence.objects.User;
import com.thecorporateer.influence.repositories.TransactionRepository;
import com.thecorporateer.influence.services.CorporateerHandlingService;
import com.thecorporateer.influence.services.InfluenceHandlingService;
import com.thecorporateer.influence.services.ObjectService;
import com.thecorporateer.influence.services.TransactionService;
import com.thecorporateer.influence.services.UserHandlingService;

public class TestCreateTransaction {

	@Mock
	private InfluenceHandlingService mockInfluenceHandlingService;
	@Mock
	private TransactionRepository mockTransactionRepository;
	@Mock
	private CorporateerHandlingService mockCorporateerHandlingService;
	@Mock
	private ObjectService mockObjectService;
	@Mock
	private UserHandlingService mockUserHandlingService;
	@Mock
	private Authentication mockAuthentication;
	@Mock
	private User mockSenderUser;
	@Mock
	private User mockReceiverUser;
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

	private String receiver = "mockReceiverName";
	private String message = "mockMessage";
	private String type = "mockTypeName";
	private int amount = 1;

	@InjectMocks
	private TransactionService transactionService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		when(mockUserHandlingService.getUserByName(mockAuthentication.getName())).thenReturn(mockSenderUser);
		when(mockSenderUser.getCorporateer()).thenReturn(mockSender);

		when(mockCorporateerHandlingService.getCorporateerByName(receiver)).thenReturn(mockReceiver);

		when(mockObjectService.getInfluenceTypeByName(type)).thenReturn(mockType);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Rule
	public ExpectedException exception = ExpectedException.none();

	/**
	 * Test whether validator function detects wrong transaction amounts (too high)
	 */
	@Test
	public void testValidatorWithHighAmount() {
		// sender has not enough tributes
		when(mockSender.getTributes()).thenReturn(5);
		amount = 10;

		exception.expect(IllegalTransferRequestException.class);

		transactionService.userTransfer(mockAuthentication, receiver, message, amount, type);
	}

	/**
	 * Test whether validator function detects wrong transaction amounts (zero)
	 */
	@Test
	public void testValidatorWithZeroAmount() {
		// sender has not enough tributes
		when(mockSender.getTributes()).thenReturn(5);

		exception.expect(IllegalTransferRequestException.class);

		// transaction amount is zero
		amount = 0;

		transactionService.userTransfer(mockAuthentication, receiver, message, amount, type);
	}

	/**
	 * Test whether validator function detects wrong transaction amounts (negative)
	 */
	@Test
	public void testValidatorWithnegativeAmount() {
		// sender has not enough tributes
		when(mockSender.getTributes()).thenReturn(5);
		amount = 10;

		exception.expect(IllegalTransferRequestException.class);

		// transaction amount is negative
		amount = -1;

		transactionService.userTransfer(mockAuthentication, receiver, message, amount, type);
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

		exception.expect(IllegalTransferRequestException.class);

		transactionService.userTransfer(mockAuthentication, receiver, message, amount, type);

		// verify no interactions with services happen
		verifyZeroInteractions(mockObjectService);
		verifyZeroInteractions(mockInfluenceHandlingService);
		verifyZeroInteractions(mockTransactionRepository);
		verifyZeroInteractions(mockCorporateerHandlingService);
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

		// return mock influence to get changed
		when(mockInfluenceHandlingService.getInfluenceByCorporateerAndDivisionAndType(any(), any(), any()))
				.thenReturn(mock(Influence.class));
		when(mockInfluenceHandlingService.getInfluenceByCorporateerAndDivisionAndType(any(), any(), any()))
				.thenReturn(mock(Influence.class));

		// type is influence
		when(mockType.getId()).thenReturn(1L);

		transactionService.userTransfer(mockAuthentication, receiver, message, amount, type);

		// verify correct influence is accessed
		verify(mockInfluenceHandlingService).getInfluenceByCorporateerAndDivisionAndType(mockReceiver, mockDivision,
				mockType);
		verify(mockInfluenceHandlingService).getInfluenceByCorporateerAndDivisionAndType(mockSender, mockDivision,
				mockType);

		// verify transaction gets saved correctly
		verify(mockTransactionRepository).save(transactionCaptor.capture());
		assertEquals("Wrong influence type saved in transaction!", mockType, transactionCaptor.getValue().getType());
		assertEquals("Wrong amount saved in transaction!", amount, transactionCaptor.getValue().getAmount());
		assertEquals("Wrong message saved in transaction!", message, transactionCaptor.getValue().getMessage());
		assertEquals("Wrong sender saved in transaction!", mockSender, transactionCaptor.getValue().getSender());
		assertEquals("Wrong receiver saved in transaction!", mockReceiver, transactionCaptor.getValue().getReceiver());
		assertEquals("Wrong division saved in transaction!", mockDivision, transactionCaptor.getValue().getDivision());

		// verify receiver and sender get saved
		verify(mockCorporateerHandlingService).updateCorporateer(mockReceiver);
		verify(mockCorporateerHandlingService).updateCorporateer(mockSender);
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
		Division mockDepartmentDefaultDivision = mock(Division.class);
		when(mockSender.getMainDivision()).thenReturn(mockDivision);
		when(mockDivision.getId()).thenReturn(1L);
		when(mockReceiver.getMainDivision()).thenReturn(mockAnotherDivision);
		when(mockAnotherDivision.getId()).thenReturn(2L);
		when(mockDivision.getDepartment()).thenReturn(mockDepartment);
		when(mockAnotherDivision.getDepartment()).thenReturn(mockDepartment);

		when(mockObjectService.getDivisionByNameAndDepartment("none", mockDivision.getDepartment()))
				.thenReturn(mockDepartmentDefaultDivision);

		// return mock influence to get changed
		when(mockInfluenceHandlingService.getInfluenceByCorporateerAndDivisionAndType(any(), any(), any()))
				.thenReturn(mock(Influence.class));

		// type is influence
		when(mockType.getId()).thenReturn(1L);

		// transfer influence
		transactionService.userTransfer(mockAuthentication, receiver, message, amount, type);

		// verify correct influence is accessed
		verify(mockInfluenceHandlingService).getInfluenceByCorporateerAndDivisionAndType(mockReceiver,
				mockDepartmentDefaultDivision, mockType);
		verify(mockInfluenceHandlingService).getInfluenceByCorporateerAndDivisionAndType(mockSender,
				mockDepartmentDefaultDivision, mockType);

		// verify transaction gets saved correctly
		verify(mockTransactionRepository).save(transactionCaptor.capture());
		assertEquals("Wrong influence type saved in transaction!", mockType, transactionCaptor.getValue().getType());
		assertEquals("Wrong amount saved in transaction!", amount, transactionCaptor.getValue().getAmount());
		assertEquals("Wrong message saved in transaction!", message, transactionCaptor.getValue().getMessage());
		assertEquals("Wrong sender saved in transaction!", mockSender, transactionCaptor.getValue().getSender());
		assertEquals("Wrong receiver saved in transaction!", mockReceiver, transactionCaptor.getValue().getReceiver());
		assertEquals("Wrong division saved in transaction!", mockDepartmentDefaultDivision,
				transactionCaptor.getValue().getDivision());

		// verify receiver and sender get saved
		verify(mockCorporateerHandlingService).updateCorporateer(mockReceiver);
		verify(mockCorporateerHandlingService).updateCorporateer(mockSender);
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

		when(mockObjectService.getDefaultDivision()).thenReturn(mockNoDivision);

		// return mock influence to get changed
		when(mockInfluenceHandlingService.getInfluenceByCorporateerAndDivisionAndType(any(), any(), any()))
				.thenReturn(mock(Influence.class));

		// type is influence
		when(mockType.getId()).thenReturn(1L);

		// transfer influence
		transactionService.userTransfer(mockAuthentication, receiver, message, amount, type);

		// verify correct influence is accessed
		verify(mockInfluenceHandlingService).getInfluenceByCorporateerAndDivisionAndType(mockReceiver, mockNoDivision,
				mockType);
		verify(mockInfluenceHandlingService).getInfluenceByCorporateerAndDivisionAndType(mockSender, mockNoDivision,
				mockType);

		// verify transaction gets saved correctly
		verify(mockTransactionRepository).save(transactionCaptor.capture());
		assertEquals("Wrong influence type saved in transaction!", mockType, transactionCaptor.getValue().getType());
		assertEquals("Wrong amount saved in transaction!", amount, transactionCaptor.getValue().getAmount());
		assertEquals("Wrong message saved in transaction!", message, transactionCaptor.getValue().getMessage());
		assertEquals("Wrong sender saved in transaction!", mockSender, transactionCaptor.getValue().getSender());
		assertEquals("Wrong receiver saved in transaction!", mockReceiver, transactionCaptor.getValue().getReceiver());
		assertEquals("Wrong division saved in transaction!", mockNoDivision,
				transactionCaptor.getValue().getDivision());

		// verify receiver and sender get saved
		verify(mockCorporateerHandlingService).updateCorporateer(mockReceiver);
		verify(mockCorporateerHandlingService).updateCorporateer(mockSender);
	}

	/**
	 * Test transaction of demerits between members of the same division
	 */
	@Test
	public void testTransactionSameDivisionDemerit() {
		// sender has enough tributes
		when(mockSender.getTributes()).thenReturn(5);
		amount = 5;

		// sender and receiver are not the same user
		when(mockSender.getId()).thenReturn(1L);
		when(mockReceiver.getId()).thenReturn(2L);

		// sender and receiver are in the same division
		when(mockSender.getMainDivision()).thenReturn(mockDivision);
		when(mockReceiver.getMainDivision()).thenReturn(mockDivision);

		// type is demerits
		when(mockType.getId()).thenReturn(2L);

		// return mock influence to get changed
		when(mockInfluenceHandlingService.getInfluenceByCorporateerAndDivisionAndType(any(), any(), any()))
				.thenReturn(mock(Influence.class));
		when(mockInfluenceHandlingService.getInfluenceByCorporateerAndDivisionAndType(any(), any(), any()))
				.thenReturn(mock(Influence.class));
		transactionService.userTransfer(mockAuthentication, receiver, message, amount, type);

		// verify correct influence is accessed
		verify(mockInfluenceHandlingService).getInfluenceByCorporateerAndDivisionAndType(mockReceiver, mockDivision,
				mockType);
		verify(mockInfluenceHandlingService).getInfluenceByCorporateerAndDivisionAndType(mockSender, mockDivision,
				mockType);

		// verify transaction gets saved correctly
		verify(mockTransactionRepository).save(transactionCaptor.capture());
		assertEquals("Wrong influence type saved in transaction!", mockType, transactionCaptor.getValue().getType());
		assertEquals("Wrong amount saved in transaction!", amount, transactionCaptor.getValue().getAmount());
		assertEquals("Wrong message saved in transaction!", message, transactionCaptor.getValue().getMessage());
		assertEquals("Wrong sender saved in transaction!", mockSender, transactionCaptor.getValue().getSender());
		assertEquals("Wrong receiver saved in transaction!", mockReceiver, transactionCaptor.getValue().getReceiver());
		assertEquals("Wrong division saved in transaction!", mockDivision, transactionCaptor.getValue().getDivision());

		// verify sender gets saved
		verify(mockCorporateerHandlingService).updateCorporateer(mockSender);
	}

}
