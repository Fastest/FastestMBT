package pruebas.texprinting;

import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;
import java.io.*;


//INTENTA imprimir desde el Para con PrintUtils - NO FUNCIONA!
public class TexPrinting4{

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
			for(int i=0; i< zParaList.size(); i++)
				processPara(zParaList.get(i), zSect, manager);
		}	
	}


	public static void processPara(Para para, ZSect zSect, SectionManager manager){
		StringWriter out = new StringWriter();
		//PrintUtils.printLatex(para, out, manager, zSect.getName());
		System.out.println(out.toString());

	}
}