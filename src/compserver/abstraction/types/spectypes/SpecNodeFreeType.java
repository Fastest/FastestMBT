package compserver.abstraction.types.spectypes;

/**
 * Represents a node in the AST generated with the abstraction laws that contains
 * the information of type of a free type
 */
public class SpecNodeFreeType implements SpecNode{

	public SpecNodeFreeType(){
	}
	public SpecNodeFreeType(String typeName){
		this.typeName = typeName;
	}
	public void setSpecID(String id){
		this.id = id;
	}
	public String getSpecID(){
		return id;
	}
	public String getTypeName(){
		return typeName;
	}
	public void setType(String typeName){
		this.typeName = typeName;
	}
	private String id;
	private String typeName;
}
