package langdetect;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test Suite for whole project
 * 
 * @author Tobias Falke
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ NGramTest.class, ProfileTest.class, TokenizerTest.class })
public class Tests {

}
