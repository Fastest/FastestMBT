package client.blogic.testing.refinement;

import java.io.*;
import java.util.*;

import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;

import common.z.AbstractTCase;
import common.z.AbstractTCaseImpl;

import client.blogic.management.ii.events.Event_;
import client.blogic.management.ii.IIComponent;
import client.blogic.management.ii.events.TCaseGenerated;
import client.blogic.management.ii.events.TCaseRefineRequested;
import client.blogic.management.ii.events.RefLawSelected;

public class TCaseRefClient extends IIComponent{

    public TCaseRefClient(){
    }

    public synchronized void manageEvent(Event_ event_)
		throws IllegalArgumentException{

	if (event_ instanceof TCaseRefineRequested){
        	TCaseRefineRequested tCaseRefineRequested = (TCaseRefineRequested) event_;
                String opName = tCaseRefineRequested.getOpName();
                AbstractTCase absTCase = tCaseRefineRequested.getAbstractTCase();
		String refLawName = tCaseRefineRequested.getRefLawName();
		String pathUUT = tCaseRefineRequested.getPathUUT();
		String targetLanguaje = tCaseRefineRequested.getTargetLanguaje();
                new TCaseRefClientRunner(opName, absTCase, pathUUT, targetLanguaje, refLawName);
	}
	else
	    throw new IllegalArgumentException();
	}

}