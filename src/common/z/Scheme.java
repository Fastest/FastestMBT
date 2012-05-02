package common.z;

import net.sourceforge.czt.z.ast.AxPara;


/**
 * Interface that abstracts a Z Scheme.
 * @author Pablo Rodriguez Monetti
 */
public interface Scheme extends AxPara{
	
	public void setMyAxPara(AxPara axPara);
	public AxPara getMyAxPara();
	public AxPara clone();

}


