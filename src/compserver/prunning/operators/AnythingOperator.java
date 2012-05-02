package compserver.prunning.operators;

import java.util.*;
import common.z.TClass;
import common.z.SpecUtils;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Pred;
import common.regex.RegExUtils;

/**
 * This module implements the \anything operator. This operator looks for an expression in the
 * predicate of a test class in which some parts of this expressions can be any expression
 */
public class AnythingOperator implements Operator
{
	public AnythingOperator()
	{
	}
    /**
     * Returns a boolean value that indicates if an atomic pred in the test class satisfies
     * an atomic pred in a elimination theorem that contains the \anything operator
     * @param predicate The predicate of an elimination theorem with real parameters 
     * @param tClass The test class
     */
	public static boolean process(String predicate, TClass tClass)
	{
		String[] preds = predicate.split(";");
		boolean result = false;
		for(int k=0;k<preds.length && !result;k++)
		{
		String predToAnalize = preds[k];

		predToAnalize = predToAnalize.replace("([(][ ])*","");
		predToAnalize = predToAnalize.replace("([ ][)])*","");

		String[] partsPredicate = predToAnalize.split("\\\\anything");
		// We create the regular expression
		String regex = createRegularExpression(partsPredicate);
		AxPara axPara = tClass.getMyAxPara();
		Pred axParaPred = SpecUtils.getAxParaPred(axPara);
		String pred = SpecUtils.termToLatex(axParaPred);
		String[] partsTClassPred = pred.split("\\n");
		for(int i=0;i<partsTClassPred.length && !result;i++)
		{
		//boolean result2 = false;
		String auxPred = partsTClassPred[i].trim();
		if(auxPred.matches(regex)){
			result = true;
		}

		}
		}
		return result;
	}

	public static String createRegularExpression(String[] partsPredicate)
	{
		String regex = partsPredicate[0];
		if(regex.endsWith("~ "))
			regex = regex.trim();
		for(int i=1;i<partsPredicate.length;i++)
		{
			regex = regex+"(.*)"+partsPredicate[i]; 
		}
		regex = regex+"(.*)";
		regex = RegExUtils.addEscapeCharacters(regex);
		return regex;
	}
    /**
     * Replace a line with a \anything operator for an alternative one with the regular
     * expression coresponding to this operator
     * @param originalPred The original atomic predicate
     */
	public String addSemantic(String originalPred)
	{
		String newPred = originalPred.replaceAll("\\\\anything",".*");
		return newPred;
	}
}