package com.thecorporateer.influence.test.datahandling;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import com.thecorporateer.influence.exceptions.IllegalDivisionChangeRequestException;
import com.thecorporateer.influence.exceptions.RepositoryNotFoundException;
import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.objects.InfluenceType;
import com.thecorporateer.influence.objects.Rank;
import com.thecorporateer.influence.objects.User;
import com.thecorporateer.influence.repositories.CorporateerRepository;
import com.thecorporateer.influence.services.ActionLogService;
import com.thecorporateer.influence.services.CorporateerHandlingService;
import com.thecorporateer.influence.services.InfluenceHandlingService;
import com.thecorporateer.influence.services.ObjectService;
import com.thecorporateer.influence.services.UserHandlingService;

public class TestCorporateerService {

	@Mock
	private CorporateerRepository mockCorporateerRepository;
	@Mock
	private ObjectService mockObjectService;
	@Mock
	private InfluenceHandlingService mockInfluenceHandlingService;
	@Mock
	private UserHandlingService mockUserHandlingService;
	@Mock
	private ActionLogService actionLogService;
	@Mock
	private Corporateer mockCorporateer;
	@Mock
	private Authentication mockAuthentication;
	@Mock
	private User mockUser;
	@Mock
	private Rank mockRank;
	@Mock
	private Influence mockInfluence;

	@Captor
	private ArgumentCaptor<Corporateer> corporateerCaptor;
	@Captor
	private ArgumentCaptor<List<Corporateer>> corporateerListCaptor;
	@Captor
	private ArgumentCaptor<List<Influence>> influenceCaptor;

	@InjectMocks
	private CorporateerHandlingService corporateerHandlingService;

	// lists to test with
	private List<InfluenceType> types = new ArrayList<>();
	private List<Division> divisions = new ArrayList<>();

	private String rankName = "rankName";

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		types.add(mock(InfluenceType.class));
		types.add(mock(InfluenceType.class));
		divisions.add(mock(Division.class));
		divisions.add(mock(Division.class));
		divisions.add(mock(Division.class));

		when(mockUserHandlingService.getUserByName(mockAuthentication.getName())).thenReturn(mockUser);
		when(mockUser.getCorporateer()).thenReturn(mockCorporateer);
		when(mockObjectService.getRankByName(rankName)).thenReturn(mockRank);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testGetAvailableCorporateerByName() {

		String mockName = "mockName";

		when(mockCorporateerRepository.findByName(mockName)).thenReturn(mockCorporateer);

		assertEquals("Wrong Corporateer returned", mockCorporateer,
				corporateerHandlingService.getCorporateerByName(mockName));
	}

	@Test
	public void testGetUnavailableCorporateerByName() {

		String mockName = "mockName";

		when(mockCorporateerRepository.findByName(mockName)).thenReturn(null);

		exception.expect(RepositoryNotFoundException.class);

		corporateerHandlingService.getCorporateerByName(mockName);
	}

	@Test
	public void testGetAllCorporateers() {

		List<Corporateer> mockCorporateerList = new ArrayList<>();

		when(mockCorporateerRepository.findAll()).thenReturn(mockCorporateerList);

		assertEquals("Wrong list of Corporateers returned", mockCorporateerList,
				corporateerHandlingService.getAllCorporateers());
	}

	@Test
	public void testGetAllCorporateersException() {

		when(mockCorporateerRepository.findAll()).thenReturn(null);

		exception.expect(RepositoryNotFoundException.class);

		corporateerHandlingService.getAllCorporateers();
	}

	@Test
	public void testGetTotalInfluence() {

		Influence mockInfluence1 = mock(Influence.class);
		Influence mockInfluence2 = mock(Influence.class);
		Influence mockInfluence3 = mock(Influence.class);
		InfluenceType mockInfluenceType1 = mock(InfluenceType.class);
		InfluenceType mockInfluenceType2 = mock(InfluenceType.class);

		List<Influence> mockInfluences = new ArrayList<>();
		mockInfluences.add(mockInfluence1);
		mockInfluences.add(mockInfluence2);
		mockInfluences.add(mockInfluence3);

		when(mockInfluence1.getType()).thenReturn(mockInfluenceType1);
		when(mockInfluence2.getType()).thenReturn(mockInfluenceType1);
		when(mockInfluence3.getType()).thenReturn(mockInfluenceType2);

		when(mockInfluenceType1.getId()).thenReturn(1L);
		when(mockInfluenceType2.getId()).thenReturn(2L);

		when(mockInfluence1.getAmount()).thenReturn(1);
		when(mockInfluence2.getAmount()).thenReturn(10);
		when(mockInfluence3.getAmount()).thenReturn(100);

		when(mockCorporateer.getInfluence()).thenReturn(mockInfluences);

		assertEquals("Wrong total influence amout calculated", 11,
				corporateerHandlingService.getTotalInfluence(mockCorporateer));
	}

	@Test
	public void testDistributeTributes() {

		List<Corporateer> mockCorporateerList = new ArrayList<>();
		Corporateer corporateer = new Corporateer();
		corporateer.setTributes(5);
		corporateer.setRank(mockRank);
		mockCorporateerList.add(corporateer);

		when(mockRank.getTributesPerWeek()).thenReturn(10);

		when(mockCorporateerRepository.findAll()).thenReturn(mockCorporateerList);

		corporateerHandlingService.distributeTributes();

		verify(mockCorporateerRepository).save(corporateerListCaptor.capture());

		assertEquals("Wrong Corporateer saved", corporateer, corporateerListCaptor.getValue().get(0));
		assertEquals("Wrong amount of tributes saved", 15, corporateerListCaptor.getValue().get(0).getTributes());
	}

	@Test
	public void testCreateCorporateer() {
		when(mockObjectService.getLowestRank()).thenReturn(mockRank);

		Division mockDivision = mock(Division.class);
		when(mockObjectService.getDefaultDivision()).thenReturn(mockDivision);

		when(mockObjectService.getAllInfluenceTypes()).thenReturn(types);
		when(mockObjectService.getAllDivisions()).thenReturn(divisions);

		corporateerHandlingService.createCorporateer("Corporateer");

		verify(mockCorporateerRepository).save(corporateerCaptor.capture());
		assertEquals("Corporateer name saved incorrectly", "Corporateer", corporateerCaptor.getValue().getName());
		assertEquals("Corporateer rank saved incorrectly", mockRank, corporateerCaptor.getValue().getRank());
		assertEquals("Corporateer division saved incorrectly", mockDivision,
				corporateerCaptor.getValue().getMainDivision());
		assertEquals("Corporateer tributes initialized incorrectly", 0, corporateerCaptor.getValue().getTributes());

		verify(mockInfluenceHandlingService).updateInfluences(influenceCaptor.capture());
		assertEquals("List of influences has wrong size", 6, influenceCaptor.getValue().size());
	}

	@Test
	public void testSetMainDivisionWhenAlreadySet() {
		String mockDivisionName = "mockDivisionName";

		when(mockUserHandlingService.getUserByName(mockAuthentication.getName())).thenReturn(mockUser);
		when(mockUser.getCorporateer()).thenReturn(mockCorporateer);
		when(mockCorporateer.getMainDivision()).thenReturn(mock(Division.class));
		when(mockCorporateer.getMainDivision().getName()).thenReturn(mockDivisionName);

		exception.expect(IllegalDivisionChangeRequestException.class);

		corporateerHandlingService.setMainDivision(mockAuthentication, mockDivisionName);
	}

	@Test
	@Ignore
	public void testSetMainDivisionToNone() {
		String mockDivisionName = "none";
		Division mockDivision = mock(Division.class);
		Corporateer corporateer = new Corporateer();
		corporateer.setMainDivision(mockDivision);

		when(mockUserHandlingService.getUserByName(mockAuthentication.getName())).thenReturn(mockUser);
		when(mockUser.getCorporateer()).thenReturn(corporateer);
		when(mockDivision.getName()).thenReturn("someDivisionName");

		Division mockNoDivision = mock(Division.class);
		when(mockObjectService.getDefaultDivision()).thenReturn(mockNoDivision);

		corporateerHandlingService.setMainDivision(mockAuthentication, mockDivisionName);

		verify(mockCorporateerRepository).save(corporateerCaptor.capture());
		assertEquals(mockNoDivision, corporateerCaptor.getValue().getMainDivision());
	}

	@Test
	@Ignore
	public void testSetMainDivision() {
		String mockDivisionName = "mockDivisionName";
		Division mockDivision = mock(Division.class);
		Corporateer corporateer = new Corporateer();
		corporateer.setMainDivision(mockDivision);

		when(mockUserHandlingService.getUserByName(mockAuthentication.getName())).thenReturn(mockUser);
		when(mockUser.getCorporateer()).thenReturn(corporateer);
		when(mockDivision.getName()).thenReturn("someDivisionName");

		Division mockNewDivision = mock(Division.class);
		when(mockObjectService.getDivisionByName(mockDivisionName)).thenReturn(mockNewDivision);

		List<Division> divisions = new ArrayList<>();
		divisions.add(mockNewDivision);
		corporateer.setMemberOfDivisions(divisions);

		corporateerHandlingService.setMainDivision(mockAuthentication, mockDivisionName);

		verify(mockCorporateerRepository).save(corporateerCaptor.capture());
		assertEquals(mockNewDivision, corporateerCaptor.getValue().getMainDivision());
	}

	@Test
	public void testSetMainDivisionIfNoMember() {
		String mockDivisionName = "mockDivisionName";
		Division mockDivision = mock(Division.class);
		Corporateer corporateer = new Corporateer();
		corporateer.setMainDivision(mockDivision);

		when(mockUserHandlingService.getUserByName(mockAuthentication.getName())).thenReturn(mockUser);
		when(mockUser.getCorporateer()).thenReturn(corporateer);
		when(mockDivision.getName()).thenReturn("someDivisionName");

		Division mockNewDivision = mock(Division.class);
		when(mockObjectService.getDivisionByName(mockDivisionName)).thenReturn(mockNewDivision);

		List<Division> divisions = new ArrayList<>();
		divisions.add(mockDivision);
		corporateer.setMemberOfDivisions(divisions);

		exception.expect(IllegalDivisionChangeRequestException.class);

		corporateerHandlingService.setMainDivision(mockAuthentication, mockDivisionName);
	}

	@Test
	public void testSetRank() {
		String corporateerName = "corporateerName";

		when(mockCorporateerRepository.findByName(corporateerName)).thenReturn(mockCorporateer);

		corporateerHandlingService.setRank(corporateerName, rankName);
		verify(mockCorporateer).setRank(mockRank);
		verify(mockCorporateerRepository).save(mockCorporateer);
	}

}
