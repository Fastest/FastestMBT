package compserver.abstraction.types.spectypes;

/**
 * Represents a node in the AST generated with the abstraction laws that contains
 * the information of type of a power set
 */
public class SpecNodePowerSet implements SpecNode{

	public SpecNodePowerSet(){
	}
	public SpecNodePowerSet(SpecNode type){
		this.type = type;
	}
	public void setSpecID(String id){
		this.id = id;
	}
	public String getSpecID(){
		return id;
	}
	public SpecNode getType(){
		return type;
	}
	public void setType(SpecNode type){
		this.type = type;
	}
	private String id;
	private SpecNode type;
}
