package compserver.abstraction.types.impltypes;

/**
 * Represents a node in the AST generated with the abstraction laws that contains
 * the information of type of a field
 */
public class ImplNodeDBColumn implements ImplNode{

	public ImplNodeDBColumn(){
	}
	public ImplNodeDBColumn(String columnName, String columnType){
		this.columnName = columnName;
		this.columnType = columnType;
	}
	public void setImplID(String id){
		this.id = id;
	}
	public String getImplID(){
		return id;
	}
	public void setColumnName(String columnName){
		this.columnName = columnName;
	}
	public String getColumnName(){
		return columnName;
	}
	public void setColumnType(String columnType){
		this.columnType = columnType;
	}
	public String getColumnType(){
		return columnType;
	}
	
	private String columnType;
	private String id;
	private String columnName;
}
