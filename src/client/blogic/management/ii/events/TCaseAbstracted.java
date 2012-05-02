package client.blogic.management.ii.events;

import common.z.AbstractTCase;


public class TCaseAbstracted extends Event_{

	private String brotherName;
	private String opName;
	private AbstractTCase caseAbstracted;

	public TCaseAbstracted(AbstractTCase caseAbstracted, String brotherName, String opName){
		this.caseAbstracted = caseAbstracted;
		this.brotherName = brotherName;
		this.opName = opName;
		super.setEventName("tCaseAbstracted");
	}

	public String getBrotherName(){
		return brotherName;
	}

	public String getOpName(){
		return opName;
	}

	public AbstractTCase getAbstractTCase(){
		return caseAbstracted;
	}	
}
 
