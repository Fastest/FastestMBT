package compserver.prunning;

import java.util.*;
import common.repository.AbstractIterator;
import common.z.TClass;
import net.sourceforge.czt.z.ast.AxPara;
import common.z.SpecUtils;
import net.sourceforge.czt.z.ast.Pred;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 
import common.regex.RegExUtils;

/**
 * Obtains all the information of the expressions in a test class that match with the
 * resular expression of an elimination theorem 
 */
public class TheoremsChecker
{
    /**
     * Creates instaces of TheoremsChecker.
     * @param tClass the test class under analysis
     */
	public TheoremsChecker(TClass tClass)
	{
		TheoremsControl theoremsControl = TheoremsControl.getInstance();
		theoremsIt = theoremsControl.createIterator();
		this.tClass = tClass;
		AxPara axParaAux = tClass.getMyAxPara();
		Pred tClassPred = SpecUtils.getAxParaPred(axParaAux);
		strPred = SpecUtils.termToLatex(tClassPred);
		String[] parts = strPred.split("\n");
		String auxPred = "";
		for(int i=0;i<parts.length;i++){
			if(parts[i].endsWith(" \\\\"))
				parts[i] = parts[i].substring(0, parts[i].length() -3)+"\n";
			auxPred+=parts[i];
		}
		strPred = auxPred;
		originalPred = auxPred;
		currentTheorem = "";
	}
    /**
     * Returns a map with the theorem name which regular expression match with a test class's
     * predicate as key and the asignation to the parametes of the theorem as value
     * @param tClass the test class under analysis
     */
	public Map<String, List<Map<String,String>>> findParameters(){

		if(theoremsIt.hasNext()){
		Theorem theTheorem = theoremsIt.next();
		String theoremName = theTheorem.getName();

		debug = false;
		/*if(tClass.getSchName().equals("DetectReferenceEvent_NR_179") && theoremName.equals("Nuevo")){
			debug = true;
			System.out.println("Debuggeando!");
		}*/

		info = new HashMap<String,List<Map<String,String>>>();


		List<List<List<String>>> reservedWords = theTheorem.getReservedWords();
		boolean reservedsInPredicate = true; 
		// We check if the necessary operators are present in the predicate
		for(int x1=0;x1<reservedWords.size() && reservedsInPredicate;x1++){
			List<List<String>> alterna = reservedWords.get(x1);
			boolean someAlternative = false;
			for(int x2=0;x2<alterna.size() && !someAlternative;x2++){
				List<String> subList = alterna.get(x2);
				boolean thisAlternative = true;
				for(int x3=0;x3<subList.size() && thisAlternative;x3++){
					if(!strPred.contains(subList.get(x3)))
					thisAlternative = false;
				}
				if(thisAlternative)
					someAlternative = true;
			}
			if(!someAlternative)
				reservedsInPredicate = false;
		}
		/*if(debug)
			System.out.println("Llega hasta aca!");*/

		// If the operators are present we do the analysis
		if(reservedsInPredicate){
			Map<String,String> mapFR = new HashMap<String,String>();
			List<Map<String,String>> matches = new ArrayList<Map<String,String>>();
			matches = analyzePatterns(theTheorem, mapFR, matches, 0);
			if(matches==null || matches.size()==0)
				return findParameters();
			else{
				info.put(theoremName, matches);
				return info;
			}
		}
		else
			return findParameters();
		}
		else
			return new HashMap<String,List<Map<String,String>>>();
	}

	private List<Map<String,String>> analyzePatterns(Theorem theTheorem, Map<String,String> mapFR, List<Map<String,String>> matches, int startIndex)
	{
		if(currentTheorem!=theTheorem.getName()){
			strPred = originalPred;
			currentTheorem = theTheorem.getName();
		}
		String[] partsPredicate = theTheorem.getPredicatesToMatch().split("\n");
		//strPred = originalPred;
		List<List<Pattern>> patterns = theTheorem.getRegEx();
		List<Map<Integer,String>> mapGroups = theTheorem.getVarRegExGroups();
		List<Pattern> auxPatterns = patterns.get(startIndex);
		Matcher matcher = auxPatterns.get(0).matcher(strPred);

		int groupCount = 0;


		Map<Integer,String> groups = mapGroups.get(startIndex);
		boolean match = false;
		boolean nonContradiction = false;
		for(int i=0;i<auxPatterns.size();i++){
		Pattern auxPattern = auxPatterns.get(i);
		/*if(debug){
		System.out.println("El patron es:\n"+auxPattern);
		System.out.println("El pred:\n"+strPred);
		}*/
		matcher = matcher.reset(strPred);
		matcher = matcher.usePattern(auxPattern);
		while(matcher.find()){
			/*if(debug){
			System.out.println("Loopea el nivel: "+startIndex);
			System.out.println("Regular expresion:\n"+auxPattern);
			System.out.println("Matcheo el grupo:\n"+matcher.group());
			}*/
			boolean contradiction = false;
			Map<String,String> mapFRCopy = new HashMap<String,String>();
			mapFRCopy.putAll(mapFR);
			match = true;
			/*if(debug)
				System.out.println("Grupos: "+matcher.groupCount());*/
			for(int j=1;j<matcher.groupCount()+1 && !contradiction;j++){
			if(matcher.group(j)!=null && matcher.group(j)!=""){
				/*if(debug)
					System.out.println("Grupo: "+(j+groupCount));*/
				String captured = matcher.group(j).trim();
				String formal = groups.get(j+groupCount).trim();
				if(captured.endsWith(" "))
					captured = captured.substring(0, captured.length()-1);
				else if(captured.endsWith(" ="))
					captured = captured.substring(0, captured.length()-2);
				/*if(debug){
				System.out.println("Formal:"+formal);
				System.out.println("Real:"+captured);
				}*/

				if(captured.contains(",") /*&& !captured.contains(")") && !captured.contains("(")*/ && partsPredicate[startIndex].contains("\\se( "+formal+" )")){
					//System.out.println("SubExtension:\n"+ partsPredicate[startIndex]);
					String auxNewVar = captured.substring(captured.lastIndexOf(",")+1).trim();
					//System.out.println("La nueva variable: "+auxNewVar);
					//System.out.println("Antes de reemplazar:\n"+strPred);
					strPred = strPred.replaceFirst(RegExUtils.addEscapeCharacters(captured), captured.substring(0, captured.lastIndexOf(",")-1));
					//String auxStrPred = matcher.toString().replaceFirst(RegExUtils.addEscapeCharacters(captured), captured.substring(0, captured.lastIndexOf(",")-1));

					//System.out.println("Despues de reemplazar:\n"+strPred);
					captured = auxNewVar;
					matcher.reset(strPred);
				}

				// If the formal parameter is present in the map we compared the captureds values
				if(mapFRCopy.containsKey(formal)){
					if(!mapFRCopy.get(formal).matches(RegExUtils.addEscapeCharacters("([(][ ])*"+captured+"([ ][)])*")) && !captured.matches(RegExUtils.addEscapeCharacters("([(][ ])*"+mapFRCopy.get(formal)+"([ ][)])*"))){
						contradiction = true;
					}
				}
				else{
				// If the formal parameter is not present in the map and the captured value is not present in the map we add this entry to the map
					if(!mapFRCopy.containsValue(captured))
						mapFRCopy.put(formal,captured);
					else{
				// If the formal parameter is not present in the map and the captured value is present in the map we check if the corresponding formal parameter is a constant or a variable. The constants must be differents
						List<Variable> vars = theTheorem.getFormalParamList();
						boolean repeat = false;
						for(int k=0;k<vars.size() && !repeat;k++){
						Variable var = vars.get(k);
						String name = var.getName();
						String type = var.getType();
						if(name.equals(formal) && type.startsWith("\\const")){
						Set<String> keySet = mapFRCopy.keySet();
						List<String> keyList = new ArrayList<String>();
						keyList.addAll(keySet);
						for(int x=0;x<keyList.size() && !repeat;x++){
						String auxFormal = keyList.get(x);
						String auxCaptured = mapFRCopy.get(auxFormal);
						if(auxCaptured.equals(captured)){
						for(int y=0;y<vars.size() && !repeat;y++){
						Variable auxVar = vars.get(y);
						String auxName = auxVar.getName();
						String auxType = auxVar.getType();
						if(auxName.equals(auxFormal) && auxType.startsWith("\\const")){
						repeat = true;
						}

						}

						}
						}
						}
						}
						if(repeat)
							contradiction = true;
						else
							mapFRCopy.put(formal,captured);
					}
				}
			}
			}
			if(!contradiction){
				nonContradiction = true;
				if(startIndex+1<patterns.size()){
					List<Map<String,String>> auxList = analyzePatterns(theTheorem, mapFRCopy, matches, startIndex+1);
					if(auxList!=null)
						matches = auxList;
				}
				else{
					matches.add(mapFRCopy);
				}
			}
		}
		groupCount+=matcher.groupCount();
		}
		if(!match || !nonContradiction)
			return null;

		return matches;
	}

	

	private AbstractIterator<Theorem> theoremsIt;
	private TClass tClass;
	private String strPred;
	private String originalPred;
	private String currentTheorem;
	private Map<String,List<Map<String,String>>> info;
	private boolean debug;
}