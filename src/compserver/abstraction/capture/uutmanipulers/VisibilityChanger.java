package compserver.abstraction.capture.uutmanipulers;

import java.util.*;
import compserver.abstraction.types.impltypes.ImplNode;

/**
 * Interface from which inherit the modules that change the visibility of the 
 * monitored variables
 */
public interface VisibilityChanger {

	public void changeVisibility(String uutPath, List<ImplNode> monitoredVars);

}