package compserver.abstraction.types.impltypes;

/**
 * Represents a node in the AST generated with the abstraction laws that contains
 * the information of type of an array
 */
public class ImplNodeArray implements ImplNode{

	public ImplNodeArray(){
	}
	public ImplNodeArray(ImplNode type, int size, String lastPosPointerID){
		this.type = type;
		this.size = size;
		this.lastPosPointerID = lastPosPointerID;
	}
	public ImplNodeArray(ImplNode type, int size){
		this.type = type;
		this.size = size;
		this.lastPosPointerID = null;
	}
	public void setImplID(String id){
		this.id = id;
	}
	public String getImplID(){
		return id;
	}
	public void setLastPosPointerID(String lastPosPointerID){
		this.lastPosPointerID = lastPosPointerID;
	}
	public String getLastPosPointerID(){
		return lastPosPointerID;
	}
	public void setType(ImplNode type){
		this.type = type;
	}
	public ImplNode getType(){
		return type;
	}
	public void setSize(int size){
		this.size = size;
	}
	public int getSize(){
		return size;
	}
	
	private ImplNode type;
	private String id;
	private int size;
	private String lastPosPointerID;
}
