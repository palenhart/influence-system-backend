package com.thecorporateer.influence.test.datahandling;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.thecorporateer.influence.repositories.CorporateerRepository;
import com.thecorporateer.influence.repositories.DepartmentRepository;
import com.thecorporateer.influence.repositories.DivisionRepository;
import com.thecorporateer.influence.repositories.InfluenceRepository;
import com.thecorporateer.influence.repositories.InfluenceTypeRepository;
import com.thecorporateer.influence.repositories.RankRepository;
import com.thecorporateer.influence.services.CorporateerHandlingService;

public class TestRefreshAll {

	@Mock
	private RankRepository rankRepository;
	@Mock
	private InfluenceTypeRepository mockInfluenceTypeRepository;
	@Mock
	private CorporateerRepository corporateerRepository;
	@Mock
	private DepartmentRepository mockDepartmentRepository;
	@Mock
	private DivisionRepository mockDivisionRepository;
	@Mock
	private InfluenceRepository mockInfluenceRepository;

	@InjectMocks
	private CorporateerHandlingService dataHandlingService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() throws Exception {
	}

}
