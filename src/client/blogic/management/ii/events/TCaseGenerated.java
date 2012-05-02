package client.blogic.management.ii.events;

import common.z.AbstractTCase;
import common.z.TClass;

public class TCaseGenerated extends Event_{

	private String opName;
	private TClass tClass;
    private AbstractTCase abstractTCase;

	public TCaseGenerated(String opName, TClass tClass, AbstractTCase abstractTCase){
		this.opName = opName;
		this.tClass = tClass;
        this.abstractTCase = abstractTCase;
		super.setEventName("tCaseGenerated");
	}

	public void setOpName(String opName){
		this.opName = opName;
	}

	public String getOpName(){
		return opName;
	}

	public void setTClass(TClass tClass){
		this.tClass = tClass;
	}

	public TClass getTClass(){
		return tClass;
	}


	public void setAbstractTCase(AbstractTCase abstractTCase){
		this.abstractTCase = abstractTCase;
	}

	public AbstractTCase getAbstractTCase(){
		return abstractTCase;
	}	



}
 
