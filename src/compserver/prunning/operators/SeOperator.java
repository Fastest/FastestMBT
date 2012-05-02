package compserver.prunning.operators;

import java.util.*;
import common.z.TClass;
import common.z.SpecUtils;
import net.sourceforge.czt.z.ast.Pred;
import common.regex.RegExUtils;

/**
 * This module implements the \se operator. This operator looks for an expression in the
 * scope of a set extension
 */
public class SeOperator implements Operator
{
	public SeOperator()
	{
	}
    /**
     * Replace a line with a \se operator for an alternative one with the regular
     * expression coresponding to this operator
     * @param originalPred The original atomic predicate
     */
	public String addSemantic(String originalPred)
	{
		String[] partsPred = originalPred.split(";");
		String newPred = "";
		for(int i=0;i<partsPred.length;i++)
			newPred+=partsPred[i].replaceAll("\\\\se\\( (.*) \\)", "\\\\{[^:\n]*? $1 .*?\\\\}")+";";
		newPred = newPred.substring(0,newPred.length()-1);

		//String newPred = originalPred.replaceAll("\\\\se\\( (.*) \\)", "\\\\{[^:]*? $1 .*?\\\\}");
		return newPred;
	}
    /**
     * Returns a boolean value that indicates if an expression is the scope of a set extension
     * in the predicate of a test class
     * @param predicate The predicate of an elimination theorem with real parameters 
     * @param tClass The test class
     */
	public boolean process(String predicate, TClass tClass)
	{
		predicate = predicate.replace("([(][ ])*","");
		predicate = predicate.replace("([ ][)])*","");
		predicate = "[ ]*"+predicate+"[ ]*";
		boolean match = false;
		String newPred = addSemantic(predicate);
		newPred = RegExUtils.addEscapeCharacters(newPred);
		Pred tClassPred = SpecUtils.getAxParaPred(tClass);
		String strTClassPred = SpecUtils.termToLatex(tClassPred);
		String[] partsPredicate = strTClassPred.split("\n");
		for(int i=0;i<partsPredicate.length;i++){
			if(partsPredicate[i].endsWith("\\\\"))
				partsPredicate[i] = partsPredicate[i].substring(0, partsPredicate[i].length() -3);
			if(partsPredicate[i].matches(newPred)){
				match = true;
			}
		}
		return match;
	}

}