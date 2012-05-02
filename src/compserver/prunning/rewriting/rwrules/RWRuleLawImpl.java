package compserver.prunning.rewriting.rwrules; 

import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.DeclList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.VarDecl;
import common.z.SpecUtils;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.Name;
import net.sourceforge.czt.z.ast.Decl;
import common.z.czt.UniqueZLive;
import net.sourceforge.czt.animation.eval.ZLive;
import net.sourceforge.czt.session.*;
import common.z.czt.visitors.DeleteParenAnn;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.parser.z.ParseUtils;
import net.sourceforge.czt.z.ast.Pred;


/**
 * An implementation of the interface RWRuleLaw
 */
public class RWRuleLawImpl implements RWRuleLaw{

	public RWRuleLawImpl()
	{
		regex = "";
		name = "";
		definition = "";
		zDeclList = null;
		listTypes = new ArrayList<Expr>();
		listStrExpr = new ArrayList<String>();
		mapFR = new HashMap<String, String>();
	}
	public String getRegEx()
	{
		return regex;
	}
	public void setRegEx(String regex)
	{
		this.regex = regex;
	}
	public void setZDeclList(ZDeclList zDeclList)
	{
		this.zDeclList = zDeclList;
	}
	public DeclList getZDeclList()
	{
		return zDeclList;
	}
	public void setDefinition(String definition){
		this.definition = definition;
	}
	public String getDefinition(){
		return definition;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public boolean match(String predicate)
	{
		Pattern regexPattern = Pattern.compile(regex);
		Matcher matcher = regexPattern.matcher(predicate);

		if(matcher.matches()){
		boolean founded = false;
		for(int i=1;i<=matcher.groupCount() && !founded;i++){
			if(matcher.group(i)!=null){
			founded = true;
			String defArr[] = definition.split("\r\n|\r|\n");
			List<String> formalNames = new ArrayList<String>();
			boolean founded2 = false;
			for(int j=0; j< defArr.length && !founded2; j++){
			Matcher auxMatcher = regexPattern.matcher(defArr[j]);
			if(auxMatcher.matches()){
				if(auxMatcher.group(i)!=null){
				founded2 = true;
				i+=1;
				listTypes.clear();
				listStrExpr.clear();
				//mapFR.clear();
				while(matcher.group(i)!=null){
				listStrExpr.add(matcher.group(i));
				formalNames.add(auxMatcher.group(i));
				mapFR.put(auxMatcher.group(i),matcher.group(i));
				boolean varFounded = false;
				for(int k=0;k<zDeclList.size() && !varFounded;k++){
					Decl decl = zDeclList.get(k);
					if(decl instanceof VarDecl){
						VarDecl varDecl = (VarDecl) decl;
						Name name = varDecl.getZNameList().get(0);
						if(name instanceof ZName){
						ZName zName = (ZName) name;
						String formalName = zName.getWord().toString();
						if(formalName.equals(auxMatcher.group(i))){
							varFounded = true;
							Expr expr = varDecl.getExpr();
							listTypes.add(expr);
						}
						}
					}
				}
				i+=1;
				}
				}
			}
			}
			}
		}
		return true;
		}
		return false;
	}
	public String rewrite(String originalExpr){
		String newPred = definition;
		for(int k=0;k<zDeclList.size();k++){
			Decl decl = zDeclList.get(k);
			if(decl instanceof VarDecl){
				VarDecl varDecl = (VarDecl) decl;
				Name name = varDecl.getZNameList().get(0);
				if(name instanceof ZName){
					ZName zName = (ZName) name;
					String formalName = zName.getWord().toString();
					if(mapFR.get(formalName)!=null){
						String realPar = mapFR.get(formalName);

						realPar = "("+realPar+")";
						realPar = realPar.replaceAll("\\\\","\\\\\\\\");
						realPar = realPar.replaceAll("\\{\\D","\\\\\\{ ");
						realPar = realPar.replaceAll("\\?","\\\\?");
						realPar = realPar.replaceAll("\\+","\\\\+");
						realPar = realPar.replaceAll("\\(","\\\\(");
						realPar = realPar.replaceAll("\\)","\\\\)");

     newPred = Pattern.compile("^"+formalName+" ",Pattern.MULTILINE).matcher(newPred).replaceAll(realPar+" ");
     newPred = Pattern.compile(" "+formalName+" ",Pattern.MULTILINE).matcher(newPred).replaceAll(" "+realPar+" ");
     newPred = Pattern.compile(" "+formalName+"$",Pattern.MULTILINE).matcher(newPred).replaceAll(" "+realPar);
					}
					/*else{
     newPred = Pattern.compile("^"+formalName+" ",Pattern.MULTILINE).matcher(newPred).replaceAll("(.*) ");
     newPred = Pattern.compile(" "+formalName+" ",Pattern.MULTILINE).matcher(newPred).replaceAll(" (.*) ");
     newPred = Pattern.compile(" "+formalName+"$",Pattern.MULTILINE).matcher(newPred).replaceAll(" (.*)");

					}*/
				}
			}
		}
		String newPredLines[] = newPred.split("\r\n|\r|\n");
		String auxPred = "";
		for(int i=0;i<newPredLines.length-1;i++){
			auxPred+= newPredLines[i]+";";
		}
		auxPred+=newPredLines[newPredLines.length-1];
		newPred = auxPred;
		return newPred;
	}

	public String addRegExValues(String originalExpr)
	{
		String newPred = originalExpr.replaceAll(";","\n");

		for(int k=0;k<zDeclList.size();k++){
			Decl decl = zDeclList.get(k);
			if(decl instanceof VarDecl){
				VarDecl varDecl = (VarDecl) decl;
				Name name = varDecl.getZNameList().get(0);
				if(name instanceof ZName){
					ZName zName = (ZName) name;
					String formalName = zName.getWord().toString();
					if(mapFR.get(formalName)==null){

			newPred = Pattern.compile("^"+formalName+" ",Pattern.MULTILINE).matcher(newPred).replaceAll("(.*) ");
			newPred = Pattern.compile(" "+formalName+" ",Pattern.MULTILINE).matcher(newPred).replaceAll(" (.*) ");
			newPred = Pattern.compile(" "+formalName+"$",Pattern.MULTILINE).matcher(newPred).replaceAll(" (.*)");
			}
			}
			}
		}
		newPred = newPred.replaceAll("\n",";");
		//System.out.println("La reemplazada:\n"+newPred);
		return newPred;
	}

	public List<String> getStrExpr()
	{
		return listStrExpr;
	}
	
	public List<Expr> getTypes()
	{
		return listTypes;
	}


	private String regex;
	private String name;
	private String definition;
	private ZDeclList zDeclList;
	private List<Expr> listTypes;
	private List<String> listStrExpr;
	private Map<String,String> mapFR;
} 