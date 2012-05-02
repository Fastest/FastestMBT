package compserver.prunning;

import java.util.*;
import client.blogic.management.Controller;
import common.z.TClass;
import compserver.prunning.operators.SpecialLine;
import compserver.prunning.operators.OperatorAnalizer;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Pred;
import common.z.SpecUtils;
import common.z.czt.visitors.DeleteParenAnn;


public class PruneAnalizer
{
	public PruneAnalizer()
	{
		intensive = false;
	}

	public boolean analize(String theoremName, TClass tClass, List<String> params)
	{
		// We obtain the theorem
		Theorem theTheorem = PruneUtils.getTheorem(theoremName);
		if(theTheorem==null){
			System.out.println(theoremName+" is not the name of a loaded theorem.");
			return false;
		}
		// We replace in the theorem formal parameters for real parameters
		String predicate = PruneUtils.replaceParameters(theoremName, params, "PredicatesToMatch",null);
		if(predicate==null)
			return false;
		//We obtain the TClass under analysis and then we check if is an empty class
		/*TClass tClass = PruneUtils.searchTClass(tClassName, controller);
		if(tClass==null){
			System.out.println(tClassName+" is not the name of any test class of the generated test trees");
			return false;
		}*/
		if(!intensive){
			//AxPara axPara = tClass.getMyAxPara();
			//axPara = TypecheckingUtils.deleteUnnecessaryParenthesis(axParaAux, manager);
			//Pred pred = SpecUtils.getAxParaPred(axPara);
			//if(pred!=null)
			//	pred = (Pred) pred.accept(new DeleteParenAnn());
			//else
			//	return false;
		}

		boolean pruneable = PruneUtils.matchPredicates(tClass, predicate);
		boolean finalResp=false;
		if(pruneable)
		{
			List<SpecialLine> lines = theTheorem.getSpecialLines();
			// Check for the presence in the theorem of fastest's operators
			if(lines.size()>0)
			{
				String pred = PruneUtils.replaceParameters(theoremName, params,"SpecialPredicates",null);
				// NO FUNCIONA!!!
				// Con los cambios para las definiciones axiomaticas ya no es funcional
				// Ver si vale la pena arreglarlos!
				boolean specials = (new OperatorAnalizer()).analize(lines, pred, tClass, null);
				if(specials)
					finalResp = true;
					//finalResp = (new TreePruner(controller)).pruneFrom(tClassName); 
			}
			else
				finalResp = true;
				//finalResp = (new TreePruner(controller)).pruneFrom(tClassName); 

		}
		return finalResp;	
	}

	public void setIntensiveMode()
	{
		intensive = true;
	}

	private boolean intensive;
	//private Controller controller;
}