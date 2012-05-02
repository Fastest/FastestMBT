package compserver.prunning;

import java.io.*;
import java.util.*;

import net.sourceforge.czt.session.StringSource;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.parser.z.ParseUtils;
import net.sourceforge.czt.animation.eval.TextUI;
import net.sourceforge.czt.animation.eval.ZLive;

import common.z.czt.UniqueZLive;
import compserver.prunning.rewriting.rwrules.RWRulesControl;
import compserver.prunning.rewriting.rwrules.RWRule;
import compserver.prunning.rewriting.rwrules.RWRuleLaw;
import compserver.prunning.rewriting.rwrules.RWRuleOperator;
import common.repository.AbstractIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 
import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.DeclList; // PRUEBA
import net.sourceforge.czt.base.ast.Term; // PRUEBA
import net.sourceforge.czt.z.ast.VarDecl; // PRUEBA
import common.z.SpecUtils; // PRUEBA
import net.sourceforge.czt.z.ast.AxPara; // PRUEBA
import net.sourceforge.czt.z.ast.ZFactory; // PRUEBA
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.ExprList;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.ast.ZStrokeList;
import net.sourceforge.czt.z.impl.ZExprListImpl;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.impl.ZNameListImpl;
import net.sourceforge.czt.z.ast.ZNameList;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.PowerExpr;
import common.z.czt.visitors.CZTReplacer;
import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.Sect;
import net.sourceforge.czt.z.ast.ZSect;
import net.sourceforge.czt.z.ast.Para;
import net.sourceforge.czt.z.ast.ParaList;
import net.sourceforge.czt.z.ast.ZParaList;
import net.sourceforge.czt.z.ast.Expr;
import common.z.UtilSymbols;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.Decl;
import compserver.prunning.typechecking.SubTyping;
import net.sourceforge.czt.z.ast.Name;
import net.sourceforge.czt.typecheck.z.TypeCheckUtils;
import net.sourceforge.czt.typecheck.z.ErrorAnn;
import common.z.czt.visitors.DeleteParenAnn;
import common.regex.RegExUtils;
import common.util.MathUtils;
import compserver.prunning.operators.*;

/**
 * Instances of this class have the functionality of parsing the file that contains
 * the theorems' definitions and load those theorems as instances of Theorem into
 * the unique instance of TheoremsControl in the system.
 * Also, load the list of Fastest's operators used in theorems.
 */
public class TheoremsLoader {
    
    private String theoremsFileName;
    private String operatorsFileName;
    private String defTheorem;
    private ZLive zLive;
    private int maxCard;
    private List<String> operatorsList;

    
    /**
     * Creates instaces of TheoremsLoader.
     * @param theoremsFileName the file where the theorems are defined.
     * @param operatorsFileName the file where the Fastest's operators are defined.
     */
    public TheoremsLoader(String theoremsFileName, String operatorsFileName){
        this.operatorsFileName = operatorsFileName;
	this.theoremsFileName = theoremsFileName;
	maxCard = 0;
	operatorsList = new ArrayList<String>();
    }
    
    
    
    /**
     * Load the theorems from the file into the unique instance of
     * TheoremsControl.
     */
	public void loadTheorems(){

        TheoremsControl theoremsControl = TheoremsControl.getInstance();
	
	try{
		System.out.println("Loading pruning theorems...");
		// We load the operators
    		BufferedReader inOp = new BufferedReader(new FileReader(operatorsFileName));
		String line;
            	String ops = "";
		while((line = inOp.readLine())!= null)
                	operatorsList.add(line);
		// We obtain the rewrite rules for easy manipulation
		RWRulesControl rwRulesControl = RWRulesControl.getInstance();
		List<RWRuleOperator> rulesOperator = new ArrayList<RWRuleOperator>();
		List<RWRuleLaw> rulesLaw = new ArrayList<RWRuleLaw>();
		AbstractIterator<RWRule> itRules = rwRulesControl.createIterator();
		while(itRules.hasNext()){
			RWRule rwRule = itRules.next();
			if(rwRule instanceof RWRuleOperator)
				rulesOperator.add((RWRuleOperator) rwRule);
			else if(rwRule instanceof RWRuleLaw)
				rulesLaw.add((RWRuleLaw) rwRule);
		}

		// We load the theorems
    		BufferedReader in = new BufferedReader(new FileReader(theoremsFileName));
            	String text = "";
		while((line = in.readLine())!= null)
                	text += line+"\n";

		// We split the string to separate the theorems's definitions
		String theoremsArr[] = text.split("end");
            
            
            	zLive = UniqueZLive.getInstance();
            	TextUI textUI = new TextUI(zLive, new PrintWriter(System.out, true));
            	PrintWriter printWriter = new PrintWriter(System.out, true);
            	for(int i=0; i< theoremsArr.length-1; i++){
                String thmDefinition = theoremsArr[i];
                Theorem theorem = new Theorem();

               	// We separate the i-th theorem definition  in lines
               	String theoremLines[] = thmDefinition.split("\r\n|\r|\n");

		String auxDefinition="";
		String auxPredicate="";
		String finalString="";
		//String regExpr ="";
		boolean firstTime = true;
		String theoremName = "";
		List<Variable> formalParamList = null;
		//Map<String, List<Integer>> ids = new HashMap<String,List<Integer>>();
		boolean isATheorem = false;
		ZDeclList zDeclList = null;
                for(int j=0; j< theoremLines.length-1; j++)
		{
		    if(!theoremLines[j].startsWith("%") && !theoremLines[j].equals("") && !theoremLines[j].equals("{theorem}")){
			if(theoremLines[j].endsWith("\\\\"))
			{
				theoremLines[j] = theoremLines[j].substring(0,theoremLines[j].length()-3);
			}
			// We extract all the information in the header
			if(firstTime){ 
				String theoremHeader = theoremLines[j].trim();
				theoremName = extractTheoremName(theoremHeader);
				//System.out.println("Teorema: "+theoremName);
				formalParamList = extractTheoremParams(theoremHeader);
				if(formalParamList.size()>maxCard)
					maxCard = formalParamList.size();
				zDeclList = extractTheoremDeclList(theoremHeader);
				isATheorem = true;
				firstTime = false;
			}
			else{
		    	auxDefinition+=theoremLines[j]+"\n";

			boolean lawFounded = false;
			boolean specialLine = false;
			int x=0;
			for(int k=0; k<operatorsList.size() && !specialLine; k++)
				if(theoremLines[j].contains(operatorsList.get(k)))
					specialLine =true;
			if(!specialLine){	
			// We apply the rewrite rules
			for(x=0;x<rulesLaw.size() && !lawFounded;x++){
				if(rulesLaw.get(x).match(theoremLines[j])){
					List<String> strNames = rulesLaw.get(x).getStrExpr();
					List<Expr> types = rulesLaw.get(x).getTypes();
					boolean isCorrect = true;
					for(int index=0;index<types.size() && isCorrect; index++){
					String strName = strNames.get(index);
					Expr type = types.get(index);
					isCorrect =isCorrectType(strName, type, zDeclList);
					}
					if(isCorrect){
					theoremLines[j] = rulesLaw.get(x).rewrite(theoremLines[j]);
					lawFounded = true;
					}
					
				}
			}
			if(lawFounded){
				theoremLines[j] = deleteExtraParenthesis(theoremLines[j]);
			}
			else
				theoremLines[j] = deleteExtraParenthesis(theoremLines[j]);
			}
			// We apply the commutativity rewrite rules
			for(int k=0;k<rulesOperator.size();k++)
				if(theoremLines[j].indexOf(rulesOperator.get(k).getOperator())>-1 && !theoremLines[j].contains("\\eval")){
					// Reemplazamos los operadores Fastest por nombres intermedios para poder parsear
					theoremLines[j] = FastestOperatorReplacer.replace(theoremLines[j]);
					theoremLines[j] = rulesOperator.get(k).rewrite(theoremLines[j]);
					// Reestablecemos los operadores Fastest
					theoremLines[j] = FastestOperatorReplacer.recover(theoremLines[j]);
			}

			if(lawFounded){
			theoremLines[j] = rulesLaw.get(x-1).addRegExValues(theoremLines[j]);
			}
			// We check if the line contains the \eval operator
			boolean special = false;
			//for(int k=0; k<operatorsList.size() && !special; k++)
				if(theoremLines[j].indexOf("\\eval")>-1)
				{
					special =true;
					theorem.setSpecialLine("\\eval", theoremLines[j], theoremName);
				}
				/*else if(theoremLines[j].indexOf("\\se")>-1)
				{
					special =true;
					theorem.setSpecialLine("\\se", theoremLines[j], theoremName);
				}*/
			if(!special)
				auxPredicate+=theoremLines[j]+"\n";

			finalString+= theoremLines[j]+"\n";
			}
		}
                }
		if(isATheorem){
		List<List<List<String>>> reservedWords = extractReservedWords(finalString);

		List<Map<Integer,String>> mapGroups = new ArrayList<Map<Integer,String>>();
		List<List<Pattern>> patterns = createRegExpr(auxPredicate, formalParamList, mapGroups);

		theorem.setVarRegExGroups(mapGroups);
		theorem.setReservedWords(reservedWords);
		theorem.setDefinition(auxDefinition);
		theorem.setPredicatesToMatch(auxPredicate);
                theorem.setName(theoremName);
                theorem.setFormalParamList(formalParamList);
		theorem.setZDeclList(zDeclList);
		theorem.setRegEx(patterns);
                theoremsControl.addElement(theorem);
		}
            }

		// We set the max cardinality of the loaded theorems
		theoremsControl.setMaxCardinality(maxCard);

		/*String regular = "^[ ]*(.+) \\notin \\{.*? (.+) .*\\}[ ]*";
		regular = RegExUtils.addEscapeCharacters(regular);
		String frase = "x \\notin \\{ y1 , y2 , y3 \\}";
		System.out.println("La expresion regular:\n"+regular);
		System.out.println("La frase:\n"+frase);
		Pattern regex = Pattern.compile(regular,Pattern.MULTILINE);
		Matcher matcher = regex.matcher(frase);
		while(matcher.find()){
			System.out.println("Matcheo!!!");
			for(int i=0;i<matcher.groupCount()+1;i++)
				System.out.println("El grupo: "+matcher.group(i));
			if(matcher.group(2).contains(",")){
			frase = frase.replaceAll(matcher.group(2), matcher.group(2).substring(0, matcher.group(2).lastIndexOf(",")-1));
			System.out.println("La frase ahora:\n"+frase);
			matcher = regex.matcher(frase);
			}
		}*/

		}
		catch(FileNotFoundException e){
			System.out.println("The theorems' configuration file " +
                    "could not be found.");
			System.exit(0);
		}	
		catch(IOException e){
			System.out.println("IOException");
			System.exit(0);
        
        }
        catch(Exception e){
	    System.out.println("Exception!!!");
            e.printStackTrace(System.out);
        }
	}

	private String extractTheoremName(String line)
	{
		if(line.startsWith("\\begin{theorem}{"))
			line = line.substring(16);
		else
			System.out.println("There was an error while read the theorem file. The expression  "+line+" has syntax errors");
		line = line.substring(0,line.indexOf('}'));
		return line;	
	}
	private List<Variable> extractTheoremParams(String line)
	{
		List<Variable> params = new ArrayList<Variable>();
		line = line.substring(line.lastIndexOf('{')+1,line.lastIndexOf('}'));
		String types[] = line.split(";");
		String auxType;
		for(int i=0;i<types.length;i++)
		{
			auxType = types[i];
			String parts[] = auxType.split(":",2);
			String varNames[] = parts[0].split(",");
			String type = parts[1].trim();
			for(int j=0;j<varNames.length;j++)
			{
				String name = varNames[j].trim();
				Variable var;
				if(name.startsWith("\\const")){
					name = name.substring(7);
					var = new Variable(name, "\\const "+type);
				}
				else
					var = new Variable(name, type);
				params.add(var);
			}
		}	
		return params;
	}
	private ZDeclList extractTheoremDeclList(String line)
	{
		List<String> nonReserved = new ArrayList<String>();
		List<String> declarations = new ArrayList<String>();
		line = line.substring(line.lastIndexOf('{')+1,line.lastIndexOf('}'));
		String types[] = line.split(";");
		String auxType;
		for(int i=0;i<types.length;i++)
		{
			auxType = types[i];
			String parts[] = auxType.split(":",2);
			String varNames[] = parts[0].split(",");
			String type = parts[1].trim();
			extractNonReservedWords(type, nonReserved);
			for(int j=0;j<varNames.length;j++)
			{
				String name = varNames[j].trim();
				if(name.startsWith("\\const")){
					name = name.substring(7);
					declarations.add(name+": "+"CONST"+type);
				}
				else
					declarations.add(name+": "+type);
			}
		}
		int random = (int)(Math.random()*1000000);
		defTheorem = "\\begin{schema}{NAME"+random+"}[";	
		for(int i=0;i<nonReserved.size()-1;i++){
			defTheorem=defTheorem+nonReserved.get(i)+", ";
		}
		if(nonReserved.size()>0)
			defTheorem=defTheorem+nonReserved.get(nonReserved.size()-1)+"]";
		else
			defTheorem=defTheorem+"X]";
		for(int i=0;i<declarations.size()-1;i++){
			defTheorem=defTheorem+declarations.get(i)+" \\\\ ";
		}
		defTheorem=defTheorem+declarations.get(declarations.size()-1)+" \\end{schema}";

		Term parsedTerm = null;
		try{
		parsedTerm = ParseUtils.parse(new StringSource(defTheorem), zLive.getSectionManager());
		}
		catch(Exception e){
			System.out.println("Exception");
			e.printStackTrace();
			System.out.println(defTheorem);
		}
		//System.out.println("Parseado:\n"+SpecUtils.termToLatex((Spec)parsedTerm));
		Spec spec = (Spec) parsedTerm;
		AxPara axPara = null;
		for (Sect sect : spec.getSect()) {
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
		DeclList declList = SpecUtils.getAxParaListOfDecl(axPara);
		ZDeclList zDeclList = null;
		if(declList instanceof ZDeclList)
			zDeclList = (ZDeclList) declList;
		return zDeclList;
	}

	private void extractNonReservedWords(String type, List<String> nonReserved)
	{
		String[] parts = type.split(" ");
		for(int i=0;i<parts.length;i++)
			if(!parts[i].startsWith("\\") && !nonReserved.contains(parts[i]))
				nonReserved.add(parts[i]);
	}	

	private Map<String,List<Integer>> getOrder(List<Variable> vars, String pred, int count)
	{
		//System.out.println("Pred original:\n"+pred);
		Map<String,List<Integer>> mapPos = new HashMap<String,List<Integer>>();
		String strRegex = "";
		for(int j=0;j<vars.size();j++){
			List<Integer> list = new ArrayList<Integer>();
			String name = vars.get(j).getName();
			mapPos.put(name, list);
			strRegex+= name+"|";
		}
		strRegex = strRegex.substring(0,strRegex.length()-1);
		Pattern regex = RegExUtils.createVarRegEx(strRegex);
		Matcher matcher = regex.matcher(pred);
		while(matcher.find()){
			String pattern = matcher.group();
			//System.out.println("Expresion rara:\n"+pattern);
			boolean found = false;
			for(int j=0;j<vars.size() && !found;j++){
				String name = vars.get(j).getName();
				if(pattern.contains(name)){
					found = true;
					List<Integer> auxList = mapPos.get(name);
					auxList.add(++count);
				}
			}
		}
		return mapPos;
	}	

	private List<List<List<String>>> extractReservedWords(String predicate)
	{
		//List<String> reservado = new ArrayList<String>();
		//Set<String> reserved = new HashSet<String>();
		List<List<List<String>>> reservedsList = new ArrayList<List<List<String>>>();
		String[] lines = predicate.split("\r\n|\r|\n");
		for(int i=0; i<lines.length;i++){
			String line = lines[i].trim();
			if(!line.contains("\\eval")){

			List<List<String>> alternatives = new ArrayList<List<String>>();

			String[] subLine = line.split(";");
			for(int x=0;x<subLine.length;x++){
			List<String> subList = new ArrayList<String>(); 
			Set<String> reservedSet = new HashSet<String>();
			String[] parts = subLine[x].split(" ");
			for(int j=0;j<parts.length;j++)
				// We analyze expressions that starts with a "\"
				if(parts[j].startsWith("\\")){
				// We check if the string is a Fastest's operator
				boolean special = false;
				for(int k=0; k<operatorsList.size() && !special; k++)
					if(parts[j].contains(operatorsList.get(k)))
						special =true;
				// We check if the string is the empty set
				boolean empty = false;
				if(parts[j].equals("\\{")){
					if(parts[j+1].equals("\\}")){
					empty = true;
					reservedSet.add(parts[j]+" "+parts[j+1]);
					j++;
					}
				} // We check if the string is the empty sequence
				else if(parts[j].equals("\\langle")){
					if(parts[j+1].equals("\\rangle")){
					empty = true;
					reservedSet.add(parts[j]+" "+parts[j+1]);
					j++;
					}
				}
				else if(!special && !parts[j].equals("\\}") && !parts[j].equals("\\rangle"))
					reservedSet.add(parts[j]);
				}
				else if(parts[j].matches("\\+|\\-|\\*|=|>|<"))
					reservedSet.add(parts[j]);

				subList.addAll(reservedSet);
				alternatives.add(subList);
			}
			reservedsList.add(alternatives);
			}
		}
		return reservedsList;
	}

	private List<List<Pattern>> createRegExpr(String originalPred, List<Variable> vars, List<Map<Integer,String>> mapGroups)
	{
		//List<Pattern> patterns = new ArrayList<Pattern>();
		List<List<Pattern>> patterns = new ArrayList<List<Pattern>>();
		String[] predLines = originalPred.split("\n");
		List<String> lines = new ArrayList<String>();
		for(int i=0;i<predLines.length;i++)
			lines.add(predLines[i]);

		for(int i=0;i<lines.size();i++){
			Map<Integer,String> groups = new HashMap<Integer,String>();
			String newPred = lines.get(i);
			//System.out.println("Predicado original:\n"+newPred);
			for(int j=0;j<vars.size();j++){
			String name = vars.get(j).getName();
			Pattern regex = RegExUtils.createVarRegEx(name);
			Matcher matcher = regex.matcher(newPred);
			while(matcher.find()){
				String pattern = matcher.group();
				String newPattern = pattern.replaceAll(name, "(.+)");
				newPred = newPred.replaceAll(pattern, newPattern);
			}
			}

			for(int x=0; x<operatorsList.size(); x++)
				if(newPred.indexOf(operatorsList.get(x))>-1){
					String operator = operatorsList.get(x);
					operator = operator.substring(1,2).toUpperCase()+operator.substring(2);
					try{
					Class operatorClass = Class.forName("compserver.prunning.operators." + operator + "Operator");
					Object object = operatorClass.newInstance();
					if (object instanceof Operator){
					Operator op = (Operator) object;
					//System.out.println("newPred antes: "+newPred);
					newPred = op.addSemantic(newPred);
					//System.out.println("newPred despues: "+newPred);
					}

					}
					catch(Exception e){
						System.out.println("Exception in ops");
					}
				}

			newPred = RegExUtils.addEscapeCharacters(newPred);
			//System.out.println("Despues newPred:\n"+newPred);
			String[] parts = newPred.split(";");
			String auxPred = "";
			for(int x=0;x<parts.length-1;x++){
				auxPred+="^[ ]*"+parts[x]+"[ ]*$|";
			}
			auxPred+="^[ ]*"+parts[parts.length-1]+"[ ]*$";
			if(auxPred.contains("(.*)~(.*)")){
				auxPred = auxPred.replace("(.*)~(.*)","([^ (]+.*)~(.*[^ )]+)");

			}
			else if(auxPred.contains("(.+)~(.+)")){
				//auxPred = auxPred.replace("(.+)~(.+)","([^ ]+)~([^ ]+)");
				auxPred = auxPred.replace("(.+)~(.+)","([^ ]+\t(?:[(][^()]*[)])\t(?:[(](?:[^ ]+\t(?:[(][^()]*[)]))[)]))~([^ ]+\t(?:[(][^()]*[)])\t(?:[(](?:[^ ]+\t(?:[(][^()]*[)]))[)]))");
				//"([^ ]+|(?:[(][^()]*[)])|(?:[(](?:[^ ]+|(?:[(][^()]*[)]))[)]))"

				//auxPred = auxPred.replace("(.+)~(.+)","([^ ]+)~([^ ]+)");
			}
			else if(auxPred.contains("(.+)~.*")){
				auxPred = auxPred.replace("(.+)~.*","([^ ]+\t(?:[(][^()]*[)])\t(?:[(](?:[^ ]+\t(?:[(][^()]*[)]))[)]))~(?:[^ ]+\t(?:[(][^()]*[)])\t(?:[(](?:[^ ]+\t(?:[(][^()]*[)]))[)]))");
			}
			//System.out.println("El auxPred:\n"+auxPred);
			String[] auxPatterns = auxPred.split("\\|");
			//System.out.println("El tamaño es: "+auxPatterns.length);
			//Pattern regex = Pattern.compile(auxPred,Pattern.MULTILINE);
			//System.out.println("PAtron:\n"+regex);
			String aux = lines.get(i);
			//aux = aux.replaceAll("\\\\sw\\( (.*) \\)", "$1");
			//aux = aux.replaceAll("\\\\se\\( (.*) \\)", "\\\\{ $1 \\\\}");
			String[] partsAux = aux.split(";");
			int groupCount=0;
			List<Pattern> singlePattern = new ArrayList<Pattern>();
			for(int j=0;j<partsAux.length;j++){


			partsAux[j] = partsAux[j].replaceAll("\\\\sw\\( (.*) \\)", "$1");
			partsAux[j] = partsAux[j].replaceAll("\\\\se\\( (.*) \\)", "\\\\{ $1 \\\\}");



				auxPatterns[j] = auxPatterns[j].replace("\t","|");
				//System.out.println("El patron:\n"+auxPatterns[j]);
				Pattern auxRegex = Pattern.compile(auxPatterns[j],Pattern.MULTILINE);
				Matcher auxMatcher = auxRegex.matcher(partsAux[j]);
				while(auxMatcher.find()){
				for(int x=1;x<auxMatcher.groupCount()+1;x++){
					if(auxMatcher.group(x)!=null && auxMatcher.group(x)!=""){
						//System.out.println("Grupo "+(x+groupCount)+": "+auxMatcher.group(x));
						groups.put(new Integer(x+groupCount), auxMatcher.group(x));
					}
				}
				}
				groupCount+=auxMatcher.groupCount();
				singlePattern.add(auxRegex);
			}

			mapGroups.add(groups);
			patterns.add(singlePattern);
		}
		return patterns;
	}


	private String extractVarName(String expr)
	{
		String name = "";
		boolean founded = false;
		String[] parts = expr.split(" ");
		for(int i=0;i<parts.length;i++){
			char firstChar = parts[i].charAt(0);
			if(Character.isLetter(firstChar))
				if(!founded){
					name = parts[i];
					founded = true;
				}
				else
					return null;
		}
		if(!founded)
			return null;
		else
			return name;
	}	

	private String deleteExtraParenthesis(String originalExpr)
	{
		String newExpr="";
		String parts[] = originalExpr.split(";");
		String definition = defTheorem;
		definition = definition.substring(0, definition.length()-13);
		definition += "\n\\where \n";
		for(int i=0;i<parts.length-1;i++)
			definition+= parts[i]+" \\\\";
		definition += parts[parts.length -1]+"\n\\end{schema}";
		Term parsedTerm = null;
		try{
		parsedTerm = ParseUtils.parse(new StringSource(definition), zLive.getSectionManager());
		}
		catch(Exception e){
			System.out.println("Excepcion");
			e.printStackTrace();
			System.out.println(defTheorem);
		}

		Spec spec = (Spec) parsedTerm;
		AxPara axPara = null;
		for (Sect sect : spec.getSect()) {
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

		try{
		ZLive zLive = UniqueZLive.getInstance();
		List<? extends ErrorAnn> errors = 
				TypeCheckUtils.typecheck(axPara, zLive.getSectionManager(), false, zLive.getCurrentSection());
		if(errors.size() >0)
			System.out.println("Errores:\n"+errors.toString());
		}
		catch(Exception e){
			System.out.println("Typechecking errors!!!");
		}


		Pred pred = SpecUtils.getAxParaPred(axPara);
		//String prevStrPred = SpecUtils.termToLatex(pred);
		pred = (Pred) pred.accept(new DeleteParenAnn());
		String strPred = SpecUtils.termToLatex(pred);

		String partsPred[] = strPred.split("\\\\\\\\\n");
		for(int i=0;i<partsPred.length -1;i++)
			newExpr += partsPred[i].trim()+";";
		newExpr += partsPred[partsPred.length-1];
		return newExpr;
	}



	private boolean isCorrectType(String strName, Expr typeRule, ZDeclList zDeclList)
	{
		if(typeRule instanceof PowerExpr)
			return true;
		if(typeRule instanceof RefExpr){
		RefExpr refRule = (RefExpr) typeRule;
		if(!refRule.getMixfix() && !refRule.getExplicit())
			return true;
		else if(refRule.getMixfix() && refRule.getExplicit()){
		String ruleTypeSymbol = refRule.getZName().getWord().toString();
		boolean varFounded = false;
		for(int i=0;i<zDeclList.size() && !varFounded;i++){
			Decl decl = zDeclList.get(i);
			if(decl instanceof VarDecl){
				VarDecl varDecl = (VarDecl) decl;
				Name name = varDecl.getZNameList().get(0);
				if(name instanceof ZName){
				ZName zName = (ZName) name;
				String formalName = zName.getWord().toString();
				if(formalName.equals(strName)){
					varFounded = true;
					Expr expr = varDecl.getExpr();
					if(expr instanceof RefExpr){
						RefExpr refExpr = (RefExpr) expr;
						String theoremTypeSymbol = refExpr.getZName().getWord().toString();
						if(SubTyping.isSubType(ruleTypeSymbol, theoremTypeSymbol))
							return true;
						else
							return false;
					}
				}
				}
			}
		}
		}
		}
		return true;
	}

}