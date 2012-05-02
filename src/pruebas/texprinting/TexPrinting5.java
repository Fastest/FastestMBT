package pruebas.texprinting;

import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.impl.*;
import java.io.*;
import java.util.*;

//Imprime desde el AxPara con PrintUtils creando una nueva estructura que lo envuelva - FUNCIONA!
// PERO tira warnings
public class TexPrinting5{

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

		StringWriter out = new StringWriter();
		//PrintUtils.printLatex(spec, out, manager);
		System.out.println(out.toString());

	}


}