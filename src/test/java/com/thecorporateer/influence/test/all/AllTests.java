package com.thecorporateer.influence.test.all;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.thecorporateer.influence.test.datahandling.AllDataHandlingTests;
import com.thecorporateer.influence.test.influence.AllInfluenceTests;

@RunWith(Suite.class)
@SuiteClasses({ AllDataHandlingTests.class, AllInfluenceTests.class })
//@SuiteClasses({ AllTransactionTests.class, AllDataHandlingTests.class, AllInfluenceTests.class })
public class AllTests {

}
