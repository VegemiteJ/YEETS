package uni.evocomp.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ClassFromNameTest.class, EuclideanDistance2DIntegerPairTest.class,
    EuclideanDistance2DPointTest.class, InBoundsTest.class})
public class UtilTests {

}
