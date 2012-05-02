package compserver.abstraction.types.impltypes;

/**
 * Represents a node in the AST generated with the abstraction laws that contains
 * the information of type of a pointer
 */
public class ImplNodePointer implements ImplNode{

	public ImplNodePointer(){
	}
	public ImplNodePointer(ImplNode type){
		this.type = type;
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
	private ImplNode type;
	private String id;
}
