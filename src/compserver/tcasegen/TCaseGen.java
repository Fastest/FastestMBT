package compserver.tcasegen;

import net.sourceforge.czt.z.ast.Spec;

import common.z.AbstractTCase;
import common.z.TClass;
import common.z.SpecUtils;
import compserver.tcasegen.strategies.TCaseStrategy;


/**
 * An instance of this class allow the generation of an abstract test case, given a test
 * class, the specification and the strategy of test case generation.
 * @author Pablo Rodriguez Monetti
 */
public class TCaseGen{
    /**
     * Generates an abstract test case for the specified test class, the specification, and
     * the strategy for test case generation. If it is not possible to generate an abstract 
     * test case, null is returned.
     * @param spec
     * @param tClass
     * @param tCaseStrategy
     * @return the generated abstract test case, or null, if any could be generated.
     */
	public static synchronized AbstractTCase generateAbstractTCase(Spec spec, TClass tClass, TCaseStrategy tCaseStrategy){
		return tCaseStrategy.generateAbstractTCase(spec, tClass);
	}
}