package common.z;

import net.sourceforge.czt.z.ast.AxPara;


/**
 * Interface that represents a horizontal definition.
 * @author Pablo Rodriguez Monetti
 */
interface HorizDef extends AxPara{
	
	public void setMyAxPara(AxPara axPara);
	public AxPara getMyAxPara();
	public AxPara clone();
}


