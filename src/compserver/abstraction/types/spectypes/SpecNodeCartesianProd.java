package compserver.abstraction.types.spectypes;

import java.util.*;

/**
 * Represents a node in the AST generated with the abstraction laws that contains
 * the information of type of a cartesian product
 */
public class SpecNodeCartesianProd implements SpecNode{

	public SpecNodeCartesianProd(){
	}
	public SpecNodeCartesianProd(List<SpecNode> types){
		this.types = types;
	}
	public void setSpecID(String id){
		this.id = id;
	}
	public String getSpecID(){
		return id;
	}
	public List<SpecNode> getTypes(){
		return types;
	}
	public void setType(List<SpecNode> types){
		this.types = types;
	}
	private String id;
	private List<SpecNode> types;
}
