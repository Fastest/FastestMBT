package compserver.prunning;

import java.io.*;
import java.util.*;

import client.blogic.testing.ttree.TClassNode;
import client.blogic.management.Controller;
import client.blogic.testing.ttree.visitors.TTreeFromPruner;
import client.blogic.testing.ttree.visitors.TTreeBelowPruner;
import common.repository.ConcreteRepository;
import common.repository.AbstractRepository;
import common.z.SpecUtils;
import net.sourceforge.czt.z.ast.AxPara;
import common.z.SpecUtils;
import common.z.czt.visitors.DeleteParenAnn;
import common.z.TClass;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.Pred;
import common.repository.AbstractIterator;
import compserver.prunning.typechecking.ParamExaminerPred;
import net.sourceforge.czt.session.SectionManager;
import compserver.prunning.operators.SpecialLine;
import compserver.prunning.operators.OperatorAnalizer;
import compserver.prunning.typechecking.TypeChecker;
import net.sourceforge.czt.z.ast.Type;

/**
 * Instances of this class provide funcionality for prunning the Test Tree
 */
public class TreePruner {
    
    private Controller controller;
    private static TreePruner treePruner;
    
    /**
     * Creates instaces of TreePruner.
     * @param controller a reference to the Controller.
     */
    public TreePruner(Controller controller){
        this.controller = controller;
    }

    /**
     * Creates intances of TreePruner.
     */
	public TreePruner(){
	}

    /**
     * Prunes a Test Class from a test tree 
     * @param tClassName The name of the test class
     * @return A boolean value that indicates the prunning result
     */
	public boolean pruneFrom(String tClassName)
	{
            Map<String, TClassNode> opTTreeMap = controller.getOpTTreeMap();
            Set<Map.Entry<String, TClassNode>> set = opTTreeMap.entrySet();
            Iterator<Map.Entry<String, TClassNode>> iterator = set.iterator();
            Boolean result = new Boolean(false);
            TTreeFromPruner tTreeFromPruner = new TTreeFromPruner(tClassName, true);
            while(iterator.hasNext() && !result.booleanValue()){
                Map.Entry<String, TClassNode> mapEntry = iterator.next();
                TClassNode opTTreeRoot = mapEntry.getValue();  
                result = opTTreeRoot.acceptVisitor(tTreeFromPruner);
            }          

	    return result.booleanValue();
	}
    /**
     * Prunes a sub tree of a test tree 
     * @param tClassName The name of the test class that is the root of the sub tree
     * @return A boolean value that indicates the prunning result
     */
	public boolean pruneBelow(String tClassName)
	{
            Map<String, TClassNode> opTTreeMap = controller.getOpTTreeMap();
            Set<Map.Entry<String, TClassNode>> set = opTTreeMap.entrySet();
            Iterator<Map.Entry<String, TClassNode>> iterator = set.iterator();
            Boolean result = new Boolean(false);
            TTreeBelowPruner tTreeBelowPruner = new TTreeBelowPruner(tClassName, true);
            while(iterator.hasNext() && !result.booleanValue()){
                Map.Entry<String, TClassNode> mapEntry = iterator.next();
                TClassNode opTTreeRoot = mapEntry.getValue();
                result = opTTreeRoot.acceptVisitor(tTreeBelowPruner);
            }
	    return result.booleanValue();
	}
    /**
     * Analyze the prunning of a test class with the elimination theorems 
     * @param tClassName The name of the test class
     * @param specInfo Relevant information of the specification associated
     * @return A boolean value that indicates the prunning result
     */
	public synchronized ResultPrune pruneTree(TClass tClass, SpecInfo specInfo)
	{
		TheoremsChecker theoremsChecker = new TheoremsChecker(tClass);
		String tClassName = SpecUtils.getAxParaName(tClass);
		String theoremName = "";
		List<String> params = new ArrayList<String>();
		boolean result = false;

		Map<String, List<Map<String,String>>> matches = theoremsChecker.findParameters();
		while(matches.size()>0 && !result){
			Set<Map.Entry<String, List<Map<String,String>>>> set = matches.entrySet();
			Iterator<Map.Entry<String, List<Map<String,String>>>> iterator = set.iterator();
			while(iterator.hasNext() && !result){
				Map.Entry<String, List<Map<String,String>>> mapEntry = iterator.next();
				theoremName = mapEntry.getKey();
				List<Map<String,String>> listMatches = mapEntry.getValue();
				for(int i=0;i<listMatches.size() && !result;i++){
					// Deberia ir tras el chequeo y el eval
					params.clear();
					result = true;
					Map<String, String> mapFR = listMatches.get(i);

				//We extract the real parameters in the correct order
				Theorem theTheorem = PruneUtils.getTheorem(theoremName);
				List<Variable> formalParameters = theTheorem.getFormalParamList();
				for(int j=0;j<formalParameters.size();j++){
					Variable formalVar = formalParameters.get(j);
					String formalName = formalVar.getName();
					String realName = mapFR.get(formalName);
					//System.out.println("Formal: "+formalName);
					//System.out.println("Real: "+realName);
					params.add(realName);
				}
				TypeChecker typeChecker = new TypeChecker();
				result = typeChecker.checkParametersTypes(theTheorem, tClass, params);
				if(result){
				List<SpecialLine> specialLines = theTheorem.getSpecialLines();
				if(specialLines.size()>0){
				Map<String,Type> mapFormalReal = typeChecker.getMapFR();
				String pred = PruneUtils.replaceParameters(theoremName, params,"SpecialPredicates", mapFormalReal);
				result = (new OperatorAnalizer()).analize(specialLines, pred, tClass, specInfo);
				}
				}
				}
			}
		if(!result)
			matches = theoremsChecker.findParameters();
		}
		ResultPrune resultPrune = new ResultPrune(tClassName, theoremName, params, result);
		return resultPrune;

	}
}