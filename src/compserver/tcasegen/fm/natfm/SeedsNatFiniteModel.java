package compserver.tcasegen.fm.natfm; 

import java.math.*;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.ast.ZNumeral;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.NumExpr;
import net.sourceforge.czt.z.impl.ZFactoryImpl;

import common.z.SpecUtils;


/*
 * An instance of this class represents a finite model generator associated to the type of 
 * the Z specification language that corresponds to the naturals. In particular, this
 * generator takes the list of integers over which it will iterate, also considering a
 * natural one unit greater than the maximun integer of the list an, a natural one unit
 * lower than the minimum (if any) and a natural between each pair of naturals from the list.
 */ 
public class SeedsNatFiniteModel implements NatFiniteModel{

	private int size;
	private int index;
	private Expr normalizedType;
	private ZFactory zFactory;
    private List<BigInteger> natList;

	public SeedsNatFiniteModel(){
  
	}
    
    public SeedsNatFiniteModel(List<BigInteger> natList){
        this.natList = natList;
		this.size = natList.size();
		index = 0;
		normalizedType = SpecUtils.getNumRefExpr();
        zFactory = new ZFactoryImpl();
	}
        
        
        
        
    public void initialize(List<BigInteger> integerList){
  
        this.natList = integerList;
        
        // We delete negative numbers
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
        

        // We sort the list
        for(int i=0; i<integerList.size(); i++){
            for(int j=0; j<integerList.size()-1; j++){
                long jValue = integerList.get(j).intValue();
                long j1Value = integerList.get(j+1).intValue();
                if(jValue > j1Value){
                    integerList.set(j, BigInteger.valueOf(j1Value));
                    integerList.set(j+1, BigInteger.valueOf(jValue));
                }
            }
        }
               
        // We add to the list the medium value between each pair of consecutive elements
        int arrayListSize = integerList.size();
        for(int i=0; i<arrayListSize-1; i++){
            long iValue = integerList.get(i).intValue();
            long i1Value = integerList.get(i+1).intValue();
            long middle = (i1Value + iValue )/2;
            if(middle != iValue && middle != i1Value)
                integerList.add(BigInteger.valueOf(middle));
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
        int value = natList.get(index).intValue();
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

    @Override
	public Object clone(){
        List<BigInteger> auxArrayList = new ArrayList<BigInteger>();
        for(int i=0; i< natList.size(); i++)
            auxArrayList.add(natList.get(i));
        NatFiniteModel natFiniteModel = new SeedsNatFiniteModel(auxArrayList);
        return natFiniteModel;
	}
} 