package com.thecorporateer.influence.test.datahandling;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.security.core.Authentication;

import com.thecorporateer.influence.objects.ActionLog;
import com.thecorporateer.influence.objects.User;
import com.thecorporateer.influence.repositories.ActionLogRepository;
import com.thecorporateer.influence.services.ActionLogService;
import com.thecorporateer.influence.services.UserHandlingService;

public class TestLogService {

	@Mock
	private ActionLogRepository mockActionLogRepository;
	@Mock
	private UserHandlingService mockUserHandlingService;

	@Mock
	private Authentication mockAuthentication;
	@Mock
	private User mockUser;

	@Captor
	private ArgumentCaptor<ActionLog> actionLogCaptor;

	private String mockAction = "mockAction";

	@InjectMocks
	private ActionLogService actionLogService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		when(mockUserHandlingService.getUserByName(mockAuthentication.getName())).thenReturn(mockUser);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Rule
	public ExpectedException exception = ExpectedException.none();

	/**
	 * Test whether log gets created
	 */
	@Test
	public void testLogAction() {

		actionLogService.logAction(mockAuthentication, mockAction);

		verify(mockActionLogRepository).save(actionLogCaptor.capture());

		assertEquals(mockUser, actionLogCaptor.getValue().getUser());
		assertEquals(mockAction, actionLogCaptor.getValue().getAction());
	}
	
	/**
	 * Test whether log gets created
	 */
	@Test
	public void testGetAllLogs() {

		actionLogService.getAllLogs();

		verify(mockActionLogRepository).findAll();
	}

}
