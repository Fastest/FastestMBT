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
import common.z.czt.visitors.FuncApplExtractor;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.TupleExpr;
import net.sourceforge.czt.z.ast.ZExprList;
import java.util.regex.Matcher;

public class RWUnion implements RWRuleOperator{
	public RWUnion(){
		operator = "\\cup";
	}
	public String getOperator(){
		return operator;
	}
	public String rewrite(String originalExpr){
		String[] parts = originalExpr.split(";");
		String alternativeExprs = "";
		List<Term> terms = null;
		FuncApplExtractor fExtractor = new FuncApplExtractor(operator);
		ZLive zLive = UniqueZLive.getInstance();
		SectionManager manager = zLive.getSectionManager();
		for(int j=0;j<parts.length;j++){
		//IntersectionExtractor iExtractor = new IntersectionExtractor();
		String param = parts[j];
		String leftOperand="";
		String rightOperand="";
		try{
		Pred parsedPred = ParseUtils.parsePred(new StringSource(param),zLive.getCurrentSection(), zLive.getSectionManager());
		//System.out.println("La expresion parseada es:\n"+
		//SpecUtils.termToLatex(parsedPred));
		terms = parsedPred.accept(fExtractor); 
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		for(int i=0;i<terms.size();i++){
		Term auxTerm = terms.get(i);
		String strTerm = SpecUtils.termToLatex(auxTerm);
		String operands[] = strTerm.split("cup ");
		leftOperand = operands[0].substring(0, operands[0].length()-2).trim();
		rightOperand = operands[1].trim();
		leftOperand = RegExUtils.addEscapeCharacters(leftOperand);
		rightOperand = RegExUtils.addEscapeCharacters(rightOperand);
		String oldExpr = leftOperand+" \\\\cup "+rightOperand;
		String newExpr = rightOperand+" \\\\cup "+leftOperand; 
		alternativeExprs = alternativeExprs+";"+param.replaceAll(oldExpr, newExpr);
		}
		}
		return originalExpr+alternativeExprs;
	}
	private String operator;
}