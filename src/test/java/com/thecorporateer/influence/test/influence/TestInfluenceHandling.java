package com.thecorporateer.influence.test.influence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.thecorporateer.influence.objects.Conversion;
import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Department;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.objects.InfluenceType;
import com.thecorporateer.influence.repositories.ConversionRepository;
import com.thecorporateer.influence.repositories.DepartmentRepository;
import com.thecorporateer.influence.repositories.DivisionRepository;
import com.thecorporateer.influence.repositories.InfluenceRepository;
import com.thecorporateer.influence.services.InfluenceHandlingService;

public class TestInfluenceHandling {

	@Mock
	private InfluenceRepository mockInfluenceRepository;
	@Mock
	private DepartmentRepository mockDepartmentRepository;
	@Mock
	private DivisionRepository mockDivisionRepository;
	@Mock
	private ConversionRepository mockConversionRepository;
	@Mock
	private Corporateer mockCorporateer;
	@Mock
	private Department mockDepartment;
	@Mock
	private Division mockDivision;
	@Mock
	private InfluenceType mockInfluenceType;

	@Captor
	private ArgumentCaptor<Influence> influenceCaptor;
	@Captor
	private ArgumentCaptor<Conversion> conversionCaptor;

	@Mock
	private Influence mockInfluenceToGeneralize;
	@Mock
	private Influence mockMoreGeneralInfluence;

	@InjectMocks
	private InfluenceHandlingService influenceHandlingService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(mockInfluenceToGeneralize.getCorporateer()).thenReturn(mockCorporateer);
		when(mockInfluenceToGeneralize.getDepartment()).thenReturn(mockDepartment);
		when(mockInfluenceToGeneralize.getDivision()).thenReturn(mockDivision);
		when(mockInfluenceToGeneralize.getType()).thenReturn(mockInfluenceType);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConvertToDepartment() {
		Department mockToDepartment = mock(Department.class);
		Division mockToDivision = mock(Division.class);
		
		when(mockInfluenceToGeneralize.getAmount()).thenReturn(10);
		when(mockMoreGeneralInfluence.getAmount()).thenReturn(5);
		when(mockInfluenceType.getId()).thenReturn(1L);
		when(mockDivisionRepository.findOne(1L)).thenReturn(mockToDivision);

		when(mockInfluenceRepository.findByCorporateerAndDepartmentAndDivisionAndType(mockCorporateer, mockDepartment,
				mockToDivision, mockInfluenceType)).thenReturn(mockMoreGeneralInfluence);

		when(mockMoreGeneralInfluence.getDepartment()).thenReturn(mockToDepartment);
		when(mockMoreGeneralInfluence.getDivision()).thenReturn(mockToDivision);
		String timestamp = Instant.now().truncatedTo(ChronoUnit.SECONDS).toString();
		assertTrue("Influence generalization failed!",
				influenceHandlingService.convertInfluenceToDepartment(mockInfluenceToGeneralize));

		verify(mockMoreGeneralInfluence).setAmount(15);
		verify(mockInfluenceToGeneralize).setAmount(0);

		verify(mockInfluenceRepository, times(2)).save(influenceCaptor.capture());
		assertEquals("Department influence does not get saved correctly!", mockMoreGeneralInfluence,
				influenceCaptor.getAllValues().get(0));
		assertEquals("Division influence does not get saved correctly!", mockInfluenceToGeneralize,
				influenceCaptor.getAllValues().get(1));

		verify(mockConversionRepository).save(conversionCaptor.capture());
		assertEquals("Wrong timestamp in conversion!", timestamp, conversionCaptor.getValue().getTimestamp());
		assertEquals("Wrong corporateer in conversion!", mockCorporateer, conversionCaptor.getValue().getCorporateer());
		assertEquals("Wrong fromDepartment in conversion!", mockDepartment,
				conversionCaptor.getValue().getFromDepartment());
		assertEquals("Wrong fromDivision in conversion!", mockDivision, conversionCaptor.getValue().getFromDivision());
		assertEquals("Wrong toDepartment in conversion!", mockToDepartment,
				conversionCaptor.getValue().getToDepartment());
		assertEquals("Wrong toDivision in conversion!", mockToDivision, conversionCaptor.getValue().getToDivision());
		assertEquals("Wrong type in conversion!", mockInfluenceType, conversionCaptor.getValue().getType());
		assertEquals("Wrong amount in conversion!", 10, conversionCaptor.getValue().getAmount());
	}

	@Test
	public void testTryToConvertDemeritsToDepartment() {
		when(mockInfluenceToGeneralize.getAmount()).thenReturn(10);
		when(mockMoreGeneralInfluence.getAmount()).thenReturn(5);
		when(mockInfluenceType.getId()).thenReturn(2L);
		when(mockDivisionRepository.findOne(1L)).thenReturn(mockDivision);

		when(mockInfluenceRepository.findByCorporateerAndDepartmentAndDivisionAndType(mockCorporateer, mockDepartment,
				mockDivision, mockInfluenceType)).thenReturn(mockMoreGeneralInfluence);

		assertFalse("Tried generalization of demerits!",
				influenceHandlingService.convertInfluenceToDepartment(mockInfluenceToGeneralize));

	}

	@Test
	public void testTryToConvertNonDivisionSpecificToDepartment() {
		when(mockInfluenceToGeneralize.getAmount()).thenReturn(10);
		when(mockMoreGeneralInfluence.getAmount()).thenReturn(5);
		when(mockInfluenceType.getId()).thenReturn(1L);
		when(mockDivisionRepository.findOne(1L)).thenReturn(mockDivision);

		when(mockInfluenceToGeneralize.getDivision()).thenReturn(mockDivision);
		when(mockDivision.getId()).thenReturn(1L);

		when(mockInfluenceRepository.findByCorporateerAndDepartmentAndDivisionAndType(mockCorporateer, mockDepartment,
				mockDivision, mockInfluenceType)).thenReturn(mockMoreGeneralInfluence);

		assertFalse("Tried generalization of already generalized influence (department)!",
				influenceHandlingService.convertInfluenceToDepartment(mockInfluenceToGeneralize));

	}

	@Test
	public void testConvertToGeneral() {
		when(mockInfluenceToGeneralize.getAmount()).thenReturn(10);
		when(mockMoreGeneralInfluence.getAmount()).thenReturn(5);
		when(mockInfluenceType.getId()).thenReturn(1L);
		when(mockDivisionRepository.findOne(1L)).thenReturn(mockDivision);
		when(mockDepartmentRepository.findOne(1L)).thenReturn(mockDepartment);

		when(mockInfluenceRepository.findByCorporateerAndDepartmentAndDivisionAndType(mockCorporateer, mockDepartment,
				mockDivision, mockInfluenceType)).thenReturn(mockMoreGeneralInfluence);

		assertTrue("Influence generalization failed!",
				influenceHandlingService.convertInfluenceToGeneral(mockInfluenceToGeneralize));

		verify(mockMoreGeneralInfluence).setAmount(15);
		verify(mockInfluenceToGeneralize).setAmount(0);

		verify(mockInfluenceRepository, times(2)).save(influenceCaptor.capture());
		assertEquals("Department influence does not get saved correctly!", mockMoreGeneralInfluence,
				influenceCaptor.getAllValues().get(0));
		assertEquals("Division influence does not get saved correctly!", mockInfluenceToGeneralize,
				influenceCaptor.getAllValues().get(1));
	}

	@Test
	public void testTryToConvertDemeritsToGeneral() {
		when(mockInfluenceType.getId()).thenReturn(2L);

		when(mockInfluenceRepository.findByCorporateerAndDepartmentAndDivisionAndType(mockCorporateer, mockDepartment,
				mockDivision, mockInfluenceType)).thenReturn(mockMoreGeneralInfluence);

		assertFalse("Tried generalization of demerits!",
				influenceHandlingService.convertInfluenceToGeneral(mockInfluenceToGeneralize));

	}

	@Test
	public void testTryToConvertGeneralToGeneral() {
		when(mockInfluenceType.getId()).thenReturn(1L);

		when(mockInfluenceToGeneralize.getDepartment()).thenReturn(mockDepartment);
		when(mockDepartment.getId()).thenReturn(1L);

		when(mockInfluenceRepository.findByCorporateerAndDepartmentAndDivisionAndType(mockCorporateer, mockDepartment,
				mockDivision, mockInfluenceType)).thenReturn(mockMoreGeneralInfluence);

		assertFalse("Tried generalization of already generalized influence (department)!",
				influenceHandlingService.convertInfluenceToGeneral(mockInfluenceToGeneralize));

	}

}
