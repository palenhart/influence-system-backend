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
import java.util.ArrayList;
import java.util.List;

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

import com.thecorporateer.influence.exceptions.InfluenceNotFoundException;
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
import com.thecorporateer.influence.services.ObjectService;

public class TestInfluenceHandling {

	@Mock
	private InfluenceRepository mockInfluenceRepository;
	@Mock
	private ObjectService mockObjectService;
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
	@Mock
	private Department mockToDepartment;
	@Mock
	private Division mockToDivision;

	@InjectMocks
	private InfluenceHandlingService influenceHandlingService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		when(mockInfluenceToGeneralize.getCorporateer()).thenReturn(mockCorporateer);
		when(mockInfluenceToGeneralize.getDivision()).thenReturn(mockDivision);
		when(mockInfluenceToGeneralize.getDivision().getDepartment()).thenReturn(mockDepartment);
		when(mockInfluenceToGeneralize.getType()).thenReturn(mockInfluenceType);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testConvertToDepartment() {

		when(mockInfluenceToGeneralize.getDivision().getId()).thenReturn(10L);

		when(mockInfluenceToGeneralize.getAmount()).thenReturn(10);
		when(mockMoreGeneralInfluence.getAmount()).thenReturn(5);
		when(mockInfluenceType.getId()).thenReturn(1L);

		when(mockObjectService.getDivisionByNameAndDepartment("none",
				mockInfluenceToGeneralize.getDivision().getDepartment())).thenReturn(mockToDivision);

		when(mockInfluenceRepository.findByCorporateerAndDivisionAndType(mockCorporateer, mockToDivision,
				mockInfluenceType)).thenReturn(mockMoreGeneralInfluence);

		when(mockMoreGeneralInfluence.getDivision()).thenReturn(mockToDivision);
		when(mockMoreGeneralInfluence.getDivision().getDepartment()).thenReturn(mockToDepartment);
		String timestamp = Instant.now().truncatedTo(ChronoUnit.SECONDS).toString();
		assertTrue("Influence generalization failed!",
				influenceHandlingService.convertInfluence(mockInfluenceToGeneralize, 10, false));

		verify(mockMoreGeneralInfluence).setAmount(15);
		verify(mockInfluenceToGeneralize).setAmount(0);

		verify(mockInfluenceRepository, times(2)).save(influenceCaptor.capture());
		assertEquals("Department influence does not get saved correctly!", mockMoreGeneralInfluence,
				influenceCaptor.getAllValues().get(0));
		assertEquals("Division influence does not get saved correctly!", mockInfluenceToGeneralize,
				influenceCaptor.getAllValues().get(1));

		verify(mockObjectService).saveConversion(conversionCaptor.capture());
		assertEquals("Wrong timestamp in conversion!", timestamp, conversionCaptor.getValue().getTimestamp());
		assertEquals("Wrong corporateer in conversion!", mockCorporateer, conversionCaptor.getValue().getCorporateer());
		assertEquals("Wrong fromDivision in conversion!", mockDivision, conversionCaptor.getValue().getFromDivision());
		assertEquals("Wrong toDivision in conversion!", mockToDivision, conversionCaptor.getValue().getToDivision());
		assertEquals("Wrong type in conversion!", mockInfluenceType, conversionCaptor.getValue().getType());
		assertEquals("Wrong amount in conversion!", 10, conversionCaptor.getValue().getAmount());
	}

	@Test
	public void testTryToConvertDemeritsToDepartment() {
		when(mockInfluenceToGeneralize.getAmount()).thenReturn(10);
		when(mockInfluenceType.getId()).thenReturn(2L);
		when(mockDivision.getId()).thenReturn(10L);

		assertFalse("Tried generalization of demerits!",
				influenceHandlingService.convertInfluence(mockInfluenceToGeneralize, 10, false));

	}

	@Test
	public void testConvertToGeneral() {

		when(mockInfluenceToGeneralize.getAmount()).thenReturn(10);
		when(mockMoreGeneralInfluence.getAmount()).thenReturn(5);

		when(mockInfluenceType.getId()).thenReturn(1L);

		when(mockObjectService.getDefaultDivision()).thenReturn(mockToDivision);

		when(mockInfluenceRepository.findByCorporateerAndDivisionAndType(mockCorporateer, mockToDivision,
				mockInfluenceType)).thenReturn(mockMoreGeneralInfluence);

		when(mockMoreGeneralInfluence.getDivision()).thenReturn(mockToDivision);
		when(mockMoreGeneralInfluence.getDivision().getDepartment()).thenReturn(mockToDepartment);

		String timestamp = Instant.now().truncatedTo(ChronoUnit.SECONDS).toString();
		assertTrue("Influence generalization failed!",
				influenceHandlingService.convertInfluence(mockInfluenceToGeneralize, 10, true));

		verify(mockMoreGeneralInfluence).setAmount(15);
		verify(mockInfluenceToGeneralize).setAmount(0);

		verify(mockInfluenceRepository, times(2)).save(influenceCaptor.capture());
		assertEquals("Department influence does not get saved correctly!", mockMoreGeneralInfluence,
				influenceCaptor.getAllValues().get(0));
		assertEquals("Division influence does not get saved correctly!", mockInfluenceToGeneralize,
				influenceCaptor.getAllValues().get(1));

		verify(mockObjectService).saveConversion(conversionCaptor.capture());
		assertEquals("Wrong timestamp in conversion!", timestamp, conversionCaptor.getValue().getTimestamp());
		assertEquals("Wrong corporateer in conversion!", mockCorporateer, conversionCaptor.getValue().getCorporateer());
		assertEquals("Wrong fromDivision in conversion!", mockDivision, conversionCaptor.getValue().getFromDivision());
		assertEquals("Wrong toDivision in conversion!", mockToDivision, conversionCaptor.getValue().getToDivision());
		assertEquals("Wrong type in conversion!", mockInfluenceType, conversionCaptor.getValue().getType());
		assertEquals("Wrong amount in conversion!", 10, conversionCaptor.getValue().getAmount());
	}

	@Test
	public void testTryToConvertDemeritsToGeneral() {
		when(mockInfluenceType.getId()).thenReturn(2L);

		assertFalse("Tried generalization of demerits!",
				influenceHandlingService.convertInfluence(mockInfluenceToGeneralize, 10, true));

	}

	@Test
	public void testTryToConvertGeneralToGeneral() {
		when(mockInfluenceType.getId()).thenReturn(1L);

		when(mockDepartment.getId()).thenReturn(1L);

		assertFalse("Tried generalization of already generalized influence (department)!",
				influenceHandlingService.convertInfluence(mockInfluenceToGeneralize, 10, true));

	}

	@Test
	public void testTryToConvertMoreInfluenceThanAvailable() {
		when(mockInfluenceType.getId()).thenReturn(1L);

		when(mockDepartment.getId()).thenReturn(2L);

		when(mockInfluenceToGeneralize.getAmount()).thenReturn(5);

		assertFalse("Tried generalization more influence than available!",
				influenceHandlingService.convertInfluence(mockInfluenceToGeneralize, 10, true));

	}

	@Test
	public void testThrowInfluenceNotFoundException() {

		exception.expect(InfluenceNotFoundException.class);

		when(mockInfluenceType.getId()).thenReturn(1L);

		when(mockDepartment.getId()).thenReturn(2L);

		when(mockInfluenceToGeneralize.getAmount()).thenReturn(10);

		when(mockInfluenceRepository.findByCorporateerAndDivisionAndType(mockCorporateer, mockToDivision,
				mockInfluenceType)).thenReturn(null);

		influenceHandlingService.convertInfluence(mockInfluenceToGeneralize, 10, false);

	}

	@Test
	public void testUpdateInfluences() {

		List<Influence> mockInfluences = new ArrayList<Influence>();

		mockInfluences.add(mock(Influence.class));
		mockInfluences.add(mock(Influence.class));
		mockInfluences.add(mock(Influence.class));
		mockInfluences.add(mock(Influence.class));
		
		when(mockInfluenceRepository.save(mockInfluences)).thenReturn(mockInfluences);

		assertEquals("Wrong influences saved!", mockInfluences,
				influenceHandlingService.updateInfluences(mockInfluences));
	}

}
