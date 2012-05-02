package pruebas.texprinting;

import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;
import java.io.*;


//Imprime desde cada Sect con PrintUtils - FUNCIONA!
public class TexPrinting2{

    public static void main(String[] args) {
        SectionManager manager = new SectionManager();
		if(args.length!=1){
			System.out.println("Ingresar nombre de archivo!!!!");
			return;
		}
		FileSource source = new FileSource(args[0]);
        manager.put(new Key(source.getName(), Source.class), source);
        try{
			Spec spec = (Spec) manager.get(new Key(source.getName(), Spec.class));
			for (Sect sect : spec.getSect()) {
				StringWriter out = new StringWriter();
				//PrintUtils.printLatex(sect, out, manager);
				System.out.println(out.toString());
    		}
		}
    	catch(Exception e){
			System.out.println (e);
		}
 }




}