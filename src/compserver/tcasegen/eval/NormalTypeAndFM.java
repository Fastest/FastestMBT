package compserver.tcasegen.eval;

import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.SetExpr;

    
/**
 * An instance of this class represents a pair (t,fm) where t is a a type and fm a finite
 * model. The type t is represented with an instance of Expr and the finite model fm is 
 * represented with an instance of SetExpr.
 * @author Pablo Rodriguez Monetti
 */          
public class NormalTypeAndFM{

	private SetExpr fM;
	private Expr type;

    /**
     * Creates an instance of NormalTypeAndFM
     * @param setExpr
     * @param expr
     */
	public NormalTypeAndFM(SetExpr setExpr, Expr expr){
		fM = setExpr;
		type = expr;
	}


    /**
     * Sets the instance of Expr.
     * @param expr
     */
	public void setNormalType(Expr expr){
		type = expr;
	}

    
    
    /**
     * Gets the instance of Expr.
     * @return the instance of Expr.
     */
	public Expr getNormalType(){
		return type;
	}

    
    
    /**
     * Sets the instance of SetExpr
     * @param setExpr
     */
	public void setFM(SetExpr setExpr){
		fM = setExpr;
	}

    
    
    /**
     * Gets the instance of SetExpr
     * @return the instance of SetExpr
     */
	public SetExpr getFM(){
		return fM;
	}
}