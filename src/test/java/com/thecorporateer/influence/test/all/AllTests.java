package com.thecorporateer.influence.test.all;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.thecorporateer.influence.test.datahandling.AllDataHandlingTests;

@RunWith(Suite.class)
@SuiteClasses({ AllDataHandlingTests.class })
//@SuiteClasses({ AllTransactionTests.class, AllDataHandlingTests.class, AllInfluenceTests.class })
public class AllTests {

}
