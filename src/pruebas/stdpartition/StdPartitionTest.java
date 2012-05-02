/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pruebas.stdpartition;

import java.util.*;
import java.io.*;

import net.sourceforge.czt.parser.z.ParseUtils;
import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.animation.eval.*;
import net.sourceforge.czt.z.impl.ZFactoryImpl;

import common.z.*;
import common.z.czt.visitors.*;
import client.blogic.testing.ttree.tactics.StdPartition;
import client.blogic.testing.ttree.tactics.StdPartitionsControl;
import client.blogic.testing.ttree.tactics.StdPartitionLoader;
import common.repository.AbstractIterator;


/**
 *
 * @author Pablo Rodriguez Monetti
 */
public class StdPartitionTest {

    public static void main(String[] args) {
        		SectionManager manager = new SectionManager();
		if(args.length!=2){
			System.out.println("Ingresar nombres de archivos!!!!");
			return;
		}
                
                
        // Extraemos el esquema del archivo
		FileSource source = new FileSource(args[0]);
		manager.put(new Key(source.getName(), Source.class), source);
		try{
			Spec spec = (Spec) 
			manager.get(new Key(source.getName(), Spec.class));
            TClass tClass = null;
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
								if(TClassImpl.isTClass(axPara)){
									tClass = new TClassImpl(axPara, SpecUtils.getAxParaName(axPara));                    
                                    
                                }
							}
						}
					}
				}
			}

            
            String operatorStr = "<";
            //String exprStr = "\\{3,4,5,6\\} \\cup \\{3\\}";
            String exprStr = " s < 1";
            
            ZLive zLive = new ZLive();
            TextUI textUI = new TextUI(zLive, new PrintWriter(System.out, true));
            
            // Convertimos la expresion que contiene el operador de latex AST
            Term parsedTerm = ParseUtils.parsePred(new StringSource(exprStr),zLive.getCurrentSection(), zLive.getSectionManager() );


            // Obtenemos los parametros reales de la expresion
            List<Term> realParamList = null;
            if(parsedTerm instanceof Pred)
                realParamList = ((Pred) parsedTerm).accept(new ParamExtractor());                 
            else
                realParamList = ((Expr) parsedTerm).accept(new ParamExtractor());                 
                
            if (realParamList == null){
                System.out.println("Lista de parametros reales nula");
                return;
            }
       
            System.out.println("Lista de parametros reales:");
            for(int i=0; i<realParamList.size(); i++)
               System.out.println(SpecUtils.termToLatex(realParamList.get(i)));
            
            
           
            // Parseamos las particiones estandar del archivo de configuracion 
            // de particiones estandar

            (new StdPartitionLoader(args[1])).loadStdPartitions();
            AbstractIterator<StdPartition> stdPartitionIt = StdPartitionsControl.getInstance().createIterator();
            
            
         
            // Buscamos la particion estandar del operador
            boolean operatorFound = false;
            StdPartition stdPartition = null;    
            

            while(stdPartitionIt.hasNext() & !operatorFound){
                stdPartition = stdPartitionIt.next();
                System.out.println(stdPartition.getOperator());
                System.out.println(operatorStr);
                if(stdPartition.getOperator().equals(operatorStr))
                    operatorFound = true;
            }
            if((stdPartition == null) || !operatorFound){
                System.out.println("No hay particion estandar para el operador solicitado");
                return;
            }
        
            List<String> formalParamList = stdPartition.getFormalParamList();
            if(formalParamList.size() != realParamList.size()){
                System.out.println("Las listas de parametros no coinciden en tamaño");
                return;
            }
           
            // Reemplazamos parametros reales en parametros formales de
            // cada predicado y con cada uno generamos un nuevo esquema
            List<AxPara> axParaList = new ArrayList<AxPara>();
            List<Pred> predList = stdPartition.getPredList();
            AxPara tClassAxPara = tClass.getMyAxPara();
            CZTCloner cloneVisitor = new CZTCloner();
            CZTReplacer replaceVisitor = new CZTReplacer();
            for(int i=0; i< predList.size(); i++){
                Pred pred = predList.get(i);
                Pred newPred = (Pred) pred.accept(cloneVisitor);
                for(int j=0; j<formalParamList.size();j++){
                   String originalString = formalParamList.get(j);  
                   
                   ZFactory zFactory = new ZFactoryImpl();
                   ZName zName = zFactory.createZName(originalString , zFactory.createZStrokeList(), "word");
                   ZExprList zExprList = zFactory.createZExprList(); 
                   RefExpr originalRefExpr = zFactory.createRefExpr(zName, zExprList, false, false);
                   
                   Term finalTerm = realParamList.get(j);
                   // Reemplazamos la RefExpr que cuyo nombre sea originalString
                   // por finalTerm

                   replaceVisitor.setOrigTerm(originalRefExpr);
                   replaceVisitor.setNewTerm(finalTerm);
           
                   newPred = (Pred) newPred.accept(replaceVisitor);
                                                               
                }
                AxPara newAxPara = (AxPara) tClassAxPara.accept(cloneVisitor);

                Pred newAxParaPred = SpecUtils.getAxParaPred(newAxPara);
                SpecUtils.setAxParaPred(newAxPara, SpecUtils.andPreds(newAxParaPred, newPred));
                System.out.println("\n"+SpecUtils.termToLatex(newAxPara));
            }              
        }

		catch(Exception e){
			System.out.println(e);
		}
        
        
    } 
}
