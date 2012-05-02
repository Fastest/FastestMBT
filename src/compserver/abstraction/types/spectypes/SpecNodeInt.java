package compserver.abstraction.types.spectypes;

/**
 * Represents a node in the AST generated with the abstraction laws that contains
 * the information of type of an integer
 */
public class SpecNodeInt implements SpecNode{

	public SpecNodeInt(){
	}
	public void setSpecID(String id){
		this.id = id;
	}
	public String getSpecID(){
		return id;
	}
	private String id;
}
