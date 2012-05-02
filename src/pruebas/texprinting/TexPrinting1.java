package pruebas.texprinting;

import net.sourceforge.czt.print.util.LatexString;
import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;

// Imprime desde Spec con LatexString - FUNCIONA!
public class TexPrinting1{

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
			manager = new SectionManager();
            manager.put(new Key("bbanco", Spec.class), spec);
            LatexString latex = (LatexString) manager.get(new Key("bbanco", LatexString.class));
            System.out.println(latex);
    	}
    	catch(Exception e){
			System.out.println (e);
		}
	}

}