package compserver.prunning.rewriting.rwrules; 

/**
 * Interface that abstracts a rewrite rule related with the properties of the operators (such
 * as asociativity, commutativity, etc)
 */
public interface RWRuleOperator extends RWRule{
    /**
     * Gets the name of the operator
     * @return the name of the operator
     */
    public String getOperator();
} 