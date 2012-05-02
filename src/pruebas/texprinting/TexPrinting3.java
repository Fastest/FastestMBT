package pruebas.texprinting;

import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;
import java.io.*;


//INTENTA imprimir desde el ParaList con PrintUtils - NO FUNCIONA!
public class TexPrinting3{

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
				if (sect instanceof ZSect)
					processSect((ZSect)sect, manager);
    		}

 		}
    	catch(Exception e){
			System.out.println (e);	
		}
 }

	public static void processSect(ZSect zSect, SectionManager manager){
		ParaList paraList = zSect.getParaList();
		if (paraList instanceof ZParaList) {
			ZParaList zParaList = (ZParaList) paraList;

			StringWriter out = new StringWriter();
			//PrintUtils.printLatex(zParaList, out, manager, zSect.getName());
			System.out.println(out.toString());
		}
	
	}


}