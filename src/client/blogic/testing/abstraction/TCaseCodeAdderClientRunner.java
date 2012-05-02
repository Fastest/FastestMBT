package client.blogic.testing.abstraction;

import java.util.*;
import client.blogic.testing.refinement.ConcreteTCase;
import compserver.abstraction.AbstractionLaw;
import compserver.abstraction.capture.xmlmanagers.ScriptTestWriter;
import compserver.abstraction.capture.xmlmanagers.ScriptTestWriterC;
import compserver.abstraction.AbstractionUtils;
import compserver.abstraction.types.impltypes.ImplNode;
import compserver.abstraction.capture.uutmanipulers.VisibilityChanger;
import compserver.abstraction.capture.uutmanipulers.VisibilityChangerC;

import client.blogic.management.ii.events.ScriptModified;
import client.blogic.management.ii.EventAdmin;

/**
 * Instances of this class has the responsibility of request, to a computation 
 * server, for the addition of the necessary code to capture the output of the
 */
public class TCaseCodeAdderClientRunner implements Runnable
{

    /**
     * Creates new instances of TCaseCodeAdderClientRunner
     * @param tClass
     */
	public TCaseCodeAdderClientRunner(ConcreteTCase cTCase, AbstractionLaw absLaw, ManipulersFactory factory){
		this.cTCase = cTCase;
		this.absLaw = absLaw;
		this.factory = factory;
	}

    /**
     * Requests the abstraction of a test case either to the client 
     * itself or to a computation server server. After a response arrives, it
     * announces a ScriptModified event.
     */
	public void run(){
		// Por ahora no considero el servidor
		ConcreteTCase newScript;

		// We create the corresponding objects
		VisibilityChanger changer = factory.createVisibilityChanger();
		ScriptTestWriter writer = factory.createScriptTestWriter();

		// We change the visibility of the UUT
		String pathUUT = cTCase.getPathUUT();
		List<ImplNode> monitoredVars = AbstractionUtils.getMonitoredVariables(absLaw);
		//changer.changeVisibility(pathUUT,monitoredVars);

		// We add the capture code to the script
		newScript = writer.addCaptureCode(cTCase,absLaw);

		// We set the name of the abstraction law being used
		newScript.setAbsLawName(absLaw.getLawName()); 

		//System.out.println("Script modificado\n"+newScript);

		try{
			EventAdmin eventAdmin = EventAdmin.getInstance();
			ScriptModified event = new ScriptModified(newScript);
			eventAdmin.announceEvent(event);
		}
		catch(Exception e){
			System.out.println("ExceptioN");
			e.printStackTrace(System.out);
		}

	}
	private ConcreteTCase cTCase;
	private AbstractionLaw absLaw;
	private ManipulersFactory factory;
}
