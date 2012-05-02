package client.blogic.management.ii.events;

import client.blogic.testing.refinement.ConcreteTCase;
import common.z.AbstractTCase;

/**
 * @author Hache
 */

public class TCaseRefined extends Event_{
    private String opName;
    private AbstractTCase absTCase;
    private ConcreteTCase concreteTCase;

    public TCaseRefined(String opName, AbstractTCase absTCase, ConcreteTCase concreteTCase){
        this.opName = opName;
	this.absTCase = absTCase;
        this.concreteTCase = concreteTCase;
	super.setEventName("tCaseRefined");
    }

    public void setOpName(String opName){
        this.opName = opName;
    }

    public String getOpName(){
        return opName;
    }

    public void setAbstractTCase(AbstractTCase absTCase){
        this.absTCase = absTCase;
    }

    public AbstractTCase getAbstractTCase(){
        return absTCase;
    }


    public void setConcreteTCase(ConcreteTCase concreteTCase){
        this.concreteTCase = concreteTCase;
    }

    public ConcreteTCase getConcreteTCase(){
        return concreteTCase;
    }
}
