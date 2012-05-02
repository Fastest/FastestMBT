package compserver.abstraction.capture.xmlmanagers;

import compserver.abstraction.AbstractionLaw;
import client.blogic.testing.refinement.ConcreteTCase;

/**
 * Interface from which inherit the modules that add code for capture the output of the UUT
 */
public interface ScriptTestWriter{
    /**
     * Adds the necessary code in the script to capture the output
     * @param script the script generated in the refinement stage
     * @param law the abstraction involved in this stage
     */
	public ConcreteTCase addCaptureCode(ConcreteTCase script, AbstractionLaw law);
}