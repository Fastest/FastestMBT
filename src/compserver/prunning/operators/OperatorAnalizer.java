package compserver.prunning.operators;

import java.util.*;
import common.z.TClass;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import compserver.prunning.SpecInfo;


/**
 * Provides utilities related to the analysis of Fastest's operators
 */
public class OperatorAnalizer
{
	public OperatorAnalizer()
	{
	}
    /**
     * Analyzes if the predicate of a test class satisfies predicates of a theorem with
     * Fastest's operators
     * @param tClass The TClass
     * @param predicate The predicate of the theorem with the real parameters
     * @param lines The predicates in the theorem with Fastest's operators
     * @return boolean
     */
	public boolean analize(List<SpecialLine> lines, String predicate, TClass tClass, SpecInfo specInfo)
	{

		SpecialLine sLine;
		boolean result = true;
		String[] partsPredicate = predicate.split("\n");

		for(int i=0;i<lines.size() && result;i++)
		{
			sLine = lines.get(i);
			String operatorName = sLine.getOperator();
			String line = sLine.getLine();
			
			if(operatorName.equals("\\eval"))
				 result = EvalOperator.process(partsPredicate[i],specInfo);
			else if(operatorName.equals("\\sw"))
				result = SwOperator.process(partsPredicate[i], tClass);
			else if(operatorName.equals("\\anything"))
				result = AnythingOperator.process(partsPredicate[i], tClass);
			else if(operatorName.equals("\\se"))
				result = (new SeOperator()).process(partsPredicate[i], tClass);
		}	
		return result;
	}
}