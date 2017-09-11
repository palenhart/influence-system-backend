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
import com.thecorporateer.influence.objects.Department;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.objects.InfluenceType;
import com.thecorporateer.influence.objects.Rank;
import com.thecorporateer.influence.repositories.CorporateerRepository;
import com.thecorporateer.influence.repositories.DepartmentRepository;
import com.thecorporateer.influence.repositories.DivisionRepository;
import com.thecorporateer.influence.repositories.InfluenceRepository;
import com.thecorporateer.influence.repositories.InfluenceTypeRepository;
import com.thecorporateer.influence.repositories.RankRepository;
import com.thecorporateer.influence.services.CorporateerHandlingService;

public class TestCorporateerCreation {

	@Mock
	private CorporateerRepository mockCorporateerRepository;
	@Mock
	private DivisionRepository mockDivisionRepository;
	@Mock
	private DepartmentRepository mockDepartmentRepository;
	@Mock
	private RankRepository mockRankRepository;
	@Mock
	private InfluenceTypeRepository mockInfluenceTypeRepository;
	@Mock
	private InfluenceRepository mockInfluenceRepository;

	@Captor
	private ArgumentCaptor<Corporateer> corporateerCaptor;
	@Captor
	private ArgumentCaptor<List<Influence>> influenceCaptor;

	@InjectMocks
	private CorporateerHandlingService dataHandlingService;

	// lists to test with
	private List<Rank> ranks = new ArrayList<>();
	private List<InfluenceType> types = new ArrayList<>();
	private List<Department> departments = new ArrayList<>();
	private List<Division> divisions = new ArrayList<>();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		Division mockNoDivision = mock(Division.class);
		when(mockNoDivision.getId()).thenReturn(1L);

		Department mockNoDepartment = mock(Department.class);
		when(mockNoDepartment.getId()).thenReturn(1L);

		ranks.add(mock(Rank.class));
		ranks.add(mock(Rank.class));
		types.add(mock(InfluenceType.class));
		types.add(mock(InfluenceType.class));
		departments.add(mock(Department.class));
		departments.add(mock(Department.class));
		divisions.add(mock(Division.class));
		divisions.add(mockNoDivision);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void testCreateCorporateer() {
		Rank mockRank = mock(Rank.class);
		when(mockRankRepository.findOne(1L)).thenReturn(mockRank);

		Division mockDivision = mock(Division.class);
		when(mockDivisionRepository.findOne(1L)).thenReturn(mockDivision);

		when(mockInfluenceTypeRepository.findAll()).thenReturn(types);
		when(mockDepartmentRepository.findAll()).thenReturn(departments);
		when(mockDivisionRepository.findAll()).thenReturn(divisions);

		dataHandlingService.createCorporateer("Corporateer");

		verify(mockCorporateerRepository).save(corporateerCaptor.capture());
		assertEquals("Corporateer name saved incorrectly", "Corporateer", corporateerCaptor.getValue().getName());
		assertEquals("Corporateer rank saved incorrectly", mockRank, corporateerCaptor.getValue().getRank());
		assertEquals("Corporateer division saved incorrectly", mockDivision,
				corporateerCaptor.getValue().getMainDivision());
		assertEquals("Corporateer tributes initialized incorrectly", 0, corporateerCaptor.getValue().getTributes());

		verify(mockInfluenceRepository).save(influenceCaptor.capture());
		assertEquals("List of influences has wrong size", 6, influenceCaptor.getValue().size());
	}

}
