package compserver.abstraction.types.impltypes;

/**
 * Represents a node in the AST generated with the abstraction laws that contains
 * the information of screen without a particular configuration
 */
public class ImplNodeScreenPlane implements ImplNodeScreen{

	public ImplNodeScreenPlane(){
		this.screenType = "plane";
	}
	public void setImplID(String id){
		this.id = id;
	}
	public String getImplID(){
		return id;
	}
	public void setScreenType(String screenType){
		this.screenType = screenType;
	}
	public String getScreenType(){
		return screenType;
	}
	public void setScreenPath(String path){
		this.path = path;
	}
	public String getScreenPath(){
		return path;
	}
	public void setEnumeration(ImplNodeEnumeration enumeration){
		this.enumeration = enumeration;
	}
	public ImplNodeEnumeration getEnumeration(){
		return enumeration;
	}
	private String screenType;
	private String id;
	private String path;
	private ImplNodeEnumeration enumeration;
}