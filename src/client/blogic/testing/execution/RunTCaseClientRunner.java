package client.blogic.testing.execution;

import java.util.*;
import java.io.*;
import java.net.URL;
import client.blogic.testing.refinement.ConcreteTCase;
import compserver.abstraction.capture.execution.CompilationInfo;
import client.blogic.management.ii.events.RunFinished;
import compserver.abstraction.AbstractionLaw;
import compserver.abstraction.capture.XMLParser;
import compserver.abstraction.capture.CapturedVar;
import common.z.AbstractTCase;
import net.sourceforge.czt.z.ast.AxPara;
import compserver.abstraction.tcasegeneration.*;
import client.blogic.management.ii.EventAdmin;

/**
 * Instances of this class has the responsibility of request, to a computation 
 * server, for the running of a concrete case and abstract the output. Each request must 
 * be done through a new instance of ServiceMediator, which is the only one that has the
 * knowledge about how the connection with the servers are implemented.
 */
public class RunTCaseClientRunner implements Runnable
{

    /**
     * Creates new instances of TClassPruneClientRunner
     * @param tClass
     */
	public RunTCaseClientRunner(String runCode, CompilationInfo compilationInfo){
		this.compilationInfo = compilationInfo;
		this.runCode = runCode;
	}

    /**
     * Requests the running of a concrete test and the subsequent abstraction of 
     * the output, either to the client itself or to a computation server.
     *  After a response arrives, it announces a TCaseAbstracted event.
     */
	public void run(){
	// Por ahora no considero el servidor
	try{
	// We obtain the working directory
	String workingDirectory = compilationInfo.getWorkingDirectory();
	if(!workingDirectory.endsWith(File.separator))
		workingDirectory += File.separator; 
	// We run the script test
	// El executer deberia devolver el status de la ejecucion!!!
	Executer.execute(runCode, workingDirectory);
	EventAdmin eventAdmin = EventAdmin.getInstance();
	RunFinished event = new RunFinished();
	eventAdmin.announceEvent(event);

	}
	catch(Exception e){
		e.printStackTrace(System.out);
	}
	
	}
	private CompilationInfo compilationInfo;
	private String runCode;
}
