package compserver.abstraction.types.impltypes;

/**
 * Represents a node in the AST generated with the abstraction laws that contains
 * the information of type of a 
 */
public class ImplNodePLType implements ImplNode{

	public ImplNodePLType(){
	}
	public ImplNodePLType(String type){
		this.type = type;
	}
	public void setImplID(String id){
		this.id = id;
	}
	public String getImplID(){
		return id;
	}
	public void setType(String type){
		this.type = type;
	}
	public String getType(){
		return type;
	}
	
	private String type;
	private String id;
}
