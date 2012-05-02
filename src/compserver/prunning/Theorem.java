package compserver.prunning;

import java.util.*;
import compserver.prunning.operators.SpecialLine;
import net.sourceforge.czt.z.ast.DeclList;
import net.sourceforge.czt.z.ast.ZDeclList;
import java.util.regex.Pattern; 

/**
 * Instances of this class represents theorems for prune empty classes.
 */
public class Theorem{

    List<Variable> formalParamList;
    String definition;
    String name;
    List<List<Pattern>> regex;
    String predicates;
    List<SpecialLine> specialLines;
    ZDeclList zDeclList;
    List<List<List<String>>> reserved;
    List<Map<Integer,String>> mapGroups;

	public Theorem()
	{
		specialLines = new ArrayList<SpecialLine>();
		//reserved = new ArrayList<String>();
	}

    /**
     * Sets the list of formal parameters of this theorem.
     * @param formalParamList
     */
    public void setFormalParamList(List<Variable> formalParamList){
        this.formalParamList = formalParamList;
    }

    /**
     * Gets the list of formal parameters of this theorem.
     * @return
     */
    public List<Variable> getFormalParamList(){
        return formalParamList;     
    }

    /**
     * Sets the definition of this theorem.
     * @param definition
     */
    public void setDefinition(String definition){
        this.definition = definition;
    }

    /**
     * Gets the definition of this theorem.
     * @return
     */
    public String getDefinition(){
        return definition;
    }

    /**
     * Sets the name of this theorem.
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }
    

    /**
     * Gets the name of this theorem.
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the list of regular expressions asociated to this theorem
     * @param regex
     */
    public void setRegEx(List<List<Pattern>> regex){
        this.regex = regex;
    }
    

    /**
     * Returns the list of regular expressions asociated to this theorem
     * @return
     */
    public List<List<Pattern>> getRegEx(){
        return regex;
    }

    /**
     * Sets the positions of the formal parameters in the asociated regular expression
     * @param regex
     */
    public void setVarRegExGroups(List<Map<Integer,String>> mapGroups){
        this.mapGroups = mapGroups;
    }

    /**
     * Returns the positions of the formal parameters in the asociated regular expression
     * @return
     */
    public List<Map<Integer,String>> getVarRegExGroups(){
        return mapGroups;
    }

    /**
     * Sets the predicates that have to match.
     * @param definition
     */
    public void setPredicatesToMatch(String predicates){
        this.predicates = predicates;
    }

    /**
     * Gets the predicates that have to match.
     * @return
     */
    public String getPredicatesToMatch(){
        return predicates;
    }

    /**
     * Sets the reserved words in the predicate
     * @param reserved
     */
    public void setReservedWords(List<List<List<String>>> reserved){
        this.reserved = reserved;
    }

    /**
     * Gets the reserved words in the predicate
     * @return
     */
    public List<List<List<String>>> getReservedWords(){
        return reserved;
    }


        /**
     * Sets the list of predicates that contains special operators.
     */
	public void setSpecialLine(String operatorName, String line, String theoremName)
	{
		SpecialLine sLine = new SpecialLine(operatorName, line, theoremName);
		specialLines.add(sLine);
	}
        /**
     * Gets the list of predicates that contains special operators.
     */
	public List<SpecialLine> getSpecialLines()
	{
		return specialLines;
	}
        /**
     * Sets the list of declarations of variables
     */
	public void setZDeclList(ZDeclList zDeclList)
	{
		this.zDeclList = zDeclList;
	}
        /**
     * Gets the list of declarations of variables
     */
	public DeclList getZDeclList()
	{
		return zDeclList;
	}
}