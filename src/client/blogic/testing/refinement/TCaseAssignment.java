package client.blogic.testing.refinement;

/**
 * Instances of this class stores information of the refinement code related to a
 * variable of the specification
 * @author Hache
 */

public class TCaseAssignment {
    private String specName, refText;
    public TCaseAssignment(String sn, String rt){
        specName = sn;
        refText = rt;
    }
    
    public String getSpecName(){
        return specName;
    }
    public String getRefText(){
        return refText;
    }

    public void setSpecName(String sn){
        specName = sn;
    }

    public void setRefText(String rt){
        refText = rt;
    }
}
