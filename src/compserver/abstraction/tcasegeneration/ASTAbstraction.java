package compserver.abstraction.tcasegeneration;

import java.util.*;
import java.net.URL;
import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.impl.*;
import common.z.AbstractTCase;
import common.z.AbstractTCaseImpl;
import common.z.SpecUtils;
import client.blogic.testing.refinement.ConcreteTCase;
import compserver.abstraction.*;
import compserver.abstraction.capture.*;
import compserver.abstraction.types.spectypes.SpecNode;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.ast.AxPara;
import common.z.czt.visitors.CZTCloner;

public class ASTAbstraction{

	public ASTAbstraction(AbstractionLaw law, List<CapturedVar> capturedVars){
		this.law = law;
		this.capturedVars = capturedVars;
		originalAxPara = null;
	}

	public AbstractTCase abstractConcreteTCase(ConcreteTCase concreteTCase){
		// First we set the path of the file to which was redirected the output
		// for that CapturedVar's that are instances of CapturedVarScreen
		String ctcName = concreteTCase.getConcreteTCaseName();
		for(int i=0;i<capturedVars.size();i++){
			CapturedVar auxVar = capturedVars.get(i);
			if(auxVar instanceof CapturedVarScreen){
				CapturedVarScreen auxVarScreen = (CapturedVarScreen) auxVar;
				String pathName = "";
				try{
				URL url = ASTAbstraction.class.getResource("ASTAbstraction.class");
				String urlStr = url.toString();
				String currentDir = urlStr.substring(9,urlStr.indexOf("fastest.jar"));
				pathName = currentDir+"screenFile"+ctcName;
				}
				catch(Exception e){
					e.printStackTrace(System.out);
				}
				auxVarScreen.setScreenPath(pathName);
			}
		} 

		ZFactory zFactory = new ZFactoryImpl();
		Map<RefExpr,Expr> equalities = new HashMap<RefExpr,Expr>();
		List<AbstractionRule> absRules = law.getAbstractionRules();
		// For each rule in the law we obtain a list of predicates of equality that
		// will be added in the predicate of the output abstract case
		for(int i=0;i<absRules.size();i++){
			AbstractionRule rule = absRules.get(i);
			List<SpecNode> specNodes = rule.getSpecNodes();
			List<Expr> exprs = VarAbstraction.abstractRule(rule, capturedVars);
			for(int j=0;j<exprs.size();j++){
			// La expresion i-esima se corresponde con el SpecNode i-esimo
				Expr expr = exprs.get(j);
				SpecNode specNode = specNodes.get(j);
				String specNodeID = specNode.getSpecID();
				if(expr!=null){
				RefExpr varRefExpr = SpecUtils.createVarRefExpr(specNodeID);
				equalities.put(varRefExpr,expr);
				}
			}
		}

		Pred predPos = SpecUtils.createAndPred(equalities);
		AbstractTCase abstractTCasePre = concreteTCase.getAbstractTCase();
		AxPara axParaPre = abstractTCasePre.getMyAxPara();
		Pred predPre = SpecUtils.getAxParaPred(axParaPre);
		Pred finalPred = SpecUtils.andPreds(predPre, predPos);
		AxPara cloneAxPara = (AxPara) originalAxPara.accept(new CZTCloner());
		SpecUtils.setAxParaPred(cloneAxPara,finalPred);
		SpecUtils.setAxParaName(cloneAxPara, abstractTCasePre.getSchName()+"COUTPUT");

		AbstractTCase abstractTCasePos = new AbstractTCaseImpl(cloneAxPara, abstractTCasePre.getSchName()+"COUTPUT");
		return abstractTCasePos;
	}

	/**
	* Sets the AxPara of the operation associated to the ConcreteTCase
	*/
	public void setOriginalAxPara(AxPara axPara){
		this.originalAxPara = axPara;
	}


	private AbstractionLaw law;
	private List<CapturedVar> capturedVars;
	private AxPara originalAxPara;
}
