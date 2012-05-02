package compserver.tcasegen.fm; 

import java.util.*;

import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.TupleExpr;
import net.sourceforge.czt.z.ast.SetExpr;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.impl.ZFactoryImpl;

import common.z.UtilSymbols;


/**
 * An instance of this class represents a finite model generator associated to the type of 
 * the Z specification language that corresponds to the total functions.
 * @author Pablo Rodriguez Monetti
 */
public class TFuncFiniteModel implements FiniteModel{

	private int size;
	private Expr normalizedType;
	private FiniteModel leftFiniteModel;
	private FiniteModel rightFiniteModel;
	private int index;
	private ZExprList rightZExprList;
	private List<FiniteModel> rightFiniteModelList;
	private ZFactory  zFactory; 

    
    /**
     * Creates an instance of this class.
     * @param leftFiniteModel
     * @param rightFiniteModel
     */
	public TFuncFiniteModel(FiniteModel leftFiniteModel, FiniteModel rightFiniteModel){

		this.leftFiniteModel = leftFiniteModel;
		this.rightFiniteModel = rightFiniteModel;

		zFactory = new ZFactoryImpl();

		// Establecemos el tipo normalizado de las expresiones que devuelve un TFuncFiniteModel
		ZExprList refZExprL = zFactory.createZExprList(); 
		refZExprL.add(leftFiniteModel.getNormalizedType());
		refZExprL.add(rightFiniteModel.getNormalizedType());
		normalizedType = zFactory.createPowerExpr(zFactory.createProdExpr(refZExprL));

		// Calculamos el tamaño del TFuncFiniteModel
		size = (int) Math.pow((double)rightFiniteModel.getFMSize(),(double)leftFiniteModel.getFMSize());	


		// Hacemos una lista con tantos clones de rightFiniteModel como elementos tenga leftFiniteModel
		// y agregamos el primer elemento de cada uno a la lista rightZExprList
		rightZExprList = zFactory.createZExprList();
		rightFiniteModelList = new ArrayList<FiniteModel>();
		for(int i=0; i<	leftFiniteModel.getFMSize(); i++){
			FiniteModel finiteModel = (FiniteModel)rightFiniteModel.clone();
			rightFiniteModelList.add(finiteModel);
			rightZExprList.add(i, finiteModel.next());
		}
		
	}

    
    /**
     * Initializes the iterator related to this FiniteModel.
     */
	public void initialize(){
		index = 0;
		leftFiniteModel.initialize();
		rightFiniteModel.initialize();	// No es necesario hacerlo

		// Agregamos a la lista rightZExprList leftFiniteModel.getFMSize() veces la primera expresion de rightFiniteModel e
		// inicializamos los leftFiniteModel.getFMSize() clones de rightFiniteModel de la lista finiteModelnList
		rightZExprList.clear();
		for(int i=0; i<leftFiniteModel.getFMSize(); i++){
			FiniteModel finiteModel = rightFiniteModelList.get(i);
			finiteModel.initialize();
			rightZExprList.add(i, finiteModel.next());
		}
	}


    /**
     * Returns true whether the iteration has more elements.
     * @return true whether the iterator has more elements.
     */
	public boolean hasNext(){
		return index < size;
	}



	public Expr next(){

		ZExprList funcZExprList = zFactory.createZExprList();
		leftFiniteModel.initialize();
		for(int i=0; i< rightZExprList.size(); i++){
			ZExprList pairZExprList = zFactory.createZExprList();
			pairZExprList.clear();
			pairZExprList.add(leftFiniteModel.next());
			pairZExprList.add(rightZExprList.get(i));
			TupleExpr pairExpr = zFactory.createTupleExpr(pairZExprList);
			funcZExprList.add(pairExpr);
		}

		SetExpr setExpr = zFactory.createSetExpr(funcZExprList);

		// El cambio que sufre rightZExprList no afecta el setExpr que se devolvera ahora, sino al siguiente
		boolean hasInc = false;
		for(int j=0; j< rightZExprList.size() && !hasInc; j++){
			FiniteModel finiteModel = rightFiniteModelList.get(j);
			if(finiteModel.hasNext())
				hasInc = true;
			else
				finiteModel.initialize();
			rightZExprList.set(j,finiteModel.next());
		}
		index++;		
		return setExpr;
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
		return new TFuncFiniteModel(newLeftFiniteModel, newRightFiniteModel);
	}

} 
