package pruebas.texprinting;

import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;
import java.io.*;

// Imprime desde Spec con PrintUtils - FUNCIONA!
public class TexPrinting{

    public static void main(String[] args) {

		if(args.length!=1){
			System.out.println("Ingresar nombre de archivo!!!!");
			return;
		}
		FileSource source = new FileSource(args[0]);
        SectionManager manager = new SectionManager();
        manager.put(new Key(source.getName(), Source.class), source);
        try{
			Spec spec = (Spec) manager.get(new Key(source.getName(), Spec.class));
			StringWriter out = new StringWriter();
			//PrintUtils.printLatex(spec, out, manager);
			System.out.println(out.toString());

		}
    	catch(Exception e){
			System.out.println (e);
		}
	}
}