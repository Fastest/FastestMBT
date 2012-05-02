package common.z;

import net.sourceforge.czt.z.ast.AxPara;

/**
 * Interface that represents a test class.
 * @author Pablo Rodriguez Monetti
 */
public interface TClass extends Scheme{

	public void setSchName(String name);
	public String getSchName();
	public AxPara clone();

}


