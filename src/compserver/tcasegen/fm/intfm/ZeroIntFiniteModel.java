package compserver.tcasegen.fm.intfm; 

import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.ast.ZNumeral;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.NumExpr;
import net.sourceforge.czt.z.impl.ZFactoryImpl;

import common.z.SpecUtils;

import java.util.*;
import java.math.*;

/*
 * An instance of this class represents a finite model generator associated to the type of 
 * the Z specification language that corresponds to the integer numbers. In particular, this
 * generator considers the integers that are close to zero: 0, -1, 1, -2, 2, -3, ...until
 * a number of integers equal to the specified finite model size is considered.
 * @author Pablo Rodriguez Monetti
 */
public class ZeroIntFiniteModel implements IntFiniteModel{

	private int size;
	private int index;
	private Expr normalizedType;
	private ZFactory zFactory;

    /**
     * Creates an instance of this class.
     */
	public ZeroIntFiniteModel(){
        this.size = 0;
		index = 0;
		normalizedType = SpecUtils.getNumRefExpr();
		zFactory = new ZFactoryImpl();
	}
    
    /**
     * Creates an instance of this class.
     * @param fMSize
     */
	public ZeroIntFiniteModel(int fMSize){
        this.size = fMSize;
		index = 0;
		normalizedType = SpecUtils.getNumRefExpr();
		zFactory = new ZFactoryImpl();
	}    
    
    
    public void initialize(List<BigInteger> list){

	} 
    
    
    
    public void initialize(int fMSize){
        this.size = fMSize;
    }        
    
    
    
    /**
     * Initializes the iterator related to this FiniteModel..
     */      
	public void initialize(){
		index = 0;
	}

    
    /**
     * Returns true whether the iteration has more elements.
     * @return true whether the iterator has more elements.
     */      
	public boolean hasNext(){
		if(index == 0)
			return (size>0);
		else
			return (2 * Math.abs(index) -1 < size);
	}

	public Expr next(){
		ZNumeral zNumeral = zFactory.createZNumeral(BigInteger.valueOf(Math.abs(index)));
		NumExpr numExpr = zFactory.createNumExpr(zNumeral);
		if(index >= 0){
			index = -index - 1;
			return numExpr;
		}
		else{
			ZName zName = zFactory.createZName("- _ ", zFactory.createZStrokeList(), "neg");
			RefExpr negRefExpr = zFactory.createRefExpr(zName, zFactory.createZExprList(), false, false);
			ZExprList zExprList = zFactory.createZExprList();
			zExprList.add(negRefExpr);
			zExprList.add(numExpr);
			index = -index;
			return zFactory.createApplExpr(zExprList, true);
		}	
	}

	public Expr getNormalizedType(){
		return normalizedType;
	}

	public int getFMSize(){
		return size;
	}

    @Override
	public Object clone(){
        IntFiniteModel intFiniteModel = new ZeroIntFiniteModel();
        intFiniteModel.initialize(size);
        return intFiniteModel;
	}
} 