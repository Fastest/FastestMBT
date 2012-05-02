/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pruebas.stdpartition;

import java.util.*;
import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;

import common.z.*;
import client.blogic.testing.ttree.tactics.SPTactic;
import client.blogic.testing.ttree.tactics.StdPartitionLoader;


/**
 *
 * @author Pablo Rodriguez Monetti
 */
public class StdPartitionTest2 {

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
                        AxPara axPara = null;
			for (Sect sect : spec.getSect()) {
				if (sect instanceof ZSect){
					ZSect zSect = (ZSect) sect;
					ParaList paraList = zSect.getParaList();
					if (paraList instanceof ZParaList) {
						ZParaList zParaList = (ZParaList) paraList;
						for(int i=0; i < zParaList.size(); i++){
							Para para = zParaList.get(i);
							if (para instanceof AxPara){
								axPara = (AxPara) para;
								if(TClassImpl.isTClass(axPara)){
									tClass = new TClassImpl(axPara, SpecUtils.getAxParaName(axPara));                    
                                    
                                }
							}
						}
					}
				}
			}

            SPTactic sPTactic = new SPTactic();
            (new StdPartitionLoader(args[1])).loadStdPartitions();
            if(sPTactic.parseArgs("< s < 1")){
                List<TClass> tClassList = sPTactic.applyTactic(tClass);
                for(int i=0; i<tClassList.size(); i++){
                    TClass auxTClass = tClassList.get(i);
                    System.out.println("\n"+SpecUtils.termToLatex(auxTClass.getMyAxPara()));
                }
            }


        }

		catch(Exception e){
			System.out.println(e);
		}
        
        
    } 
}
