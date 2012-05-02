package pruebas.texprinting;

import java.util.*;

import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.impl.*;
import net.sourceforge.czt.print.util.LatexString;
import net.sourceforge.czt.parser.util.LatexMarkupFunction;


// INTENTA imprimir desde el AxPara con LatexString creando una nueva estructura que lo envuelva - Y PASANDOLE el OpTable y LatexmarkUpTable que se obtuvo de una de las secciones ZSect - FUNCIONA!
public class TexPrinting8{

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
					processSect(getLatexMarkupFunction(manager, spec), (ZSect)sect);
    		}
 		}
    	catch(Exception e){
			System.out.println (e);	
		}
	}


	public static void processSect(LatexMarkupFunction latexMarkupFunction, ZSect zSect){
		ParaList paraList = zSect.getParaList();
		try{
			if (paraList instanceof ZParaList) {
				ZParaList zParaList = (ZParaList) paraList;
				for(int i=0; i< zParaList.size(); i++)
					processPara(zParaList.get(i), latexMarkupFunction);
			}
		}	
    	catch(Exception e){
			System.out.println (e);
		}
	}


	public static void processPara(Para para,LatexMarkupFunction latexMarkupFunction){
		if(para instanceof AxPara)
			processAxPara((AxPara)para, latexMarkupFunction);
	}


	public static void processAxPara(AxPara axPara, LatexMarkupFunction latexMarkupFunction){

		ZFactory zFactory = new ZFactoryImpl();
		List<Parent> parentList = new ArrayList<Parent>();
		parentList.add(zFactory.createParent("standard_toolkit"));
		List<Para> paraList = new ArrayList<Para>();
		paraList.add(axPara);
		ZParaList zParaList = zFactory.createZParaList(paraList);
		ZSect zSect = zFactory.createZSect("Specification", parentList, zParaList);
		List<ZSect> sectList = new ArrayList<ZSect>();
		sectList.add(zSect);
		Spec spec = (new ZFactoryImpl()).createSpec(sectList,"2.1");
		SectionManager manager = new SectionManager();
        manager.put(new Key("spec", Spec.class), spec);
		manager.put(new Key("Specification", LatexMarkupFunction.class), latexMarkupFunction);

		try{
        	LatexString latex = (LatexString) manager.get(new Key("spec", LatexString.class));
    		System.out.println(latex + "\n");
		}
		catch(CommandException e){
			e.printStackTrace();	
		}
	}


	public static LatexMarkupFunction getLatexMarkupFunction(SectionManager manager, Spec spec){
		
		LatexMarkupFunction latexMarkupFunction = null;
		for (Sect sect : spec.getSect()) {
			if (sect instanceof ZSect){
				try{
					latexMarkupFunction = (LatexMarkupFunction)
						manager.get(new Key(((ZSect)sect).getName(), LatexMarkupFunction.class));
				}
				catch(CommandException e){
					e.printStackTrace();	
				}
			}
    	}	
		return latexMarkupFunction;

	}



}
