package compserver.tcasegen.fm.intfm; 

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
 * the Z specification language that corresponds to the integer numbers. In particular, this
 * generator takes the list of integers over which it will iterate, also considering an
 * integer one unit greater than the maximun integer of the list and another integer one unit
 * lower than the minimum.
 * @author Pablo Rodriguez Monetti
 */
public class GivenIntFiniteModel implements IntFiniteModel{

	private int size;
	private int index;
	private Expr normalizedType;
	private ZFactory zFactory;
    private List<BigInteger> arrayList;

	public GivenIntFiniteModel(){
        
	}

    
    /**
     * Creates instances of this class.
     * @param arrayList
     */
    public GivenIntFiniteModel(List<BigInteger> arrayList){
        this.arrayList = arrayList;
		this.size = arrayList.size();
		index = 0;
		normalizedType = SpecUtils.getNumRefExpr();
        zFactory = new ZFactoryImpl();
	}
        
        
        
  
    public void initialize(List<BigInteger> integerList){

        this.arrayList = integerList;
        // Buscamos maximo y minimo de la lista y eliminamos duplicados
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
        int value = arrayList.get(index).intValue();
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
        List auxArrayList = new ArrayList<BigInteger>();
        for(int i=0; i< arrayList.size(); i++)
            auxArrayList.add(arrayList.get(i));
        IntFiniteModel intFiniteModel = new GivenIntFiniteModel(auxArrayList);
        return intFiniteModel;
	}
} 