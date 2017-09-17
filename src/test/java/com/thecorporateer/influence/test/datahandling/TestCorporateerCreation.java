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
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.objects.InfluenceType;
import com.thecorporateer.influence.objects.Rank;
import com.thecorporateer.influence.repositories.CorporateerRepository;
import com.thecorporateer.influence.services.CorporateerHandlingService;
import com.thecorporateer.influence.services.InfluenceHandlingService;
import com.thecorporateer.influence.services.ObjectService;

public class TestCorporateerCreation {

	@Mock
	private CorporateerRepository mockCorporateerRepository;
	@Mock
	private ObjectService mockObjectService;
	@Mock
	private InfluenceHandlingService mockInfluenceHandlingService;

	@Captor
	private ArgumentCaptor<Corporateer> corporateerCaptor;
	@Captor
	private ArgumentCaptor<List<Influence>> influenceCaptor;

	@InjectMocks
	private CorporateerHandlingService mockCorporateerHandlingService;

	// lists to test with
	private List<InfluenceType> types = new ArrayList<>();
	private List<Division> divisions = new ArrayList<>();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		types.add(mock(InfluenceType.class));
		types.add(mock(InfluenceType.class));
		divisions.add(mock(Division.class));
		divisions.add(mock(Division.class));
		divisions.add(mock(Division.class));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void testCreateCorporateer() {
		Rank mockRank = mock(Rank.class);
		when(mockObjectService.getLowestRank()).thenReturn(mockRank);

		Division mockDivision = mock(Division.class);
		when(mockObjectService.getDefaultDivision()).thenReturn(mockDivision);

		when(mockObjectService.getAllInfluenceTypes()).thenReturn(types);
		when(mockObjectService.getAllDivisions()).thenReturn(divisions);

		mockCorporateerHandlingService.createCorporateer("Corporateer");

		verify(mockCorporateerRepository).save(corporateerCaptor.capture());
		assertEquals("Corporateer name saved incorrectly", "Corporateer", corporateerCaptor.getValue().getName());
		assertEquals("Corporateer rank saved incorrectly", mockRank, corporateerCaptor.getValue().getRank());
		assertEquals("Corporateer division saved incorrectly", mockDivision,
				corporateerCaptor.getValue().getMainDivision());
		assertEquals("Corporateer tributes initialized incorrectly", 0, corporateerCaptor.getValue().getTributes());

		verify(mockInfluenceHandlingService).updateInfluences(influenceCaptor.capture());
		assertEquals("List of influences has wrong size", 6, influenceCaptor.getValue().size());
	}

}
