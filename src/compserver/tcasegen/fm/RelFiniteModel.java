package compserver.tcasegen.fm; 

import java.util.*;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.PowerExpr;

import common.z.SpecUtils;


/**
 * An instance of this class represents a finite model generator associated to the type of 
 * the Z specification language that corresponds to the relations.
 * @author Pablo Rodriguez Monetti
 */
public class RelFiniteModel implements FiniteModel{

	private int size;
	private Expr normalizedType;
	private PowerFiniteModel powerFiniteModel;
	private FiniteModel leftFiniteModel;
	private FiniteModel rightFiniteModel;

    
    /**
     * Creates an instance of RelFiniteModel.
     * @param leftFiniteModel
     * @param rightFiniteModel
     */
	public RelFiniteModel(FiniteModel leftFiniteModel, FiniteModel rightFiniteModel){

		this.leftFiniteModel = leftFiniteModel;
		this.rightFiniteModel = rightFiniteModel;

		List<FiniteModel> finiteModelList = new ArrayList<FiniteModel>();
		finiteModelList.add(leftFiniteModel);
		finiteModelList.add(rightFiniteModel);
		powerFiniteModel = new PowerFiniteModel(new ProdFiniteModel(finiteModelList));
		
		size = powerFiniteModel.getFMSize();
		normalizedType = powerFiniteModel.getNormalizedType();
	}

    
    /**
     * Returns true whether the iteration has more elements.
     * @return true whether the iterator has more elements.
     */
    public void initialize(){
		powerFiniteModel.initialize();
	}
    

    
    /**
     * Returns true if the iteration has more elements.
     * @return true if the iterator has more elements.
     */    
	public boolean hasNext(){
		return powerFiniteModel.hasNext();
	}

	public Expr next(){
		return powerFiniteModel.next();
	}

	public Expr getNormalizedType(){
		return normalizedType;
	}

	public int getFMSize(){
		return size;
	}

    @Override
	public Object clone(){
		FiniteModel newLeftFiniteModel = (FiniteModel) leftFiniteModel.clone();
		FiniteModel newRightFiniteModel = (FiniteModel) rightFiniteModel.clone();
		return new RelFiniteModel(newLeftFiniteModel, newRightFiniteModel);
	}

}