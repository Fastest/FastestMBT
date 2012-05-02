package compserver.tcasegen.fm; 

import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.Name;
import net.sourceforge.czt.z.ast.ZBranchList;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.impl.ZFactoryImpl;


import common.z.SpecUtils;

/**
 * An instance of this class represents a finite model generator associated to the free type
 * of Z.
 * @author Pablo Rodriguez Monetti
 */
public class FreetypeFiniteModel implements FiniteModel{

	private int size;
	private ZBranchList zBranchList;
	private Expr normalizedType;
	private int index;
	private ZFactory zFactory;

    /**
     * Creates an instance of this class.
     * @param zBranchList
     */
	public FreetypeFiniteModel(ZBranchList zBranchList){
		this.zBranchList = zBranchList;
		size = zBranchList.size();
		normalizedType = SpecUtils.getNumRefExpr();
		index = 0;
		zFactory = new ZFactoryImpl();
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

    
    /**
     * Returns the next element in the iteration.
     * @return the next element in the iteration.
     */    
	public Expr next(){
		Name name = zBranchList.get(index).getName();
		index++;
		return zFactory.createRefExpr(name, zFactory.createZExprList(), false, false);
	}


	public Expr getNormalizedType(){
		return normalizedType;
	}

	public int getFMSize(){
		return size;
	}

    @Override
	public Object clone(){
		return new FreetypeFiniteModel(zBranchList);
	}
}  
