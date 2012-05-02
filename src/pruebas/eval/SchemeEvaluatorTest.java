package pruebas.eval;

import java.io.*;
import java.util.*;

import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.base.ast.Term;


import net.sourceforge.czt.animation.eval.*;
import net.sourceforge.czt.session.Markup;
import net.sourceforge.czt.typecheck.z.util.UndeterminedTypeException;
import net.sourceforge.czt.typecheck.z.*;

import common.z.*;


// Evalua el predicado de cada esquema de la especificacion leida


public class SchemeEvaluatorTest{

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

								System.out.println("El predicado que se va a evaluar es,  es: ");
								System.out.println(textUI.printTerm(predd, Markup.LATEX));
 											
								List<? extends ErrorAnn> errors = 
									TypeCheckUtils.typecheck(predd, zLive.getSectionManager(), false, zLive.getCurrentSection());
 								if(errors.size() >0){
									System.out.println(errors.toString());
									return;
								}

								result = zLive.evalPred(predd);


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
