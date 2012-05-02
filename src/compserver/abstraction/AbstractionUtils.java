package compserver.abstraction;

import java.util.*;
import compserver.abstraction.types.impltypes.*;
import compserver.abstraction.types.spectypes.*;
import net.sourceforge.czt.z.ast.*;
import common.z.SpecUtils;
import common.z.AbstractTCase;
import compserver.abstraction.capture.CapturedVar;

/**
 * Provides general  utilities related to the process of abstraction
 */
public class AbstractionUtils {    

    /**
     * Returns the variables under monitoring
     * @param law the abstraction law that contains all the information of the monitored
     * variables
     * @return the monitored variables
     */
	public static List<ImplNode> getMonitoredVariables(AbstractionLaw law){
		List<ImplNode> monitoredVars = new ArrayList<ImplNode>();
		List<AbstractionRule> rules = law.getAbstractionRules();
		for(int i=0;i<rules.size();i++){
			AbstractionRule auxRule = rules.get(i);
			monitoredVars.addAll(auxRule.getImplNodes());
		}
		return monitoredVars;
	}
    /**
     * Returns the abstraction rule in the abstraction law that contains among his
     * implementation variables a variable whose name is equals to the argument varName
     * @param law the abstraction law that contains all the information of the monitored
     * variables
     * @param varName the name of the variable
     * @return the rule
     */
	public static AbstractionRule getAbstractionRule(AbstractionLaw law, String varName){
		List<AbstractionRule> rules = law.getAbstractionRules();
		for(int i=0;i<rules.size();i++){
			AbstractionRule rule = rules.get(i);
			List<ImplNode> auxList = rule.getImplNodes();
			for(int j=0;j<auxList.size();j++){
				ImplNode auxNode = auxList.get(j);
				if(auxNode.getImplID().equals(varName))
					return rule;
			}
		}
		return null;
	}
    /**
     * Returns the CapturedVar object in a list whose id is equals to the id that is passed
     * as argument
     * @param capturedVars a list with captured variables
     * @param implID the identifier of a variable in the implementation
     * @return the captured var
     */
	public static CapturedVar getCapturedVar(List<CapturedVar> capturedVars, String implID){
		for(int i=0;i<capturedVars.size();i++){
			CapturedVar auxVar = capturedVars.get(i);
			if(auxVar.getVarName().equals(implID))
				return auxVar;
		}
		return null;
	}
    /**
     * Returns the object that represent the type of a variable 
     * @param tCase the abstract case generated in the generation stage
     * @param varName the name of the variable
     * @return the object
     */
	// Voy a tener que crear un visitante porque puede ocurrir que la secuencia sea parte de un tipo mas grande. Una consideracion a tener en cuenta es que de hecho puede haber mas de una secuencia en el tipo "mas grande" y entonces con solo encontrar la primera secuencia no nos alcanzara
	public static Expr getTypeCZTObject(AbstractTCase tCase, String varName){
		// Tal vez como lo imprimo derecho al ZName esta condicion no sea necesaria
		if(varName.endsWith("?") || varName.endsWith("!"))
			varName = varName.substring(0,varName.length()-1);
		AxPara axPara = tCase.getMyAxPara();
		DeclList declList = SpecUtils.getAxParaListOfDecl(axPara);
		if(declList instanceof ZDeclList){
			ZDeclList zDeclList = (ZDeclList)declList;
			for(int i=0; i< zDeclList.size(); i++){
				Decl decl = ((ZDeclList)declList).get(i);
				if(decl instanceof VarDecl){
					VarDecl varDecl = (VarDecl)decl;
					ZNameList zNameList = varDecl.getZNameList();
					for(int j=0;j<zNameList.size();j++){
						String auxVarName=zNameList.get(j).toString();
						if(auxVarName.equals(varName))
							return varDecl.getExpr();
					}
				}
			}
		}
		return null;
	}
}