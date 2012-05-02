package common.z;

import net.sourceforge.czt.z.ast.AxPara;

/**
 * Interface that abstracts a Z axiomatic definition.
 * @author Pablo Rodriguez Monetti
 */
interface AxDef extends AxPara{
	
	public void setMyAxPara(AxPara axPara);
	public AxPara getMyAxPara();
	public AxPara clone();
}


