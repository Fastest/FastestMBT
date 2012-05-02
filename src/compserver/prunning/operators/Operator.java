package compserver.prunning.operators;

    /**
     * An interface that represent a Fastest's operator
     */
public interface Operator { 
    /**
     * Replace a line with a Fastest's operator for an alternative one with the regular
     * expression coresponding to this operator
     * @param originalPred The original atomic predicate
     */
	public String addSemantic(String originalPred);

}