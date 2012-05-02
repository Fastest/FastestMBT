package compserver.abstraction.types.impltypes;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a node in the AST generated with the abstraction laws that contains
 * the information of type of a list
 */
public class ImplNodeList implements ImplNode{

	public ImplNodeList(){
	}
	public ImplNodeList(String name, String linkType, String linkNextName, String linkPrevName, List<ImplNodeField> fields, String memalloc)
	{
		this.name = name;
		this.linkType = linkType;
		this.linkNextName = linkNextName;
		this.linkPrevName = linkPrevName;
		this.fields = fields;
		this.memalloc = memalloc;
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
	public void setLinkType(String linkType){
		this.linkType = linkType;
	}	
	public String getLinkType(){
		return linkType;
	}
	public void setLinkNextName(String linkNextName){
		this.linkNextName = linkNextName;
	}
	public String getLinkNextName(){
		return linkNextName;
	}
	public void setLinkPrevName(String linkPrevName){
		this.linkPrevName = linkPrevName;
	}
	public String getLinkPrevName(){
		return linkPrevName;
	}
	public void setFields(List<ImplNodeField> fields){
		this.fields = fields;
	}
	public List<ImplNodeField> getFields(){
		return fields;
	}
	public void setMemalloc(String memalloc){
		this.memalloc = memalloc;
	}
	public String getMemalloc(){
		return memalloc;
	}
	
	private String id;
	private String name;
	private String linkType;
	private String linkNextName;
	private String linkPrevName;
	private String memalloc;
	private List<ImplNodeField> fields;
}
