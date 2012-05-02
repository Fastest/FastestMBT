package compserver.prunning.rewriting.rwrules;

import java.util.*;
import net.sourceforge.czt.parser.z.ParseUtils;
import common.z.czt.UniqueZLive;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.session.*;
import net.sourceforge.czt.animation.eval.*;
import common.z.SpecUtils;
import common.regex.RegExUtils;
import common.z.UtilSymbols;
import common.z.czt.visitors.MemPredExtractor;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.TupleExpr;
import net.sourceforge.czt.z.ast.ZExprList;


public class RWEquality implements RWRuleOperator{
	public RWEquality(){
		operator = "=";
	}
	public String getOperator(){
		return operator;
	}
	public String rewrite(String originalExpr){
		if(originalExpr.indexOf("\\eval")>-1)
			return originalExpr;

		String[] parts = originalExpr.split(";");
		String alternativeExprs = "";
		List<Term> terms = null;
		MemPredExtractor mExtractor = new MemPredExtractor(operator);
		ZLive zLive = UniqueZLive.getInstance();
		SectionManager manager = zLive.getSectionManager();
		for(int j=0;j<parts.length;j++){
		String param = parts[j];
		String leftOperand="";
		String rightOperand="";
		try{
		Pred parsedPred = ParseUtils.parsePred(new StringSource(param),zLive.getCurrentSection(), zLive.getSectionManager());
		//Pred parsedPrueba = ParseUtils.parsePred(new StringSource(prueba),zLive.getCurrentSection(), zLive.getSectionManager());
		//Term auxTerm = RewriteUtils.rewrite(parsedPrueba, zLive.getSectionManager(), zLive.getCurrentSection());
		//System.out.println("Original:\n" + SpecUtils.termToLatex(parsedPrueba) + "Reescrita:\n" + SpecUtils.termToLatex(auxTerm));
		//System.out.println("La expresion parseada es:\n"+
		//SpecUtils.termToLatex(parsedPred));
		terms = parsedPred.accept(mExtractor);
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
			System.out.println(e);
		}
		for(int i=0;i<terms.size();i++){
		Term auxTerm = terms.get(i);
		String oldExpr,newExpr;
		String strTerm = SpecUtils.termToLatex(auxTerm);
		String operands[] = strTerm.split(" = ");
		if(operands.length>1){
		leftOperand = operands[0].trim();
		rightOperand = operands[1].trim();
		//System.out.println("Before:\n"+leftOperand+"\n"+rightOperand);
		leftOperand = RegExUtils.addEscapeCharacters(leftOperand);
		rightOperand = RegExUtils.addEscapeCharacters(rightOperand);
		//System.out.println("After:\n"+leftOperand+"\n"+rightOperand);
		oldExpr = leftOperand+" = "+rightOperand;
		newExpr = rightOperand+" = "+leftOperand;
		}
		else{
			operands = strTerm.split(" =~");
			leftOperand = operands[0].trim();
			rightOperand = operands[1].trim();
		leftOperand = RegExUtils.addEscapeCharacters(leftOperand);
		rightOperand = RegExUtils.addEscapeCharacters(rightOperand);
			oldExpr = leftOperand+" =~"+rightOperand;
			newExpr = rightOperand+" =~"+leftOperand;
			// ANALIZAR - Debe estar mal!!!!
		}
		//System.out.println("Los operandos son:\n"+leftOperand+" y \n"+rightOperand);


		//System.out.println("La real:\n"+param+"\nLa que quiero reemplazar:\n"+oldExpr); 

		alternativeExprs = alternativeExprs+";"+param.replaceAll(oldExpr, newExpr);
		}
		}
		return originalExpr+alternativeExprs;
	}
	private String operator;
}