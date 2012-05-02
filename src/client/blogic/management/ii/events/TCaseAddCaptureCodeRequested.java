package client.blogic.management.ii.events;

import client.blogic.testing.refinement.ConcreteTCase;
import compserver.abstraction.AbstractionLaw;

public class TCaseAddCaptureCodeRequested extends Event_{

	public TCaseAddCaptureCodeRequested(ConcreteTCase cTCase, AbstractionLaw absLaw, String targetLanguaje){
		this.cTCase = cTCase;
		this.absLaw = absLaw;
		this.targetLanguaje = targetLanguaje;
		super.setEventName("tCaseAddCaptureCodeRequested");
	}

	/**
	* Gets the abstraction law associated to this object.
	* @return
	*/
	public AbstractionLaw getAbstractionLaw(){
		return absLaw;
	}

	/**
	* Gets the target languaje associated to this object.
	* @return
	*/
	public String getTargetLanguaje(){
		return targetLanguaje;
	}

	/**
	* Gets the concrete case associated to this object.
	* @return
	*/
	public ConcreteTCase getConcreteTCase(){
		return cTCase;
	}

	private ConcreteTCase cTCase;
	private AbstractionLaw absLaw;
	private String targetLanguaje;
}