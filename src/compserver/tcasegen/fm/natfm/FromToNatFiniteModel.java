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
 * generator considers the integers taken from a range.
 * @author Pablo Rodriguez Monetti
 */
public class FromToNatFiniteModel implements IntFiniteModel{

	private int size;
	private int index;
	private Expr normalizedType;
	private ZFactory zFactory;
    private List<BigInteger> arrayList;

    /**
     * Creates an instance of this class.
     */
    /**
     * Creates instances of this class.
     * @param arrayList
     */
    public FromToNatFiniteModel(List<BigInteger> arrayList){
        this.arrayList = arrayList;
        for(int i=0; i<arrayList.size(); i++)
            if(arrayList.get(i).intValue() < 0){
                arrayList.remove(i);
                i--;
            }

		this.size = arrayList.size();
		index = 0;
		normalizedType = SpecUtils.getNumRefExpr();
        zFactory = new ZFactoryImpl();
	}




    public void initialize(List<BigInteger> arrayList){

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
        IntFiniteModel intFiniteModel = new FromToIntFiniteModel(auxArrayList);
        return intFiniteModel;
	}
}