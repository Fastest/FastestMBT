package pruebas.eval;

import java.io.*;

import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.impl.*;
import net.sourceforge.czt.base.ast.Term;


import net.sourceforge.czt.animation.eval.*;
import net.sourceforge.czt.session.Markup;
import net.sourceforge.czt.typecheck.z.util.UndeterminedTypeException;

import common.z.*;
import common.z.czt.visitors.*;




// Parseamos una especificacion schEvalTest2.tex cuyo unico esquema tiene el predicado a = {}. 
//Obtenemos el predicado y reemplazamos las ocurrencias de a por {5} obteniendo {5}={}, lo evaluamos y el resultado NO es correcto. 
//Lanza una UndeterminedtypeException.


// O parseamos una especificacion schEvalTest3.tex cuyo unico esquema tiene el predicado a = {5}. 
//Obtenemos el predicado y reemplazamos la ocurrencia de a por {5}, obteniendo {5}={5}, lo evaluamos y el resultado es correcto, true.

public class SchemeEvaluatorTest2{

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

								Expr expr2 = (Expr)textUI.parseTerm("\\{5\\}", new PrintWriter(System.out, true));

								ZFactory zFactory = new ZFactoryImpl(); 
								ZName zName = zFactory.createZName("a", zFactory.createZStrokeList(), "right");
								ZExprList zExprList = zFactory.createZExprList(); 
								RefExpr refExpr = zFactory.createRefExpr(zName, zExprList, false, false);
	
								CZTReplacer replaceVisitor = new CZTReplacer();
								replaceVisitor.setOrigTerm(refExpr);
								replaceVisitor.setNewTerm(expr2);
								Pred newPred = (Pred) predd.accept(replaceVisitor);
								System.out.println("El predicado con el reemplazo, y que se va a evaluar es,  es: ");
								System.out.println(textUI.printTerm(newPred, Markup.LATEX));
 											
								result = zLive.evalPred(newPred);


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
