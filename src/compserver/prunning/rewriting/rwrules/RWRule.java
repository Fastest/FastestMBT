package compserver.prunning.rewriting.rwrules; 

/**
 * Interface that abstracts a rewrite rule
 */
public interface RWRule{
    /**
     * Returns the regular expression that result of add to the original expression
     * the alternatives ways of writing
     * @param originalExpr
     * @return the generated regular expression 
     */
    public String rewrite(String originalExpr); 
} 