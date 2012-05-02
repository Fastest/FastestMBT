package compserver.abstraction;

import java.util.*;
//import compserver.abstraction.types.impltypes.ImplNode;
//import compserver.abstraction.types.spectypes.SpecNode;
/**
 * Abstract an abstraction law
 */
public class AbstractionLaw{

	public AbstractionLaw(){
	}
	public AbstractionLaw(String lawName, List<AbstractionRule> absRules){
		this.lawName = lawName;
		this.absRules = absRules;
	}
	public void setLawName(String lawName){
		this.lawName = lawName;
	}
	public String getLawName(){
		return lawName;
	}
	public List<AbstractionRule> getAbstractionRules(){
		return absRules;
	}
	public void setAbstractionRules(List<AbstractionRule> absRules){
		this.absRules = absRules;
	}
	private String lawName;
	private List<AbstractionRule> absRules;
}
