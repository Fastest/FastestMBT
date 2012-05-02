package compserver.tcasegen.fm.natfm; 

import java.util.*;
import java.math.*;
import java.util.ArrayList;

import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.ast.ZNumeral;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.NumExpr;
import net.sourceforge.czt.z.impl.ZFactoryImpl;

import common.z.SpecUtils;



/**
 * An instance of this class represents a finite model generator associated to the type of 
 * the Z specification language that corresponds to the integers. In particular, this
 * generator takes the list of integers over which it will iterate, also considering an
 * integer one unit greater than the maximun integer of the list and another integer one unit
 * lower than the minimum.
 * @author Pablo Rodriguez Monetti
 */
public class GivenNatFiniteModel implements NatFiniteModel{

	private int size;
	private int index;
	private Expr normalizedType;
	private ZFactory zFactory;
    private List<BigInteger> integerList;

    
    /**
     * Creates an instance of this class.
     */
	public GivenNatFiniteModel(){
    }
    
    
    /**
     * Creates an instance of this class.
     * @param arrayList
     */
    public GivenNatFiniteModel(List<BigInteger> integerList){
        this.integerList = integerList;
        this.size = integerList.size();
		index = 0;
		normalizedType = SpecUtils.getNumRefExpr();
        zFactory = new ZFactoryImpl();
	}
    
    
    public void initialize(List<BigInteger> integerList){
        this.integerList = integerList;
        // We delete negatives
        for(int i=0; i<integerList.size(); i++){
            if(integerList.get(i).intValue() < 0){
                integerList.remove(i);
                i--;
            }
        }        
        
        // We look for max and min of the list and delete duplicates
        int min,max;
        min = max = integerList.get(0).intValue();
        for(int i=0; i<integerList.size(); i++){
            int auxValue = integerList.get(i).intValue();
            if(min > auxValue)
                min = auxValue;
            if(max < auxValue)
                max = auxValue;
            boolean found = false;
            for(int j=0; j<i && !found; j++)
                if(auxValue == integerList.get(j).intValue())
                    found = true;
            if(found){
                integerList.remove(i);
                i--;
            }
        }
        // We do not want to add negative numbers to the list
        if(min>0)
            integerList.add(0,BigInteger.valueOf(min - 1));

        integerList.add(0,BigInteger.valueOf(max + 1));

		this.size = integerList.size();
		index = 0;
		normalizedType = SpecUtils.getNumRefExpr();

        zFactory = new ZFactoryImpl();
	}
    
    
    public void initialize(int fMSize){
        
    } 
    
    
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
        int value = integerList.get(index).intValue();
        index++;        
		ZNumeral zNumeral = zFactory.createZNumeral(BigInteger.valueOf(Math.abs(value)));
		NumExpr numExpr = zFactory.createNumExpr(zNumeral);
		if(value >= 0){
			return numExpr;
		}
		else{
			ZName zName = zFactory.createZName("- _ ", zFactory.createZStrokeList(), "neg");
			RefExpr negRefExpr = zFactory.createRefExpr(zName, zFactory.createZExprList(), false, false);
			ZExprList zExprList = zFactory.createZExprList();
			zExprList.add(negRefExpr);
			zExprList.add(numExpr);
			return zFactory.createApplExpr(zExprList, true);
		}	
	}

	public Expr getNormalizedType(){
		return normalizedType;
	}

	public int getFMSize(){
		return size;
	}    
    
    public void setFMSize(int fMSize){
        
    }

    @Override
	public Object clone(){
        List auxArrayList = new ArrayList<BigInteger>();
        for(int i=0; i< integerList.size(); i++)
            auxArrayList.add(integerList.get(i));
        NatFiniteModel natFiniteModel = new GivenNatFiniteModel(auxArrayList);
        return natFiniteModel;
	}
} 