package compserver.tcasegen.fm; 

import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.ast.ZFactory;

import common.z.SpecUtils;



/**
 * An instance of this class represents a finite model generator associated to the type of 
 * the Z specification language that corresponds to the sets defined by extension.
 * @author Pablo Rodriguez Monetti
 */
public class SetFiniteModel implements FiniteModel{

	private int size;
	private int index;
	private Expr normalizedType;
	private ZExprList zExprList;

    
    /**
     * Creates an instance of this class.
     * @param zExprList
     */
	public SetFiniteModel(ZExprList zExprList){
		this.zExprList = zExprList;
		this.size = zExprList.size();
		index = 0;
		ZFactory zFactory = new ZFactoryImpl();
		normalizedType = zFactory.createPowerExpr(SpecUtils.getNumRefExpr());
	}

    
    /**
     * Initializes the iterator related to this FiniteModel.
     */
	public void initialize(){
		index = 0;
	}

    
    
    /**
     * Returns true whether the iteration has more elements.
     * @return true whether the iterator has more elements.
     */
	public boolean hasNext(){
		return (index < size);
	}

    
	public Expr next(){
		Expr expr = zExprList.get(index);	
		index++;
		return expr;
	}

    
	public Expr getNormalizedType(){
		return normalizedType;
	}
    

	public int getFMSize(){
		return size;
	}
    

    @Override
	public Object clone(){
		return new SetFiniteModel(zExprList);
	}
} 
