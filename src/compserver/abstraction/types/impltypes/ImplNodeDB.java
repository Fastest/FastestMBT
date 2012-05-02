package compserver.abstraction.types.impltypes;

import java.util.List;
/**
 * Represents a node in the AST generated with the abstraction laws that contains
 * the information of type of a database
 */
public class ImplNodeDB implements ImplNode{

	public ImplNodeDB(){
	}
	public ImplNodeDB(String dbmsID, String connID, String tableName, List<ImplNodeDBColumn> columns){
                this.dbmsID=dbmsID;
        	this.connID=connID;
        	this.tableName=tableName;
                this.columns=columns;
        }
	public void setImplID(String id){
		this.id = id;
	}
	public String getImplID(){
		return id;
	}
	public void setDbmsID(String dbmsID){
		this.dbmsID = dbmsID;
	}
	public String getDbmsID(){
		return dbmsID;
	}
	public void setConnID(String connID){
		this.connID = connID;
	}
	public String getConnID(){
		return connID;
	}
	public void setTableName(String tableName){
		this.tableName = tableName;
	}
	public String getTableName(){
		return tableName;
	}
	public void setColumns(List<ImplNodeDBColumn> columns){
		this.columns = columns;
	}
	public List<ImplNodeDBColumn> getColumns(){
		return columns;
	}
	
	private String id;
	private String dbmsID;
	private String connID;
	private String tableName;
	private List<ImplNodeDBColumn> columns;
}