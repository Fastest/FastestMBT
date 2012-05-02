package compserver.tcasegen.fm; 

import java.util.*;

import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.TupleExpr;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.impl.ZFactoryImpl;

/**
 * An instance of this class represents a finite model generator associated to the type of 
 * the Z specification language that corresponds to the partial functions.
 * @author Pablo Rodriguez Monetti
 */
public class ProdFiniteModel implements FiniteModel{

	private int size;
	private Expr normalizedType;
	private List<FiniteModel> finiteModelList;
	private int index;
	private ZExprList tupleZExprList;
	private ZFactory  zFactory; 

    
    /**
     * Creates an instance of ProdFiniteModel.
     * @param finiteModelList
     */
	public ProdFiniteModel(List<FiniteModel> finiteModelList){

		this.finiteModelList = finiteModelList;
		index = 0;

		zFactory = new ZFactoryImpl();
		tupleZExprList = zFactory.createZExprList();


		// Inicializamos tupleZExprList con el primer elemento devuelto
                // por cada FiniteModel de finiteModelList
		for(int i=0; i<finiteModelList.size(); i++)
			tupleZExprList.add(i, finiteModelList.get(i).next());


		ZExprList typeList = zFactory.createZExprList();
		size = 1;
		// Calculamos el tipo explicito y el tamaño
		for(int i=0; i<finiteModelList.size(); i++){
			FiniteModel finiteModel = finiteModelList.get(i);
			size *=  finiteModel.getFMSize();
			typeList.add(finiteModel.getNormalizedType());
		}

                // If the size computation results in a negative number it is
                // because there was an overflow because it is a very big number
                // So, we give size a fixed big number, smaller than the real one
                if (size < 0)
                   size = 1000000;
		normalizedType = zFactory.createProdExpr(typeList);

	}



    /**
     * Initializes the iterator related to this FiniteModel, each instance of finiteModelList and
     * tupleZExprList with the first element returned by each FiniteModel of finiteModelList.
     */        
	public void initialize(){
		index = 0;
		tupleZExprList.clear();
		for(int i=0; i<finiteModelList.size(); i++){
			FiniteModel finiteModel = finiteModelList.get(i);
			finiteModel.initialize();
			tupleZExprList.add(i, finiteModel.next());
		}


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
		ZExprList zExprList = zFactory.createZExprList();
		for(int i=0; i< tupleZExprList.size(); i++)
			zExprList.add(tupleZExprList.get(i));

		TupleExpr tupleExpr = zFactory.createTupleExpr(zExprList);	

		// Modificamos tupleExprList para la proxima llamada que se haga
                // a next()
		boolean hasInc = false;
		for(int j=0; j< finiteModelList.size() && !hasInc; j++){
			FiniteModel finiteModel = finiteModelList.get(j);
			if(finiteModel.hasNext())
				hasInc = true;
			else
				finiteModel.initialize();
			tupleZExprList.set(j,finiteModel.next());
		}

		index++;
		return tupleExpr;	
	}




	public Expr getNormalizedType(){
		return normalizedType;
	}

	public int getFMSize(){
		return size;
	}

    
    @Override
	public Object clone(){
		List<FiniteModel> newFiniteModelList = new ArrayList<FiniteModel>();

		for(int i=0; i<finiteModelList.size(); i++){
			FiniteModel finiteModel = finiteModelList.get(i);
			newFiniteModelList.add((FiniteModel)finiteModel.clone());
		}
		return new ProdFiniteModel(newFiniteModelList);
	}

} 
 
