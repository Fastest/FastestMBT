package client.blogic.management.ii.events;

import client.blogic.testing.refinement.ConcreteTCase;
import compserver.abstraction.AbstractionLaw;
import net.sourceforge.czt.z.ast.AxPara;

public class TCaseAbstractRequested extends Event_{

	public TCaseAbstractRequested(ConcreteTCase cTCase, AbstractionLaw absLaw, AxPara originalAxPara){
		this.cTCase = cTCase;
		this.absLaw = absLaw;
		this.originalAxPara = originalAxPara;
		super.setEventName("tCaseAbstractRequested");
	}

	/**
	* Gets the abstraction law associated to this object.
	* @return
	*/
	public AbstractionLaw getAbstractionLaw(){
		return absLaw;
	}

	/**
	* Gets the AxPara related to this concrete case
	* @return
	*/
	public AxPara getOriginalAxPara(){
		return originalAxPara;
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
	private AxPara originalAxPara;
}