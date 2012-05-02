package client.blogic.testing.execution;

import java.util.*;
import client.blogic.management.ii.events.CTCaseRunRequested;
import client.blogic.management.ii.events.Event_;
import client.blogic.management.ii.EventAdmin;
import client.blogic.management.ii.IIComponent;
import client.blogic.testing.refinement.ConcreteTCase;
import compserver.abstraction.capture.execution.CompilationInfo;
import compserver.abstraction.AbstractionLaw;
import net.sourceforge.czt.z.ast.AxPara;


/**
 * Intances of this class (although we assume there is only one in the system)
 * manages the requests for execute concrete test cases . These requests are
 * done in each event of type CTCaseRunRequested that is announced in the system and
 * each of them are processed in a different thread (to favour performance 
 * issues), running the method run() of RunTCaseClientRunner in each new thread.
 */
public class RunTCaseClient extends IIComponent {

    /**
     * Manages an implicit invocation event.
     * @param event_
     * @throws java.lang.IllegalArgumentException if event_ is not instance of
     * PruneTClassRequested
     */
	public synchronized void manageEvent(Event_ event_)
		throws IllegalArgumentException{

		if(event_ instanceof CTCaseRunRequested){
			CTCaseRunRequested runRequest = (CTCaseRunRequested)event_;
			CompilationInfo compilationInfo = runRequest.getCompilationInfo();
			String runCode = runRequest.getRunCode();
			(new Thread(new RunTCaseClientRunner(runCode, compilationInfo))).start();
		}
		else
			throw new IllegalArgumentException();
	}
}
 
