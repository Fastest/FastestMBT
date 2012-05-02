package pruebas;

import net.sourceforge.czt.session.Key;
import net.sourceforge.czt.session.SectionManager;
import net.sourceforge.czt.session.Source;
import net.sourceforge.czt.session.FileSource;
import net.sourceforge.czt.parser.util.LatexMarkupFunction;

public class GettingOpTable{

    public static void main(String[] args) {
        SectionManager manager = new SectionManager();
		
		FileSource source = new FileSource("bbook.tex");
        manager.put(new Key(source.getName(), Source.class), source);
        try{
		SectionManager manager2 = new SectionManager();
		manager2.put(new Key(source.getName(), LatexMarkupFunction.class), new LatexMarkupFunction(source.getName()));


/*			LatexMarkupFunction latexMarkupFunction = (LatexMarkupFunction) 
				manager.get(new Key(source.getName(), LatexMarkupFunction.class));
*/ 		}
    	catch(Exception e){
			System.out.println (e);	
		}
	}

}