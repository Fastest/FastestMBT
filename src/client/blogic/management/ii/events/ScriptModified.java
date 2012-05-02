package client.blogic.management.ii.events;

import client.blogic.testing.refinement.ConcreteTCase;
import compserver.abstraction.AbstractionLaw;

public class ScriptModified extends Event_{

	public ScriptModified(ConcreteTCase cTCase){
		this.cTCase = cTCase;
		//this.absLaw = absLaw;
		//this.targetLanguaje = targetLanguaje;
		super.setEventName("scriptModified");
	}

	/**
	* Gets the concrete case associated to this object.
	* @return
	*/
	public ConcreteTCase getConcreteTCase(){
		return cTCase;
	}

	private ConcreteTCase cTCase;
	//private AbstractionLaw absLaw;
	//private String targetLanguaje;
}