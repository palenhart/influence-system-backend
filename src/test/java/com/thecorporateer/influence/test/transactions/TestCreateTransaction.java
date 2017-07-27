package com.thecorporateer.influence.test.transactions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Department;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.objects.InfluenceType;
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

	private String message = "";
	private int amount = 1;

	@InjectMocks
	private TransactionService mockTransactionService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidatorWithFalseAmount() {
		when(mockSender.getTributes()).thenReturn(5);
		amount = 10;
		assertFalse("Amount too high was not detected!",
				mockTransactionService.transfer(mockSender, mockReceiver, message, amount, mockType));
	}

	@Test
	public void testValidatorWithIdenticalUser() {
		when(mockSender.getTributes()).thenReturn(5);
		amount = 5;
		when(mockSender.getId()).thenReturn(1L);
		when(mockReceiver.getId()).thenReturn(1L);
		assertFalse("Identical user was not detected!",
				mockTransactionService.transfer(mockSender, mockReceiver, message, amount, mockType));
	}

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
		assertTrue(mockTransactionService.transfer(mockSender, mockReceiver, message, amount, mockType));

		// verify correct influence is accessed
		verify(mockInfluenceRepository).findByCorporateerAndDepartmentAndDivisionAndType(mockReceiver, mockDepartment,
				mockDivision, mockType);
		verify(mockInfluenceRepository).findByCorporateerAndDepartmentAndDivisionAndType(mockSender, mockDepartment,
				mockDivision, mockType);

		// verify sender gets saved
		verify(mockCorporateerRepository).save(mockSender);
	}
	
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
		when(mockReceiver.getMainDivision()).thenReturn(mockAnotherDivision);
		when(mockDivision.getDepartment()).thenReturn(mockDepartment);
		when(mockAnotherDivision.getDepartment()).thenReturn(mockDepartment);
		Division mockNoDivision = mock(Division.class);
		when(mockDivisionRepository.findOne(1L)).thenReturn(mockNoDivision);

		// return mock influence to get changed
		when(mockInfluenceRepository.findByCorporateerAndDepartmentAndDivisionAndType(any(), any(), any(), any()))
				.thenReturn(mock(Influence.class));
		assertTrue(mockTransactionService.transfer(mockSender, mockReceiver, message, amount, mockType));

		// verify correct influence is accessed
		verify(mockInfluenceRepository).findByCorporateerAndDepartmentAndDivisionAndType(mockReceiver, mockDepartment,
				mockNoDivision, mockType);
		verify(mockInfluenceRepository).findByCorporateerAndDepartmentAndDivisionAndType(mockSender, mockDepartment,
				mockNoDivision, mockType);

		// verify sender gets saved
		verify(mockCorporateerRepository).save(mockSender);
	}
	
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
		when(mockReceiver.getMainDivision()).thenReturn(mockAnotherDivision);
		when(mockDivision.getDepartment()).thenReturn(mockDepartment);
		when(mockAnotherDivision.getDepartment()).thenReturn(mockAnotherDepartment);
		Division mockNoDivision = mock(Division.class);
		when(mockDivisionRepository.findOne(1L)).thenReturn(mockNoDivision);
		Department mockNoDepartment = mock(Department.class);
		when(mockDepartmentRepository.findOne(1L)).thenReturn(mockNoDepartment);

		// return mock influence to get changed
		when(mockInfluenceRepository.findByCorporateerAndDepartmentAndDivisionAndType(any(), any(), any(), any()))
				.thenReturn(mock(Influence.class));
		assertTrue(mockTransactionService.transfer(mockSender, mockReceiver, message, amount, mockType));

		// verify correct influence is accessed
		verify(mockInfluenceRepository).findByCorporateerAndDepartmentAndDivisionAndType(mockReceiver, mockNoDepartment,
				mockNoDivision, mockType);
		verify(mockInfluenceRepository).findByCorporateerAndDepartmentAndDivisionAndType(mockSender, mockNoDepartment,
				mockNoDivision, mockType);

		// verify sender gets saved
		verify(mockCorporateerRepository).save(mockSender);
	}

}
