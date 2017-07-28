package com.thecorporateer.influence.test.datahandling;

import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.thecorporateer.influence.objects.Department;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.Rank;
import com.thecorporateer.influence.repositories.CorporateerRepository;
import com.thecorporateer.influence.repositories.DepartmentRepository;
import com.thecorporateer.influence.repositories.DivisionRepository;
import com.thecorporateer.influence.repositories.InfluenceTypeRepository;
import com.thecorporateer.influence.repositories.RankRepository;
import com.thecorporateer.influence.services.DataHandlingService;
import com.thecorporateer.influence.services.InitializationService;

public class TestInitializeObjects {

	@Mock
	private DataHandlingService mockDatahandler;

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

	@InjectMocks
	private InitializationService mockInitializationService;

	@Mock
	private List<Department> mockDepartments;
	@Mock
	private List<Rank> mockRanks;
	@Mock
	private List<Division> mockDivisions;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInitialization() {
		when(mockRankRepository.save(anyCollectionOf(Rank.class))).thenReturn(mockRanks);
		when(mockDivisionRepository.save(anyCollectionOf(Division.class))).thenReturn(mockDivisions);
		when(mockDepartmentRepository.save(anyCollectionOf(Department.class))).thenReturn(mockDepartments);
		mockInitializationService.initialize();
	}

}
