package pruebas.texprinting;

import java.util.*;

import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.impl.*;
import net.sourceforge.czt.print.util.LatexString;

//INTENTA imprimir desde el AxPara con LatexString creando una nueva estructura que lo envuelva - FUNCIONA!
// PERO tira warnings
public class TexPrinting6{

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
					processSect((ZSect)sect);
    		}

 		}
    	catch(Exception e){
			System.out.println (e);	
		}
	}


	public static void processSect(ZSect zSect){
		ParaList paraList = zSect.getParaList();
		if (paraList instanceof ZParaList) {
			ZParaList zParaList = (ZParaList) paraList;
			for(int i=0; i< zParaList.size(); i++)
				processPara(zParaList.get(i));
		}	
	}


	public static void processPara(Para para){
		if(para instanceof AxPara)
			processAxPara((AxPara)para);
	}


	public static void processAxPara(AxPara axPara){

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

		try{
        	LatexString latex = (LatexString) manager.get(new Key("spec", LatexString.class));
    		System.out.println(latex);
		}
		catch(CommandException e){
			e.printStackTrace();	
		}
	}
}
