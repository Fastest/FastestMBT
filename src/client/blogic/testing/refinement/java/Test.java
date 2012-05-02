package client.blogic.testing.refinement.java;

import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.impl.ZNameImpl;
//import common.z.CztPrinter;

import java.util.*;

/**
 *
 * @author Hache
 */
public class Test {
    
	public static List<AxPara> getAxParaList(Spec spec){
        
		List<AxPara> list = new ArrayList<AxPara>();
        
		for (Sect sect : spec.getSect())
		{
    		
			if (sect instanceof ZSect)
			{
                ZSect 		zSect		= (ZSect)sect;
    			ParaList	paraList	= zSect.getParaList();
    			
    			if (paraList instanceof ZParaList){
    				
    				ZParaList zParaList = (ZParaList) paraList;
    				
    				for(int i = 0; i < zParaList.size(); i++){
    					
    					Para para = zParaList.get(i);
    					
    					if(para instanceof AxPara)
    					{    
    						AxPara axPara	= (AxPara) para;
                            String strBox	= (axPara.getBox()).name();
                            
                            if (strBox.equals("SchBox"))
                                list.add(axPara);
                            
                            //System.out.println(SpecUtils.termToLatex(axPara));
                            //CztPrinter.printAxPara(axPara, 0, 0);
                        }
    				}
    			}
        	}
        }
        return list;
}

//	public static String getZSchemaName(AxPara axPara)
//	{
//    	ZSchText	schText		=	(ZSchText) axPara.getSchText();
//		
//    	ZDeclList	zDecList	=	schText.getZDeclList();
//    		
//    	ZDeclList	list		=	zDecList.getDecl();
//    		
//    	Decl		decl		=	list.get(0);
//    		
//    	Object[]	childs		=	decl.getChildren();
//    		
//    	Object		child		=	childs[0];
//    		
//    	ZNameImpl	zName		=	(ZNameImpl)	child;
//    		
//    	String		name		=	zName.getWord();
//    	
//    	return		name;
//	}

}