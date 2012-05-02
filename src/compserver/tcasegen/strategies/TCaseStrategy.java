package compserver.tcasegen.strategies; 

import java.io.Serializable;

import common.z.AbstractTCase;
import common.z.TClass;


import net.sourceforge.czt.z.ast.Spec;

/**
 * This interface represents an strategy of test case generation.
 * @author Pablo Rodriguez Monetti
 */
public interface TCaseStrategy extends Serializable{
    /**
     * Tries to generate an abstract test case for the specified test class and returns it if
     * the creation was possible, null if not.
     * @param spec
     * @param tClass
     * @return the generated abstract test case 
     */
	public AbstractTCase generateAbstractTCase(Spec spec, TClass tClass);
}
