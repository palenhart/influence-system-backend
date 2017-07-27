package com.thecorporateer.influence.test.datahandling;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Department;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.InfluenceType;
import com.thecorporateer.influence.objects.Rank;
import com.thecorporateer.influence.repositories.CorporateerRepository;
import com.thecorporateer.influence.repositories.DepartmentRepository;
import com.thecorporateer.influence.repositories.DivisionRepository;
import com.thecorporateer.influence.repositories.InfluenceRepository;
import com.thecorporateer.influence.repositories.InfluenceTypeRepository;
import com.thecorporateer.influence.repositories.RankRepository;
import com.thecorporateer.influence.services.DataHandlingService;

public class TestInitializeInfluence {

	@Mock
	private RankRepository mockRankRepository;
	@Mock
	private InfluenceTypeRepository mockInfluenceTypeRepository;
	@Mock
	private CorporateerRepository mockCorporateerRepository;
	@Mock
	private DepartmentRepository mockDepartmentRepository;
	@Mock
	private DivisionRepository mockDivisionRepository;
	@Mock
	private InfluenceRepository mockInfluenceRepository;

	@Mock
	private List<Rank> mockRanks = new ArrayList<>();
	@Mock
	private List<InfluenceType> mockTypes = new ArrayList<>();
	@Mock
	private List<Department> mockDepartments = new ArrayList<>();
	@Mock
	private List<Division> mockDivisions = new ArrayList<>();
	@Mock
	private List<Corporateer> mockCorporateers = new ArrayList<>();

	// lists to test with
	private List<Rank> ranks = new ArrayList<>();
	private List<InfluenceType> types = new ArrayList<>();
	private List<Department> departments = new ArrayList<>();
	private List<Division> divisions = new ArrayList<>();
	private List<Corporateer> corporateers = new ArrayList<>();

	@InjectMocks
	private DataHandlingService mockDataHandlingService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		Division mockNoDivision = mock(Division.class);
		when(mockNoDivision.getId()).thenReturn(1L);
		
		ranks.add(mock(Rank.class));
		ranks.add(mock(Rank.class));
		types.add(mock(InfluenceType.class));
		types.add(mock(InfluenceType.class));
		departments.add(mock(Department.class));
		departments.add(mock(Department.class));
		divisions.add(mock(Division.class));
		divisions.add(mockNoDivision);
		corporateers.add(mock(Corporateer.class));
		corporateers.add(mock(Corporateer.class));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRefresh() {
		mockDataHandlingService.refreshAll();
	}

	@Test
	public void testInitializeInfluence() {
		when(mockInfluenceTypeRepository.findAll()).thenReturn(types);
		when(mockDepartmentRepository.findAll()).thenReturn(departments);
		when(mockDivisionRepository.findAll()).thenReturn(divisions);
		mockDataHandlingService.initializeInfluenceTable(mock(Corporateer.class));
	}

}
