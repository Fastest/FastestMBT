package compserver.prunning.operators;

import java.util.*;
import common.z.TClass;
import common.z.SpecUtils;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Pred;
import common.regex.RegExUtils;

public class SwOperator implements Operator
{
/**
 * This module implements the \sw operator. This operator looks for an expression in the
 * predicate of a test class
 */
	public SwOperator()
	{
	}
    /**
     * Returns a boolean value that indicates if an expression is in the predicate of a class
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

		String[] partsPredicate = predToAnalize.split("\\\\sw");
		List<String> toFound = new ArrayList<String>();
		for(int i=1;i<partsPredicate.length-1;i++)
		{
			toFound.add(partsPredicate[i].substring(partsPredicate[i].indexOf("(")+1, partsPredicate[i].indexOf(")")).trim());
			partsPredicate[i] = partsPredicate[i].substring(partsPredicate[i].indexOf(")")+1, partsPredicate[i].length()-1);
			//System.out.println(partsPredicate[i]);
		}
		toFound.add(partsPredicate[partsPredicate.length-1].substring(partsPredicate[partsPredicate.length-1].indexOf("(")+1, partsPredicate[partsPredicate.length-1].indexOf(")")).trim());
		partsPredicate[partsPredicate.length-1] = partsPredicate[partsPredicate.length-1].substring( partsPredicate[partsPredicate.length-1].indexOf(")")+1);
		//System.out.println(partsPredicate[partsPredicate.length-1]);
		/*for(int i=0;i<partsPredicate.length;i++)
			System.out.println("Pred: "+partsPredicate[i]);
		for(int i=0;i<toFound.size();i++)
			System.out.println("To found: "+toFound.get(i));*/
		
		String regex = createRegularExpression(partsPredicate, toFound);
		AxPara axPara = tClass.getMyAxPara();
		Pred axParaPred = SpecUtils.getAxParaPred(axPara);
		String pred = SpecUtils.termToLatex(axParaPred);
		String[] partsTClassPred = pred.split("\r\n|\r|\n");
		for(int i=0;i<partsTClassPred.length && !result;i++)
		{
		String auxPred = partsTClassPred[i].trim();
		if(auxPred.endsWith("\\\\"))
			auxPred = auxPred.substring(0,auxPred.length()-3);
		if(auxPred.matches(regex)){
			result = true;
		}
		}
		}
		return result;
	}

	public static String createRegularExpression(String[] partsPredicate, List<String> toFound)
	{
		String regex = partsPredicate[0];
		for(int i=0;i<partsPredicate.length-1;i++){
			String auxToFound = toFound.get(i);
			if(auxToFound.contains("\\anything")){
				String[] parts = auxToFound.split("\\\\anything");
				String auxRegex = AnythingOperator.createRegularExpression(parts);
				regex = regex+".*"+auxRegex+".*"+partsPredicate[i+1];
			}
			else{
				regex = regex+".*"+auxToFound+".*"+partsPredicate[i+1];
				regex = RegExUtils.addEscapeCharacters(regex); 
			}
		}

		return regex;
	} 
    /**
     * Replace a line with a \sw operator for an alternative one with the regular
     * expression coresponding to this operator
     * @param originalPred The original atomic predicate
     */
	public String addSemantic(String originalPred)
	{
		String newPred = originalPred.replaceAll("\\\\sw\\( (.*) \\)", ".*?$1.*");
		return newPred;
	}

}