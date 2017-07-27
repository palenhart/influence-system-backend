package com.thecorporateer.influence.test.all;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.thecorporateer.influence.test.datahandling.AllDataHandlingTests;
import com.thecorporateer.influence.test.transactions.AllTransactionTests;

@RunWith(Suite.class)
@SuiteClasses({ AllTransactionTests.class, AllDataHandlingTests.class })
public class AllTests {

}
