package compserver.prunning;

import java.util.*;
import client.blogic.management.Controller;
import common.z.SpecUtils;
import net.sourceforge.czt.z.ast.Pred;
import common.repository.AbstractIterator;
import common.z.TClass;
import client.blogic.testing.ttree.visitors.TClassLeavesFinder;
import net.sourceforge.czt.z.ast.AxPara;
import common.repository.AbstractRepository;
import client.blogic.testing.ttree.TClassNode;
import compserver.prunning.operators.SpecialLine;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import common.repository.ConcreteRepository;
import common.regex.RegExUtils;
import net.sourceforge.czt.z.ast.Type;
import net.sourceforge.czt.z.ast.GivenType;
import client.blogic.testing.ttree.visitors.PrePrunerVisitor;

/**
 * Provides general  utilities related to the prune of empty classes.
 */
public class PruneUtils {

    /**
     * Returns a Theorem with the specified name
     * @param name
     */
	public static Theorem getTheorem(String name)
	{
		TheoremsControl theoremsControl = TheoremsControl.getInstance();
		AbstractIterator<Theorem> theoremsIt = theoremsControl.createIterator();
		Theorem result = null;
		while(theoremsIt.hasNext() && result==null)
		{
			Theorem auxTheorem = theoremsIt.next();
			if(auxTheorem.getName().equals(name))
				result = auxTheorem;
		}
		return result;
	}

    /**
     * Returns the predicate that results of replace in a Theorem
     * the formal parameters for the real parameters
     * @param theoremName The name of the theorem
     * @param realParams The real parameters
     * @param predicateType The type of predicate (with or without Fastest's operators)
     * @return The resulting predicate in text format
     */
	public static String replaceParameters(String theoremName, List<String> realParams, String predicateType, Map<String,Type> mapFR)
	{
		// We obtain the Theorem from the repository
		Theorem theTheorem = getTheorem(theoremName);
		if(theTheorem==null)
		{
			System.out.println(theoremName+" is not the name of a loaded theorem.");
			return null; // Disparo excepcion???
		}
		// We obtain the predicate of the theorem
		String predicate = "";
		if(predicateType.equals("PredicatesToMatch"))
			predicate = theTheorem.getPredicatesToMatch();
		else if(predicateType.equals("SpecialPredicates")){
			List<SpecialLine> lines = theTheorem.getSpecialLines();
			for(int i=0;i<lines.size();i++){
				SpecialLine auxLine = lines.get(i);
				predicate+=auxLine.getLine()+"\n";
			}
		}
		// We obtain the formal parameters
		List<Variable> formalParams = theTheorem.getFormalParamList();
		if(formalParams.size() != realParams.size())
		{
			System.out.println("The cardinality of the real parameters not match with the cardinality of formal parameters of the theorem "+theoremName);
			return null; // Disparo excepcion???
		}

		// Chequeo si alguno de los parametros formales tiene el mismo nombre que
		// alguno de los reales y lo modifico
		boolean areConflictive = changeConflictiveNames(formalParams, realParams);
		if(areConflictive){
			for(int i=0;i<formalParams.size();i++){
			Variable auxVar = formalParams.get(i);
			String formalParam = auxVar.getName();
			if(formalParam.endsWith("BiS")){
			String originalParam = formalParam.substring(0, formalParam.length() - 3);
			predicate = replaceAllOccurrences(predicate, originalParam, formalParam);
			}
			}
		}


		for(int i=0;i<formalParams.size();i++)
		{
			Variable auxVar = formalParams.get(i);
			String formalParam = auxVar.getName();
			String realParam = realParams.get(i);
			// We obtain all the ocurrences of the formal parameters
			// and replace them for the real parameters
			predicate = replaceAllOccurrences(predicate, formalParam, "([(][ ])*"+realParam+"([ ][)])*");
			//predicate = replaceAllOccurrences(predicate, formalParam, realParam);
		}

		// Finally we check if any of the types of the parameters appears in the predicate
		if(predicateType.equals("SpecialPredicates")){
			for(int i=0;i<formalParams.size();i++)
			{
				Variable auxVar = formalParams.get(i);
				String formalType = auxVar.getType();
				String typeName = "";
				if(formalType.startsWith("\\const"))
					formalType = (formalType.substring(6)).trim();
				String formalParam = auxVar.getName();
				Type type = mapFR.get(formalType);
				if(type!=null && type instanceof GivenType){
					GivenType givenType = (GivenType) type;
					typeName = SpecUtils.termToLatex(givenType.getName());
					predicate = predicate.replaceAll("[ ]+"+formalType+"[ ]+", " "+typeName+" ");
				}
			}
		}
		return predicate;
	}

    /**
     * Obtains the TClass whose name is tClassName
     * @param tClassName The name of the TClass
     * @param controller A reference to the Controller
     * @return The TClass
     */
	public static TClass searchTClass(String tClassName, Controller controller)
	{
		Map<String, TClassNode> opTTreeMap = controller.getOpTTreeMap();
            	Set<Map.Entry<String, TClassNode>> set = opTTreeMap.entrySet();
            	Iterator<Map.Entry<String, TClassNode>> iterator = set.iterator();
            	TClass tClassFounded = null;
            	while(iterator.hasNext()  && tClassFounded == null){
                	Map.Entry<String, TClassNode> mapEntry = iterator.next();
                	TClassNode opTTreeRoot = mapEntry.getValue();
			AbstractRepository<TClass> tClassLeaves = opTTreeRoot.acceptVisitor(new TClassLeavesFinder());
			AbstractIterator<TClass> tClassIt = tClassLeaves.createIterator();
			while(tClassIt.hasNext() && tClassFounded == null){
		                    TClass tClass = tClassIt.next();
                		    String auxTClassName = tClass.getSchName();
                    			if(auxTClassName.equals(tClassName))
                        			tClassFounded = tClass;
					}
            	}
		if(tClassFounded == null)
                	return null;
		return tClassFounded;
	}

    /**
     * Returns a repository with the leaves ...
     * @param controller A reference to the Controller
     * @return The repository
     */
	public static AbstractRepository<TClass> obtainTClasses(Controller controller)
	{
		AbstractRepository<TClass> leaves = new ConcreteRepository<TClass>();
		Map<String, TClassNode> opTTreeMap = controller.getOpTTreeMap();
		if(!opTTreeMap.isEmpty()){
            	Set<Map.Entry<String, TClassNode>> set = opTTreeMap.entrySet();
            	Iterator<Map.Entry<String, TClassNode>> iterator = set.iterator();
            	while(iterator.hasNext()){
                	Map.Entry<String, TClassNode> mapEntry = iterator.next();
                	TClassNode opTTreeRoot = mapEntry.getValue();
			AbstractRepository<TClass> tClassLeaves = opTTreeRoot.acceptVisitor(new TClassLeavesFinder());
			AbstractIterator<TClass> tClassIt = tClassLeaves.createIterator();
			while(tClassIt.hasNext()){
		                    TClass tClass = tClassIt.next();
				    leaves.addElement(tClass);
			}
            	}
		return leaves;
		}
		return null;
	}

    /**
     * Preprunes every test tree previously generated.
     * @param controller A reference to the Controller.
     * @return true if some test tree is pruned; false otherwise.
     */
        public static boolean prePrune(Controller controller){
                Map<String, TClassNode> opTTreeMap = controller.getOpTTreeMap();
		boolean isTTreePruned = false;
                if(!opTTreeMap.isEmpty()){
                    Set<Map.Entry<String, TClassNode>> set = opTTreeMap.entrySet();
                    Iterator<Map.Entry<String, TClassNode>> iterator = set.iterator();

                while(iterator.hasNext()){
                        Map.Entry<String, TClassNode> mapEntry = iterator.next();
                        TClassNode opTTreeRoot = mapEntry.getValue();
                        isTTreePruned = (opTTreeRoot.acceptVisitor(new PrePrunerVisitor())).booleanValue()
                                && isTTreePruned;
                    }
		}
            return isTTreePruned;
        }



    /**
     * Check if all lines in a predicate are in the predicate of a TClass
     * @param tClassName The name of the TClass
     * @param predicate The predicate in text format
     */
	public static boolean matchPredicates(TClass tClass, String predicate)
	{
		// We obtain the predicate of the TClass and convert it to String
		AxPara axPara = tClass.getMyAxPara();
		Pred tClassPred = SpecUtils.getAxParaPred(axPara);
		String originalPredicate = SpecUtils.termToLatex(tClassPred);
		//System.out.println("Predicado:\n"+originalPredicate);
		Pattern regex;
		Matcher matcher;

		// We check that all lines in predicate are in the predicate of tClass
		String lines[] = predicate.split("\n");
		boolean result = true;
		for(int i=0; i<lines.length && result;i++)
		{
			String line = lines[i].trim();
			String[] parts;
			// We split the line to obtain all the alternatives expressions that
			// result of the rewrite rules
			if(line.indexOf(";")>-1){
				boolean resultPart = false;
				parts = line.split(";");
				for(int j=0; j<parts.length && !resultPart;j++){

					parts[j] = RegExUtils.addEscapeCharacters(parts[j]);
					regex = Pattern.compile("^[ ]*"+parts[j],Pattern.MULTILINE);
					//System.out.println("La regular:\n"+regex);
					matcher = regex.matcher(originalPredicate);
					if(matcher.find()){
						resultPart = true;
						//System.out.println("Matcheo");
					}
					else{
						resultPart = false;
						//System.out.println("No matcheo");
					}
				}
				if(!resultPart){
					result = false;
					//System.out.println("Entra aca!");
				}
			}
			else{
			line = RegExUtils.addEscapeCharacters(line);
			regex = Pattern.compile("^[ ]*"+line,Pattern.MULTILINE);
			//System.out.println("La REGULAR:\n"+regex);
			matcher = regex.matcher(originalPredicate);
			if(matcher.find()){
				//System.out.println("FOund\n"+matcher.group());
			}
			else{
				result = false;
			}
			}
		}
		//System.out.println("El resultado: "+result);
		return result;
	}

    /**
     * Check if any of the formal parameters have the same name that any of the real
     * parameter and in that case change the name of the formal parameter for an
     * alternative one
     * @param formalParams The formal parameters
     * @param realParams The real parameters
     */
	private static boolean changeConflictiveNames(List<Variable> formalParams, List<String> realParams)
	{
		boolean areConflictive = false;
		for(int i=0;i<formalParams.size();i++){
			Variable auxVar = formalParams.get(i);
			String formalParam = auxVar.getName();
			boolean isPresent = false;
			for(int j=0;j<realParams.size() && !isPresent;j++){
				String realParam = realParams.get(j);
				if(formalParam.equals(realParam)){
					isPresent = true;
					areConflictive = true;
					formalParam = changeName(formalParam);
					Variable newVar = new Variable(formalParam, auxVar.getType());
					formalParams.set(i, newVar);
				}
			}
		}
		return areConflictive;
	}

    /**
     * Replace a string for an alternative one
     * @param name The string that will be replaced
     */
	private static String changeName(String name)
	{
		return name+"BiS";
	}

    /**
     * Replace all the occurrences of a string for another string in the scope of a third one
     * @param predicate The scope
     * @param originalTerm The string that will be replaced
     * @param newTerm The new string
     */
	public static String replaceAllOccurrences(String predicate, String originalTerm, String newTerm)
	{
		Pattern regex = RegExUtils.createVarRegEx(originalTerm);
		Matcher matcher = regex.matcher(predicate);
		while (matcher.find()){
			String pattern = matcher.group();
			// We check that the pattern is not part of a word
			if(!pattern.equals(originalTerm)){
				String replacedPattern = pattern.replaceAll(originalTerm, newTerm);
				replacedPattern = replacedPattern.replaceAll("\\\\","\\\\\\\\");
				Pattern newPattern;
				if(pattern.endsWith(originalTerm))
					newPattern = Pattern.compile(pattern+"$", Pattern.MULTILINE);
				else if(pattern.startsWith(originalTerm))
					newPattern = Pattern.compile("^"+pattern, Pattern.MULTILINE);
				else
					newPattern = Pattern.compile(pattern);
				predicate = newPattern.matcher(predicate).replaceFirst(replacedPattern);
				}
		}
		return predicate;
	}
}