package pruebas.tcasestrategy;


import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;

import common.z.*;
import compserver.tcasegen.strategies.*;


// Para testear CompleteTCaseStrategy

public class CompleteTCaseStrategyTest{

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
								if(TClassImpl.isTClass(axPara)){
									System.out.println(SpecUtils.termToLatex(axPara));
									TClass tClass = new TClassImpl(axPara, "Pepe");
									AbstractTCase abstractTCase = (new CompleteTCaseStrategy(3)).generateAbstractTCase(spec,tClass);
    								if(abstractTCase != null)
										System.out.println("\n"+SpecUtils.termToLatex((abstractTCase.getMyAxPara())));
									else
										System.out.println("\nNo se encontró ningún caso de prueba!");
									return;
								}
							}
						}
					}
				}
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

} 
