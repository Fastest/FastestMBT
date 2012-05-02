package compserver.prunning.operators;

import java.util.*;
import common.z.SpecUtils;
import common.z.czt.UniqueZLive;
import compserver.prunning.PruneUtils;
import compserver.prunning.SpecInfo;
import net.sourceforge.czt.parser.z.ParseUtils;
import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.animation.eval.ZLive;
import net.sourceforge.czt.session.*;
import common.z.czt.visitors.StringToNumReplacer;
import common.regex.RegExUtils;
import net.sourceforge.czt.z.impl.ZFactoryImpl;

/**
 * This module implements the \eval operator. This operator compare constants values
 */
public class EvalOperator implements Operator
{
	public EvalOperator()
	{
	}
    /**
     * Returns a boolean value that indicates the result of the constants comparation
     * @param line
     */
	public static boolean process(String line, SpecInfo specInfo)
	{
		ZLive zLive = UniqueZLive.getInstance();
		// We delete the parts related to regular expressions
		line = line.replace("([(][ ])*","");
		line = line.replace("([ ][)])*","");

		// We obtain all the information related to the specification that we need
		Map<RefExpr,Expr> axDefsValues = specInfo.getAxDefsValues();
		List<FreePara> freeParaList = specInfo.getFreeParaList();

		// Reemplazamos los valores seteados de las definiciones axiomaticas
		if (axDefsValues != null) {
		Set<Map.Entry<RefExpr, Expr>> set = axDefsValues.entrySet();
		Iterator<Map.Entry<RefExpr, Expr>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<RefExpr, Expr> mapEntry = iterator.next();
			RefExpr refExpr = mapEntry.getKey();
			Expr expr = mapEntry.getValue();
			String strRefExpr = SpecUtils.termToLatex(refExpr);
			String strExpr = SpecUtils.termToLatex(expr);
			strExpr = RegExUtils.addEscapeCharacters(strExpr);
			line = PruneUtils.replaceAllOccurrences(line, strRefExpr, strExpr);
		}
		}

		// Reemplazamos los nombres de los tipos libres por un conjunto con sus elementos
		// Primero debemos chequear si se trata de este tipo de evaluaciones
		//if(line.substring(6,line.length()-1).matches("\\w+[ ]*=[ ]*\\\\\\{ .+ \\\\\\}[ ]*")){
		ZFactory zFactory = new ZFactoryImpl();
		SetExpr setExpr = zFactory.createSetExpr();
		ZExprList zExprList = zFactory.createZExprList();
		String freeTypeName = "";
		boolean found = false;
		for(int i=0;i<freeParaList.size() && !found;i++){
			FreePara freePara = freeParaList.get(i);
			FreetypeList freeTypeList = freePara.getFreetypeList();
			if(freeTypeList instanceof ZFreetypeList){
				ZFreetypeList zFreetypeList = (ZFreetypeList) freeTypeList;
				for(int j=0;j<zFreetypeList.size() && !found;j++){
					Freetype freetype = zFreetypeList.get(j);
					freeTypeName = freetype.getZName().getWord();

					//if(line.substring(6,line.length()-1).startsWith(freeTypeName+" ")){
					if(line.contains(" "+freeTypeName+" ")){
					found = true;
					BranchList branchList = freetype.getBranchList();
					if(branchList instanceof ZBranchList){
					ZBranchList zBranchList = (ZBranchList) branchList;
					for(int k=0;k<zBranchList.size();k++){
						Branch branch = zBranchList.get(k);
						Name branchName = branch.getName();
						RefExpr refExpr = zFactory.createRefExpr(branchName, zFactory.createZExprList(),false,false);
						zExprList.add(refExpr);
					}
					}
					}
				}
			}
		}
		setExpr.setExprList(zExprList);
		String strSetExpr = SpecUtils.termToLatex(setExpr);
		line = line.replace(" "+freeTypeName+" "," "+strSetExpr+" ");

		// We parse the line with the replacements
		Pred pred = null;
		StringToNumReplacer stringToNumReplacer = new StringToNumReplacer();
		try{
		pred = ParseUtils.parsePred(new StringSource(line.substring(6,line.length()-1)),zLive.getCurrentSection(), zLive.getSectionManager());
		pred = (Pred) pred.accept(stringToNumReplacer);
		Term result = zLive.evalPred(pred);
		String strResult = SpecUtils.termToLatex(result);
		Boolean b = new Boolean(strResult);
		return b.booleanValue();
		}
		catch(Exception e)
		{
			//e.printStackTrace(System.out);
			//System.out.println("Excepcion!!!");
			return false;
		}
	}
    /**
     * Replace a line with a \eval operator for an alternative one with the regular
     * expression corresponding to this operator
     * @param originalPred The original atomic predicate
     */
	public String addSemantic(String originalPred)
	{
		return originalPred;
	}
}