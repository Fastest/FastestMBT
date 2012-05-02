package compserver.prunning.rewriting.rwrules;

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
import common.repository.AbstractIterator;
import net.sourceforge.czt.session.*; 
import net.sourceforge.czt.z.ast.DeclList; 
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.ast.VarDecl;
import common.z.SpecUtils;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.ZFactory;
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

/**
 * Instances of this class have the functionality of parsing the file that contains
 * the rewrite rules and load those theorems as instances of RWRuleImpl into
 * the unique instance of RWRulesControl in the system.
 */
public class RWRulesLoader {
    
    private String rwRulesFileName;
    /**
     * Creates instaces of RWRulesLoader.
     * @param fileName the file where the rewrite rules are defined.
     */
    public RWRulesLoader(String rwRulesFileName){
        this.rwRulesFileName = rwRulesFileName;
    }

    /**
     * Load the rewrite rules from the file into the unique instance of
     * RWRulesControl.
     */
	public void loadRWRules(){
	System.out.println("Loading pruning rewrite rules...");
	RWRulesControl rwRulesControl = RWRulesControl.getInstance();
	
	try{
		// We load the rewrite rules
    		BufferedReader in = new BufferedReader(new FileReader(rwRulesFileName));
            	String text = "";
		String line;
		while((line = in.readLine())!= null)
                	text += line+"\n";

		// We extract the rules's definitions  from the file.
		String rulesArr[] = text.split("end");

            	ZLive zLive = UniqueZLive.getInstance();
            	TextUI textUI = new TextUI(zLive, new PrintWriter(System.out, true));
            	PrintWriter printWriter = new PrintWriter(System.out, true);
            
            	for(int i=0; i< rulesArr.length-1; i++){
                String ruleDefinition = rulesArr[i];
                RWRuleLawImpl rwRuleLaw = new RWRuleLawImpl();

               	// We separate the i-th rule definition  in lines
               	String ruleLines[] = ruleDefinition.split("\r\n|\r|\n");

		String auxDefinition="";
		String regex="";
		boolean firstTime = true;
		String ruleName = "";
		ZDeclList zDeclList = null;
                for(int j=0; j< ruleLines.length-1; j++)
		{       
		    if(!ruleLines[j].startsWith("%") && !ruleLines[j].equals("") && !ruleLines[j].equals("{rwRule}")){
			if(ruleLines[j].endsWith("\\\\"))
			{
				//System.out.println("Termina con \\\\");
				ruleLines[j] = ruleLines[j].substring(0,ruleLines[j].length()-3);
			}
			if(firstTime){ 
				String ruleHeader = ruleLines[j].trim();
				ruleName = extractRuleName(ruleHeader);
				zDeclList = extractRuleDeclList(ruleHeader);
				firstTime = false;
			}
			else{
		    	auxDefinition+=ruleLines[j]+"\n";
			regex+=replaceNonReservedWords(ruleLines[j])+"|";
			}
		}
                }
		regex = regex.substring(0,regex.length()-1);
		regex = regex.replaceAll("\\\\","\\\\\\\\");
		regex = regex.replaceAll("\\{\\D","\\\\\\{ ");
		regex = regex.replaceAll("\\?","\\\\?");
		regex = regex.replaceAll("\\+","\\\\+");

		rwRuleLaw.setDefinition(auxDefinition);
                rwRuleLaw.setName(ruleName);
		rwRuleLaw.setZDeclList(zDeclList);
		rwRuleLaw.setRegEx(regex);
                rwRulesControl.addElement(rwRuleLaw);
		}
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
            e.printStackTrace();
        }
	}

	private String extractRuleName(String line)
	{
		if(line.startsWith("\\begin{rwRule}{"))
			line = line.substring(15);
		else
			System.out.println("There was an error while read the theorem file. The expression  "+line+" has syntax errors");
		line = line.substring(0,line.indexOf('}'));
		return line;	
	}

	private ZDeclList extractRuleDeclList(String line)
	{
		ZLive zLive = UniqueZLive.getInstance();
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
					//System.out.println(name);
					declarations.add(name+": "+"CONST"+type);
				}
				else
					declarations.add(name+": "+type);
			}
		}
		int random = (int)(Math.random()*1000000);
		String def = "\\begin{schema}{NAME"+random+"}[";	
		for(int i=0;i<nonReserved.size()-1;i++){
			def=def+nonReserved.get(i)+", ";
		}
		if(nonReserved.size()>0)
			def=def+nonReserved.get(nonReserved.size()-1)+"]";
		else
			def=def+"X]";
		for(int i=0;i<declarations.size()-1;i++){
			def=def+declarations.get(i)+" \\\\ ";
		}
		def=def+declarations.get(declarations.size()-1)+" \\end{schema}";

		Term parsedTerm = null;
		try{
		parsedTerm = ParseUtils.parse(new StringSource(def), zLive.getSectionManager());
		}
		catch(Exception e){
			System.out.println("Exception");
			e.printStackTrace();
			System.out.println(def);
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
		//System.out.println("El AxPara solo:\n"+SpecUtils.termToLatex(axPara));
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

	private String replaceNonReservedWords(String line)
	{
/*		String[] parts = line.split(" ");
		String replacedLine = "(^";
		for(int i=0;i<parts.length;i++){
			if(!parts[i].startsWith("\\") && !parts[i].startsWith("="))
				replacedLine+= "(.*) ";
			else
				replacedLine+= parts[i]+" ";
			if(((int)parts[i])>=0 && ((int)parts[i])<=9)
				System.out.prin
		}
		replacedLine=replacedLine.trim();
		replacedLine+=")";
		return replacedLine;*/
		String[] parts = line.split(" ");
		String replacedLine = "(^";
		for(int i=0;i<parts.length;i++){
			char firstChar = parts[i].charAt(0);
			if(Character.isLetter(firstChar))
				replacedLine+= "(.*) ";
			else
				replacedLine+= parts[i]+" ";
		}
		replacedLine=replacedLine.trim();
		replacedLine+=")";
		return replacedLine;
	}	


}

// Deberia lanzar excepciones para cuando hay un problema de carga!!!