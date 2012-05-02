package compserver.tcasegen.fm; 

import java.util.*;

import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.ProdExpr;
import net.sourceforge.czt.z.ast.PowerExpr;
import net.sourceforge.czt.z.ast.TupleExpr;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.impl.ZFactoryImpl;

import common.z.UtilSymbols;
import common.z.SpecUtils;


/**
 * An instance of this class represents a finite model generator associated to the type of 
 * the Z specification language that corresponds to the partial functions.
 * @author Pablo Rodriguez Monetti
 */
public class PFuncFiniteModel implements FiniteModel{

	private int size;
	private Expr normalizedType;
	private FiniteModel leftFiniteModel;
	private FiniteModel rightFiniteModel;
	private int index;
	private int leftIndex;
	private int rightIndex;
	private ZExprList leftZExprList;
	private ZExprList rightZExprList;
	private List<FiniteModel> rightFiniteModelList;
	private ZFactory zFactory; 
	private boolean first;

    
    /**
     * Creates an instance of this class.
     * @param leftFiniteModel
     * @param rightFiniteModel
     */
	public PFuncFiniteModel(FiniteModel leftFiniteModel, FiniteModel rightFiniteModel){

		this.leftFiniteModel = leftFiniteModel;
		this.rightFiniteModel = rightFiniteModel;
		index = 0;
		leftIndex = 0;
		rightIndex = 1;
		zFactory = new ZFactoryImpl();
		leftZExprList = zFactory.createZExprList();
		rightZExprList = zFactory.createZExprList();
		rightFiniteModelList = null;
		first = true;


        // We set the normalized type of the expressions that a PFuncFiniteModel returns


		ZExprList refZExprL = zFactory.createZExprList(); 
		refZExprL.add(leftFiniteModel.getNormalizedType());
		refZExprL.add(rightFiniteModel.getNormalizedType());
		normalizedType = zFactory.createPowerExpr(zFactory.createProdExpr(refZExprL));

        // We calculate the size of the PFuncFiniteModel
		size = 0;
                try{
                    long m = rightFiniteModel.getFMSize();
                    long n = leftFiniteModel.getFMSize();
                    long nFact = 1;
                    for(int i=1; i<=n; i++)
    			nFact *= i;
                    long iFact = 1;
                    long n_iFact = nFact;
                    for(int i=0; i<=n; i++){
    			if(i>0)
                                    iFact *= i;
                            if(i>0 && i<n)
    				n_iFact /= (n-i+1);
                            long pow = (long) Math.pow((double)m,(double)i);
                            long combinatoryNumber = nFact / (iFact * n_iFact);
                            size += combinatoryNumber * pow;
                    }
                }
                catch(Exception e){
                    size = 1000000;
                }
	}


    /**
     * Initializes the iterator related to this FiniteModel.
     */
	public void initialize(){
		index = 0;
		leftIndex = 0;
		rightIndex = 1;
		leftFiniteModel.initialize();
		rightFiniteModel.initialize();
		leftZExprList.clear();
		rightZExprList.clear();
		rightFiniteModelList = null;
		first = true;
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

		int currentLeftSize = leftZExprList.size();

		Expr expr = null;

		if(first){
			//Devolvemos el conjunto vacio con el tipo explicito
			ZName zName = zFactory.createZName(UtilSymbols.emptySetSymbol(), 
					zFactory.createZStrokeList(), "pfunc");
			ZExprList refZExprL = zFactory.createZExprList();
			refZExprL.add(((PowerExpr)normalizedType).getExpr());
			expr = zFactory.createRefExpr(zName, refZExprL, false, false);
			first = false;
		}
		else{

			int currentRightSize = (int) Math.pow(rightFiniteModel.getFMSize(),currentLeftSize);
			if(rightIndex == currentRightSize){		// Ya se devolvieron todas las funciones asociadas a la lista leftZExprList

				// Actualizamos la lista leftZExprList y aumentamos el indice leftIndex
				leftZExprList.clear();
				leftIndex++;
				leftFiniteModel.initialize();
				for(int j=0; j< leftFiniteModel.getFMSize(); j++){
					Expr auxExpr = leftFiniteModel.next();
					if((leftIndex & 1<<j) != 0)
						leftZExprList.add(auxExpr);
				}

				//Ponemos a cero el indice rightIndex
				rightIndex = 0;

				// Creamos una nueva lista de clones, rightFiniteModelList , del rightFiniteModel con tantos clones como elementos 
				// tenga la lista leftZExprList
				// Tambien limpiamos la lista rightZExprList y le agregamos leftZExprList.size() veces 
				// la primera expresion de rightFiniteModel
				rightFiniteModelList = new ArrayList<FiniteModel>();
				rightFiniteModel.initialize();
				rightZExprList.clear();
				for(int i=0; i<	leftZExprList.size(); i++){
					FiniteModel finiteModel = (FiniteModel)rightFiniteModel.clone();
					rightFiniteModelList.add(finiteModel);
					rightZExprList.add(i, finiteModel.next());
				}

			}

			// Construimos la funcion (en expr) que se devolvera en esta llamada a next
				ZExprList funcZExprList = zFactory.createZExprList();
			for(int i=0; i< leftZExprList.size(); i++){
				ZExprList pairZExprList = zFactory.createZExprList();
				pairZExprList.clear();
				pairZExprList.add(leftZExprList.get(i));
				pairZExprList.add(rightZExprList.get(i));
				TupleExpr pairExpr = zFactory.createTupleExpr(pairZExprList);
				funcZExprList.add(pairExpr);
			}
			
			expr = zFactory.createSetExpr(funcZExprList);


			// El cambio que sufre rightZExprList no afecta a la funcion que se devolvera ahora, sino a la de la sig llamada
			boolean hasInc = false;
			for(int j=0; j< rightZExprList.size() && !hasInc; j++){
				FiniteModel finiteModel = rightFiniteModelList.get(j);
				if(finiteModel.hasNext())
					hasInc = true;
				else
					finiteModel.initialize();
				rightZExprList.set(j,finiteModel.next());
			}
			rightIndex++;
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
		FiniteModel newLeftFiniteModel = (FiniteModel) leftFiniteModel.clone();
		FiniteModel newRightFiniteModel = (FiniteModel) rightFiniteModel.clone();
		return new PFuncFiniteModel(newLeftFiniteModel, newRightFiniteModel);
	}

} 
