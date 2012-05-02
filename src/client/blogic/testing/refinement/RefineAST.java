package client.blogic.testing.refinement;

import java.util.*;
import net.sourceforge.czt.z.ast.*;

import common.z.AbstractTCase;
import common.z.SpecUtils;


/**
* This module is responsible for the refinement of an abstract case 
* using a refinement law.
*/

public class RefineAST{

    /**
     * Obtains a concrete case in the corresponding target language from an abstract
     * case and a refinement law.
     * @param ast The parsed refinement law
     * @param atc The abstract test case
     * @param pathUUT The file path of the UUT
     * @param targetLanguaje The target language
     * @return A concrete test case
     */
    public static ConcreteTCase refine(TCRL_AST ast, AbstractTCase atc,
            String pathUUT, String targetLanguaje){
        ConcreteTCase ctc = new ConcreteTCase();
	// We set some fields in the concrete case
        ctc.setPreamble(ast.getPreamble());
        ctc.setEpilogue(ast.getEpilogue());
	ctc.setPathUUT(pathUUT);
	ctc.setLanguaje(targetLanguaje);

	// We analyze the targetLanguaje and create the corresponding refiner
	RefineExpr refiner = null;
	if(targetLanguaje.equals("C")){ // Analizar incongruencia may/min
		refiner = new RefineExprC();
	}
	else if(targetLanguaje.equals("Java"))
		refiner = new RefineExprJava();

	// We obtain the predicate of the abstract case
        AxPara axPara = atc.getMyAxPara();
        Pred atcPred = SpecUtils.getAxParaPred(axPara);

	// We obtain the rules in the AST and send them to refine
        NodeRules rules = ast.getRules();
	Set<String> ruleNames = rules.getKeys();
	Iterator<String> itRules = ruleNames.iterator();
	while(itRules.hasNext()){
		String ruleName = itRules.next();
		NodeRule rule = rules.getRule(ruleName);
		if(rule instanceof RuleRefinement){
			RuleRefinement refRule = (RuleRefinement) rule;
			List<TCaseAssignment> assigns = refiner.refineRule(refRule, atcPred);
			ctc.addTCaseAssignment(assigns);
		}
	}

        return ctc;
    }
}
