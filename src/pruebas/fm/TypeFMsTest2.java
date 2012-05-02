package pruebas.fm;


import compserver.tcasegen.fm.natfm.ZeroNatFiniteModel;
import compserver.tcasegen.fm.intfm.ZeroIntFiniteModel;
import java.util.*;

import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;

import common.z.*;
import common.z.czt.CztPrinter;
import compserver.tcasegen.fm.*;




// Obtiene los modelos finitos de los tipos globales de la especificacion
// utilizando la generacion Iterative
public class TypeFMsTest2{
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

            ZeroIntFiniteModel zeroIntFiniteModel = new ZeroIntFiniteModel();
            zeroIntFiniteModel.initialize(3);

            ZeroNatFiniteModel zeroNatFiniteModel = new ZeroNatFiniteModel();
            zeroNatFiniteModel.initialize(3);            
            
            
			FiniteModelCreator finiteModelCreator = 
                    new FiniteModelCreator(null, 3, 3, zeroIntFiniteModel, 
                                zeroNatFiniteModel, new HashMap<String,List<String>>());
			spec.accept(finiteModelCreator);
			Map<Expr,FiniteModel> typeFMs = finiteModelCreator.getExprMap();


			Set<Map.Entry<Expr, FiniteModel>> set = typeFMs.entrySet();
			System.out.println("El tamaño del map es " + set.size());
			System.out.println("\n<--------*********************-------->");
			Iterator<Map.Entry<Expr, FiniteModel>> iterator = set.iterator();
			while(iterator.hasNext()){
				Map.Entry<Expr, FiniteModel> mapEntry = iterator.next();
				Expr expr = mapEntry.getKey();
				FiniteModel finiteModel = mapEntry.getValue();
				System.out.print("Tipo: ");		
				CztPrinter.printExpr(expr,0);
				System.out.print("Tipo normalizado: ");	
				CztPrinter.printExpr(finiteModel.getNormalizedType(),0);
				System.out.println("Tamaño del Set: " + finiteModel.getFMSize());
				System.out.println("Modelo finito:");
				while(finiteModel.hasNext()){
					System.out.println("\t*" + SpecUtils.termToLatex(finiteModel.next()));
					//CztPrinter.printExpr(finiteModel.next(),0);
				}
				System.out.println("<--------*********************-------->");
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
}