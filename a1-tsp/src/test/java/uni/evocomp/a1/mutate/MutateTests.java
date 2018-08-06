package uni.evocomp.a1.mutate;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Runs all Mutate operator tests
 *
 * @author Namdrib
 */
@RunWith(Suite.class)
@SuiteClasses({InsertTest.class, InvertTest.class, SwapTest.class, JumpTest.class})
public class MutateTests {}
