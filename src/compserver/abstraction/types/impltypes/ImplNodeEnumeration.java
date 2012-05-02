package compserver.abstraction.types.impltypes;

import java.util.*;

/**
 * Represents a node in the AST generated with the abstraction laws that contains
 * the information of type of an enumeration
 */
public class ImplNodeEnumeration implements ImplNode{

	public ImplNodeEnumeration(){
	}
	public ImplNodeEnumeration(ImplNode typeEnum, Map<String, String> elements){
		this.typeEnum = typeEnum;
		this.elements = elements;
	}

	public ImplNode getEnumType(){
		return typeEnum;
	}
	public Map<String, String> getElements()
	{
		return elements;
	}

	public String getConstant(String element)
	{
		return elements.get(element);
	}
	public void setImplID(String id){
		this.id = id;
	}
	public String getImplID(){
		return id;
	}

	private ImplNode typeEnum;
	private Map<String,String> elements;
	private String id;
}
