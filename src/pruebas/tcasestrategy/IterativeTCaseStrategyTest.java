package pruebas.tcasestrategy;

import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;

import common.z.*;
import compserver.tcasegen.strategies.*;
import compserver.tcasegen.fm.intfm.ZeroIntFiniteModel;
import compserver.tcasegen.fm.natfm.ZeroNatFiniteModel;


// Para testear IterativeTCaseStrategy
public class IterativeTCaseStrategyTest{

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
									TClass tClass = new TClassImpl(axPara, SpecUtils.getAxParaName(axPara));
                                    
                                    ZeroIntFiniteModel zeroIntFiniteModel = new ZeroIntFiniteModel();
                                    zeroIntFiniteModel.initialize(3);

                                    ZeroNatFiniteModel zeroNatFiniteModel = new ZeroNatFiniteModel();
                                    zeroNatFiniteModel.initialize(3);  
                                    TCaseStrategy tcs = new IterativeTCaseStrategy(3,1000000, null, null, zeroIntFiniteModel, zeroNatFiniteModel);
									AbstractTCase abstractTCase = tcs.generateAbstractTCase(spec,tClass);
									if(abstractTCase != null)
										System.out.println("\n"+SpecUtils.termToLatex((abstractTCase.getMyAxPara())));
									else
										System.out.println("\nNo se encontró ningún caso de prueba!");
									
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