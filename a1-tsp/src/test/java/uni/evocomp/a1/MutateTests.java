package uni.evocomp.a1;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Runs all Mutate operator tests
 * 
 * @author Namdrib
 *
 */
@RunWith(Suite.class)
@SuiteClasses({InsertTest.class, InvertTest.class, SwapTest.class})
public class MutateTests {
}
