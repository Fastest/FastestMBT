package compserver.abstraction.types.impltypes;

import java.util.List;

/**
 * Represents a node in the AST generated with the abstraction laws that contains
 * the information of type of an structure
 */
public class ImplNodeStructure implements ImplNode{

	public ImplNodeStructure(){
	}
	public ImplNodeStructure(String name, List<ImplNodeField> fields){
		this.name = name;
		this.fields = fields;
	}
	public void setImplID(String id){
		this.id = id;
	}
	public String getImplID(){
		return id;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public void setFields(List<ImplNodeField> fields){
		this.fields = fields;
	}
	public List<ImplNodeField> getFields(){
		return fields;
	}

	private String id;
	private String name;
	private List<ImplNodeField> fields;
}
