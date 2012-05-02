package compserver.tcasegen.fm; 

import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.impl.ZFactoryImpl;

import common.z.UtilSymbols;



/**
 * An instance of this class represents a finite model generator associated to the type of 
 * the Z specification language that corresponds to the power of another type.
 * @author Pablo Rodriguez Monetti
 */
public class PowerFiniteModel implements FiniteModel{

	private int size;
	private Expr normalizedType;
	private FiniteModel finiteModel;
	private int index;
	private ZFactory zFactory;


	public PowerFiniteModel(FiniteModel finiteModel){
		this.finiteModel = finiteModel;	
		finiteModel.initialize();
		zFactory = new ZFactoryImpl();
                double sonSize = (double)finiteModel.getFMSize();
                if(sonSize < 25)
                    size = (int) Math.pow((double)2,sonSize);
                else
                    size = 1000000;
		//System.out.println("El tamaño del conjunto es: "+size);
		normalizedType = zFactory.createPowerExpr(finiteModel.getNormalizedType());
		//normalizedType = finiteModel.getNormalizedType();
		index = 0;
	}

    /**
     * Initializes the iterator related to this FiniteModel.
     */
	public void initialize(){
		index = 0;
		finiteModel.initialize();
	}


    
    /**
     * Returns true whether the iteration has more elements.
     * @return true whether the iterator has more elements.
     */  
	public boolean hasNext(){
		return index < size;
	}


    
    /**
     * Returns the next element in the iteration.
     * @return the next element in the iteration.
     */    
	public Expr next(){		
		Expr expr = null;
		if(index == 0){
			// Devolvemos el conjunto vacio
			ZName zName = zFactory.createZName(UtilSymbols.emptySetSymbol(), zFactory.createZStrokeList(), "pfunc");
			ZExprList refZExprL = zFactory.createZExprList(); 
			refZExprL.add(finiteModel.getNormalizedType());
			//refZExprL.add(normalizedType);  CAMBIO
			expr = zFactory.createRefExpr(zName, refZExprL, false, false);
			//expr = finiteModel.getNormalizedType();
		}
		else{
			ZExprList zExprList = zFactory.createZExprList();
			finiteModel.initialize();
			for(int j=0; j< finiteModel.getFMSize(); j++){
				Expr auxExpr = finiteModel.next();
				if((index & 1<<j) != 0)
					zExprList.add(auxExpr);	
			}
			expr = zFactory.createSetExpr(zExprList);
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
		FiniteModel newFiniteModel = (FiniteModel) finiteModel.clone();
		return new PowerFiniteModel(newFiniteModel);
	}

} 
 
