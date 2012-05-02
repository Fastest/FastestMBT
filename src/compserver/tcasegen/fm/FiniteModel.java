package compserver.tcasegen.fm; 


import net.sourceforge.czt.z.ast.Expr;
import java.io.Serializable;

/**
 * This interface abstracts a finite model generator. A finite model generator is an 
 * structure associated to a particular type of the Z language and provides and iterator that 
 * can be used to obtain the elements associated to the finite model.
 * @author Pablo Rodriguez Monetti
 */
public interface FiniteModel extends Serializable{
    
    /**
     * Initializes the iterator related to the FiniteModel.
     */
	public void initialize();
    
    /**
     * Returns true whether the iteration has more elements.
     * @return true whether the iterator has more elements.
     */
	public boolean hasNext();
	
    /**
     * Returns the next element in the iteration.
     * @return the next element in the iteration.
     */
    public Expr next();
	
    /**
     * Returns the normalized type related to this FiniteModel.
     * @return
     */
    public Expr getNormalizedType();
	
    /**
     * Returns the finite model size of this finite model generator.
     * @return the finite model size of this finite model generator.
     */
    public int getFMSize();
	
    /**
     * Clones this object.
     * @return
     */
    public Object clone();
}