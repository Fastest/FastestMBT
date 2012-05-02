package client.blogic.testing.abstraction;

import java.util.*;
import client.blogic.management.ii.events.TCaseAbstractRequested;
import client.blogic.management.ii.events.Event_;
import client.blogic.management.ii.EventAdmin;
import client.blogic.management.ii.IIComponent;
import client.blogic.testing.refinement.ConcreteTCase;
import compserver.abstraction.AbstractionLaw;
import net.sourceforge.czt.z.ast.AxPara;



/**
 * Intances of this class (although we assume there is only one in the system)
 * manages the requests for for the addition of the necessary code to capture 
 * the output of the monitored variables and create the appropiate factory
 * of "code manipulers". These requests are done in each event of type 
 * TCaseAbstractRequested that is announced in the system and each of them are
 * processed in a different thread (to favour performance issues), running
 * the method run() of TCaseAbsClientRunner in each new thread.
 */
public class TCaseAbsClient extends IIComponent {

    /**
     * Manages an implicit invocation event.
     * @param event_
     * @throws java.lang.IllegalArgumentException if event_ is not instance of
     * PruneTClassRequested
     */
	public synchronized void manageEvent(Event_ event_)
		throws IllegalArgumentException{

		if(event_ instanceof TCaseAbstractRequested){
			TCaseAbstractRequested absRequest = (TCaseAbstractRequested)event_;
			AxPara originalAxPara = absRequest.getOriginalAxPara();
			ConcreteTCase cTCase = absRequest.getConcreteTCase();
			AbstractionLaw absLaw = absRequest.getAbstractionLaw();

			(new Thread(new TCaseAbsClientRunner(cTCase,absLaw,originalAxPara))).start();
		}
		else
			throw new IllegalArgumentException();
	}
}
 
