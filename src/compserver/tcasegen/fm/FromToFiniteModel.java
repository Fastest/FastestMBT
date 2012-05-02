
package compserver.tcasegen.fm; 

import java.math.*;

import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.ast.ZNumeral;
import net.sourceforge.czt.z.ast.NumExpr;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.impl.ZFactoryImpl;


import common.z.SpecUtils;

/**
 * An instance of this class represents a finite model generator associated to the type of the
 * Z specification language that corresponds to a range of values of the form a..b.
 * @author Pablo Rodriguez Monetti
 */
public class FromToFiniteModel implements FiniteModel{

	private int size;
	private int index;
	private Expr normalizedType;
	private int leftValue;
	private int rightValue;
	private ZFactory zFactory;


    /**
     * Creates an instance of this class.
     * @param leftValue
     * @param rightValue
     */
	public FromToFiniteModel(int leftValue, int rightValue){
		this.leftValue = leftValue;
		this.rightValue = rightValue;
		this.size = rightValue - leftValue + 1;
		this.normalizedType = SpecUtils.getNumRefExpr();
		this.index = leftValue;
		zFactory = new ZFactoryImpl();

	}
    
    
    /**
     * Initializes the iterator related to this FiniteModel.
     */
	public void initialize(){
		index = leftValue;
	}

    /**
     * Returns true whether the iteration has more elements.
     * @return true whether the iterator has more elements.
     */
	public boolean hasNext(){
		return (index <= rightValue);
	}

    
    /**
     * Returns the next element in the iteration.
     * @return the next element in the iteration.
     */    
	public Expr next(){
		ZNumeral zNumeral = zFactory.createZNumeral(BigInteger.valueOf(index));
		NumExpr numExpr = zFactory.createNumExpr(zNumeral);
		Expr expr = null;
		if(index >= 0){
			expr = numExpr;
		}
		else{
			ZName zName = zFactory.createZName("- _ ", zFactory.createZStrokeList(), "neg");
			RefExpr negRefExpr = zFactory.createRefExpr(zName, zFactory.createZExprList(), false, false);
			ZExprList zExprList = zFactory.createZExprList();
			zExprList.add(negRefExpr);
			zExprList.add(numExpr);
			expr = zFactory.createApplExpr(zExprList, true);
		}	
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
		return new FromToFiniteModel(leftValue, rightValue);
	}
} 