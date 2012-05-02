package compserver.prunning.typechecking;

import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.session.SectionManager;
import net.sourceforge.czt.z.ast.AxPara;
import common.z.SpecUtils;
import net.sourceforge.czt.parser.z.ParseUtils;
import net.sourceforge.czt.z.ast.Para;
import net.sourceforge.czt.z.ast.ParaList;
import net.sourceforge.czt.z.ast.ZParaList;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.ast.Pred;
import common.z.czt.visitors.DeleteParenAnn;
import net.sourceforge.czt.z.ast.Sect;
import net.sourceforge.czt.z.ast.ZSect;
import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.session.*;

/**
 * Provides general  utilities related to the typechecking
 */
public class TypecheckingUtils{

    /**
     * Delete all unnecessary parenthesis of an AxPara 
     * @param oldAxPara 
     */
	public static AxPara deleteUnnecessaryParenthesis(AxPara oldAxPara, SectionManager manager)
	{
		String tcName = SpecUtils.getAxParaName(oldAxPara);
		Pred pred = SpecUtils.getAxParaPred(oldAxPara);
		pred = (Pred) pred.accept(new DeleteParenAnn());
		SpecUtils.setAxParaPred(oldAxPara, pred);
		String strAxPara = SpecUtils.termToLatex(oldAxPara);
		Term parsedTerm = null;
		Pred tClassPred = null;
		AxPara axPara = null;
		try{
		// We parse the string representation of AxPara and obtain it from the
		// resulting Spec
		parsedTerm = ParseUtils.parse(new StringSource(strAxPara), manager);
		Spec auxSpec = (Spec) parsedTerm;
		for (Sect sect : auxSpec.getSect()) {
			if (sect instanceof ZSect) {
				ParaList paraList = ((ZSect) sect).getParaList();
				if (paraList instanceof ZParaList) {
					for(int i=0; i < ((ZParaList)paraList).size(); i++){
						Para para = ((ZParaList)paraList).get(i);
						if (para instanceof AxPara){
							axPara = (AxPara) para;
						}
					}
				}
			}
		}
		}
		catch(Exception e){
			System.out.println("There was an error in the deleting of parenthesis stage");
			System.out.println(e);
		}
		return axPara;
	}

}