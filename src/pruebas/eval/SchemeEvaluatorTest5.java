package pruebas.eval;

import java.util.*;
import java.io.*;

import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.impl.*;
import net.sourceforge.czt.base.ast.*;
import net.sourceforge.czt.animation.eval.*;
import net.sourceforge.czt.session.Markup;
import net.sourceforge.czt.typecheck.z.util.UndeterminedTypeException;
import net.sourceforge.czt.typecheck.z.ErrorAnn;
import net.sourceforge.czt.typecheck.z.TypeCheckUtils;

import common.z.*;
import common.z.czt.CztPrinter;
import common.z.czt.visitors.*;





// Parseamos una especificacion schEvalTest2.tex cuyo unico esquema tiene el predicado a = {}. Obtenemos el predicado y reemplazamos la ocurrencia de a por {}, obteniendo {}={}, lo evaluamos y el resultado NO es correcto. Pero si antes hacemos typecheck...tampoco funciona...

public class SchemeEvaluatorTest5{

	public static void main(String[] args) {	
	
		SectionManager manager = new SectionManager();
		if(args.length!=1){
			System.out.println("Ingresar nombre de archivo!!!!");
			return;
		}
		FileSource source = new FileSource(args[0]);
		manager.put(new Key(source.getName(), Source.class), source);
		try{
			Spec spec = (Spec) 
			manager.get(new Key(source.getName(), Spec.class));

			for (Sect sect : spec.getSect()) {
				if (sect instanceof ZSect){
					ZSect zSect = (ZSect) sect;
					ParaList paraList = zSect.getParaList();
					if (paraList instanceof ZParaList) {
						ZParaList zParaList = (ZParaList) paraList;
						for(int i=0; i < zParaList.size(); i++){
							Para para = zParaList.get(i);
							if (para instanceof AxPara){
								AxPara axPara = (AxPara) para;
							
								ZLive zLive = new ZLive();
 								TextUI textUI = new TextUI(zLive, new PrintWriter(System.out, true));

								Term result = null;

								Pred predd = SpecUtils.getAxParaPred(axPara);
								System.out.println(textUI.printTerm(predd, Markup.LATEX));

								ZFactory zFactory = new ZFactoryImpl(); 

								ZName zName = zFactory.createZName("a", zFactory.createZStrokeList(), "right");
								ZExprList zExprList = zFactory.createZExprList(); 
								RefExpr refExpr = zFactory.createRefExpr(zName, zExprList, false, false);

 								Expr expr2 = zFactory.createSetExpr(zFactory.createZExprList());
	
								CZTReplacer replaceVisitor = new CZTReplacer();
								replaceVisitor.setOrigTerm(refExpr);
								replaceVisitor.setNewTerm(expr2);
								Pred newPred = (Pred) predd.accept(replaceVisitor);
								System.out.println("El predicado con el reemplazo, y que se va a evaluar es,  es: ");
								System.out.println(textUI.printTerm(newPred, Markup.LATEX));
 								
								Pred newPred2 = (Pred) newPred.accept(new CZTCloner());
								List<? extends ErrorAnn> errors =
									TypeCheckUtils.typecheck(newPred2,zLive.getSectionManager() , false, zLive.getCurrentSection() );
								if(errors.size() > 0){
									System.out.println("Error: el termino contiene errores de tipo");
									for(ErrorAnn next: errors){
										System.out.println(next);
									}
								}
								CztPrinter.printPred(newPred2,1);
								if(newPred.equals(newPred2))
									System.out.println("El predicado y el predicado typechequeado son iguales");
								else
									System.out.println("El predicado y el predicado typechequeado NO son iguales");	
 								
								result = zLive.evalPred(newPred2);


								if(result != null){
									System.out.print("El resultado de la evaluacion es: ");
 									System.out.println(textUI.printTerm(result, Markup.LATEX));
									System.out.println("***********************************************************");	
								}
								else{
									System.out.println("El resultado de la evaluacion es null!!");
		
								}
							}
						}
					}
				}
			}
		}
    	catch (UndefException ex)
    	{
      		System.out.println("Undefined!  " + ex.getMessage());

    	}
    	catch (EvalException ex){
      		System.out.println();
      		System.out.println("Error: evaluation too difficult/large: "
	          	+ ex.getMessage());
    	}
    	catch (UndeterminedTypeException ex){
			ex.printStackTrace();
    	}
		catch(Exception e){
			e.printStackTrace();
			System.out.println(e);
			System.out.println("la puta madre!");
		}
	

	}	

} 
