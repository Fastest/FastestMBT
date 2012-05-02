package compserver.abstraction.types.impltypes;

/**
 * Represents a node in the AST generated with the abstraction laws that contains
 * the information of type of a field
 */
public class ImplNodeField implements ImplNode{

	public ImplNodeField(){
	}
	public ImplNodeField(String name, ImplNode type){
		this.type = type;
		this.name = name;
	}
	public void setImplID(String id){
		this.id = id;
	}
	public String getImplID(){
		return id;
	}
	public void setType(ImplNode type){
		this.type = type;
	}
	public ImplNode getType(){
		return type;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	
	private ImplNode type;
	private String id;
	private String name;
}
