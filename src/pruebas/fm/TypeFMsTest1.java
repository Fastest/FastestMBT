package pruebas.fm;

import compserver.tcasegen.eval.NormalTypeAndFM;
import java.util.*;

import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;

import common.z.*;
import common.z.czt.CztPrinter;
import compserver.tcasegen.fm.*;



// Obtiene los modelos finitos de los tipos globales de la especificacion
// utilizando la generacion GenAll
public class TypeFMsTest1{
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


			TypeFMsGenVisitor typeFMsGenVisitor = new TypeFMsGenVisitor(null,3);
			spec.accept(typeFMsGenVisitor);
			Map<Expr,NormalTypeAndFM> typeFMs = typeFMsGenVisitor.getExprMap();


			Set<Map.Entry<Expr, NormalTypeAndFM>> set = typeFMs.entrySet();
			System.out.println("El tamaño del map es " + set.size());
			System.out.println("\n<--------*********************-------->");
			Iterator<Map.Entry<Expr, NormalTypeAndFM>> iterator = set.iterator();
			while(iterator.hasNext()){
				Map.Entry<Expr, NormalTypeAndFM> mapEntry = iterator.next();
				Expr expr = mapEntry.getKey();
				NormalTypeAndFM fMPair = mapEntry.getValue();
				System.out.print("Tipo: ");		
				CztPrinter.printExpr(expr,0);
				System.out.print("Tipo normalizado: ");	
				CztPrinter.printExpr(fMPair.getNormalType(),0);
				SetExpr setExpr = fMPair.getFM();
				System.out.println("Tamaño del modelo finito: " + setExpr.getZExprList().size());
				System.out.println("Modelo finito: " + SpecUtils.termToLatex(setExpr));
				System.out.println("<--------*********************-------->");
				}
			}
		catch(Exception e){
			System.out.println(e);
		}
	}
}