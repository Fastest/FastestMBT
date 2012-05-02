package compserver.abstraction.types.spectypes;

/**
 * Represents a node in the AST generated with the abstraction laws that contains
 * the information of type of a natural number
 */
public class SpecNodeNat implements SpecNode{

	public SpecNodeNat(){
	}
	public void setSpecID(String id){
		this.id = id;
	}
	public String getSpecID(){
		return id;
	}
	private String id;
}
