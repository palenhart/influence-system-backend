package com.thecorporateer.influence.test.datahandling;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.objects.Rank;
import com.thecorporateer.influence.repositories.CorporateerRepository;
import com.thecorporateer.influence.services.DataHandlingService;

public class TestInfluenceDistribution {
	
	@Mock
	private CorporateerRepository mockCorporateerRepository;
	@Mock
	private Corporateer corporateer1;
	@Mock
	private Corporateer corporateer2;
	@Mock
	private Corporateer corporateer3;
	
	@Captor
	private ArgumentCaptor<List<Influence>> influenceCaptor;

	@InjectMocks
	private DataHandlingService mockDataHandlingService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInfluenceDistribution() {
		List<Corporateer> corporateers = new ArrayList<>();
		corporateers.add(corporateer1);
		corporateers.add(corporateer2);
		corporateers.add(corporateer3);
		
		when(mockCorporateerRepository.findAll()).thenReturn(corporateers);
		
		when(corporateer1.getTributes()).thenReturn(0);
		when(corporateer2.getTributes()).thenReturn(1);
		when(corporateer3.getTributes()).thenReturn(2);
		
		Rank rank1 = mock(Rank.class);
		Rank rank2 = mock(Rank.class);
		Rank rank3 = mock(Rank.class);
		
		when(rank1.getTributesPerWeek()).thenReturn(5);
		when(rank2.getTributesPerWeek()).thenReturn(10);
		when(rank3.getTributesPerWeek()).thenReturn(15);
		
		when(corporateer1.getRank()).thenReturn(rank1);
		when(corporateer2.getRank()).thenReturn(rank2);
		when(corporateer3.getRank()).thenReturn(rank3);
		
		mockDataHandlingService.distributeInfluence();
		
		verify(corporateer1).setTributes(5);
		verify(corporateer2).setTributes(11);
		verify(corporateer3).setTributes(17);
	}

}
