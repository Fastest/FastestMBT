package compserver.tcasegen.fm.natfm; 

import java.math.*;
import java.util.*;

import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZNumeral;
import net.sourceforge.czt.z.impl.ZFactoryImpl;

import common.z.SpecUtils;

/*
 * An instance of this class represents a finite model generator associated to the type of 
 * the Z specification language that corresponds to the integer numbers. In particular, this
 * generator considers the integers that are close to zero: 0, 1, 2, 3, ...until
 * a number of naturals equal to the specified finite model size is considered.
 * @author Pablo Rodriguez Monetti
 */
public class ZeroNatFiniteModel implements NatFiniteModel{

	private int size;
	private int index;
	private Expr normalizedType;
	private ZFactory zFactory;

	public ZeroNatFiniteModel(){
        
		this.size = 0;
		index = 0;
		normalizedType = SpecUtils.getNumRefExpr();
		zFactory = new ZFactoryImpl();
	}
    
    
	public ZeroNatFiniteModel(int fMSize){
        this.size = fMSize;
		index = 0;
		normalizedType = SpecUtils.getNumRefExpr();
		zFactory = new ZFactoryImpl();
	}  
    
    
    
    public void initialize(List<BigInteger> arrayList){

	} 
    
    
    public void initialize(int fMSize){
        this.size = fMSize;
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
		ZNumeral zNumeral = zFactory.createZNumeral(BigInteger.valueOf(index));
		index++;
		return zFactory.createNumExpr(zNumeral);
	}

    
    
	public Expr getNormalizedType(){
		return normalizedType;
	}

    
    
	public int getFMSize(){
		return size;
	}

    @Override
	public Object clone(){
		NatFiniteModel natFiniteModel = new ZeroNatFiniteModel();
        natFiniteModel.initialize(size);
        return natFiniteModel;
	}
}