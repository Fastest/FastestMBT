package compserver.abstraction.types.spectypes;

/**
 * Represents a node in the AST generated with the abstraction laws that contains
 * the information of type of a sequence
 */
public class SpecNodeSeq implements SpecNode{

	public SpecNodeSeq(){
	}
	public SpecNodeSeq(SpecNode type){
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
	public void setType(SpecNode yype){
		this.type = type;
	}
	private String id;
	private SpecNode type;
}
