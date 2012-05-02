package compserver.tcasegen.fm; 

import java.math.*;

import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.PowerExpr;
import net.sourceforge.czt.z.ast.SetExpr;
import net.sourceforge.czt.z.ast.TupleExpr;
import net.sourceforge.czt.z.ast.ProdExpr;
import net.sourceforge.czt.z.ast.NumExpr;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.impl.ZFactoryImpl;


import common.z.SpecUtils;
import common.z.czt.visitors.CZTCloner;
import common.z.UtilSymbols;
import common.z.SpecUtils;


/**
 * An instance of this class represents a finite model generator associated to the type of 
 * the Z specification language that corresponds to the sequences.
 * @author Pablo Rodriguez Monetti
 */
public class SeqFiniteModel implements FiniteModel{

	private int size;
	private Expr normalizedType;
	private FiniteModel finiteModel;
	private int index;
	private ZFactory zFactory;
	private Expr posPowerExpr;
	private FiniteModel rightFiniteModel;

    /**
     * Creates an instance of this class.
     * @param finiteModel
     */
	public SeqFiniteModel(FiniteModel finiteModel){
		this.finiteModel = finiteModel;	

		finiteModel.initialize();
		posPowerExpr = finiteModel.next();
		finiteModel.initialize();

		zFactory = new ZFactoryImpl();
	
		double sonSize = (double) finiteModel.getFMSize();

                if(sonSize < 10){
                    size = (int) Math.pow((double)2,sonSize);
                    size += (sonSize-1) * sonSize;
                }
                else
                    size = 1000000;

		ZExprList refZExprL = zFactory.createZExprList(); 
		refZExprL.add(SpecUtils.getNumRefExpr());
		refZExprL.add(finiteModel.getNormalizedType());
		normalizedType = zFactory.createPowerExpr(zFactory.createProdExpr(refZExprL));

		index = 0;

	}

    
    /**
     * Initializes the iterator related to this FiniteModel.
     */
	public void initialize(){
		index = 0;
		finiteModel.initialize();
		posPowerExpr = finiteModel.next();
		finiteModel.initialize();
		
	}


    
    /**
     * Returns true whether the iteration has more elements.
     * @return true whether the iterator has more elements.
     */
	public boolean hasNext(){
		return index < size;
	}


	public Expr next(){		
		Expr expr = null;
		int sonSize = finiteModel.getFMSize();
		ZExprList zExprList = zFactory.createZExprList();
		ZName zName = zFactory.createZName(UtilSymbols.seqAnglesSymbol(), 
                                    zFactory.createZStrokeList(), "seq");
		RefExpr seqRefExpr = zFactory.createRefExpr(zName, zFactory.createZExprList(), false, false);
		ZExprList applExprList = zFactory.createZExprList();
		applExprList.add(seqRefExpr);

		if(index == 0){
			// Devolvemos el conjunto vacio de tipo \nat \cross normalizedType
			zName = zFactory.createZName(UtilSymbols.emptySetSymbol(), 
					zFactory.createZStrokeList(), "emptyseq");
			ProdExpr prodExpr = (ProdExpr)((PowerExpr)normalizedType).getExpr();
			ZExprList refZExprL = zFactory.createZExprList();
			refZExprL.add(prodExpr);
			expr = zFactory.createRefExpr(zName, refZExprL, false, false);
		}
		else if (index < (int) Math.pow((double)2,(double)sonSize) ){
			// Devolvemos una secuencia cuyos elementos son obtenidos del conjunto de partes
			finiteModel.initialize();
			for(int j=0, k=0; j< sonSize; j++){
				Expr auxExpr = finiteModel.next();
				if((index & 1<<j) != 0){
					ZExprList tupleZExprList = zFactory.createZExprList();
					NumExpr numExpr = zFactory.createNumExpr(zFactory.createZNumeral(BigInteger.valueOf(k+1)));
					tupleZExprList.add(numExpr);
					tupleZExprList.add(auxExpr);
					TupleExpr tupleExpr = zFactory.createTupleExpr(tupleZExprList);
					zExprList.add(tupleExpr);
					k++;
				}
			}
			SetExpr setExpr = zFactory.createSetExpr(zExprList);
			applExprList.add(setExpr);
			expr = zFactory.createApplExpr(applExprList, true);

		}
		else{
			//Devolvemos una secuencia cuyos donde hay entre 2 y sonSize elementos repetidos
			int posPowerIndex = index - (int) Math.pow((double)2,(double)sonSize);
			int modIndex =  posPowerIndex % (sonSize -1);
			for(int i=0, k=0; i<modIndex+2; i++){
				ZExprList tupleZExprList = zFactory.createZExprList();
				NumExpr numExpr = zFactory.createNumExpr(zFactory.createZNumeral(BigInteger.valueOf(k+1)));
				tupleZExprList.add(numExpr);
				tupleZExprList.add((Expr)posPowerExpr.accept(new CZTCloner()));
				TupleExpr tupleExpr = zFactory.createTupleExpr(tupleZExprList);
				zExprList.add(tupleExpr);
				k++;
			}
			SetExpr setExpr = zFactory.createSetExpr(zExprList);
			applExprList.add(setExpr);
			expr = zFactory.createApplExpr(applExprList, true);
			//Verificamos si hay que actualizar posPowerExpr para la proxima llamada
			if(posPowerIndex == 0){
				finiteModel.initialize();
				finiteModel.next();
			}

			if(modIndex == sonSize-2){
				if(index+1==size || !finiteModel.hasNext()){
					finiteModel.initialize();
					posPowerExpr = finiteModel.next();
				}
				else{
					posPowerExpr = finiteModel.next();
				}
			}
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
		return new SeqFiniteModel(newFiniteModel);
	}

} 
 
 
