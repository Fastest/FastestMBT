package compserver.abstraction.types.impltypes;

/**
 * Represents a node in the AST generated with the abstraction laws that contains
 * the information of the screen
 */
public interface ImplNodeScreen extends ImplNode{

    /**
     * Sets the identifier of the variable in the implementation
     * @param id
     */
	public void setImplID(String id);
    /**
     * Gets the identifier of the variable in the implementation
     * @return id
     */
	public String getImplID();
    /**
     * Sets the type of the Screen
     * The possibilities are regex, plane and table
     * @param id
     */
	public void setScreenType(String screenType);
    /**
     * Gets the type of the screen
     * @return id
     */
	public String getScreenType();
    /**
     * Sets the path in which will be redirected the output
     * @param path
     */
	public void setScreenPath(String path);
    /**
     * Gets the path in which was redirected the output
     * @return path
     */
	public String getScreenPath();
    /**
     * Sets the enumeration associated to this node
     * @param enumeration
     */
	public void setEnumeration(ImplNodeEnumeration enumeration);
    /**
     * Gets the enumeration associated to this node
     * @return enumeration
     */
	public ImplNodeEnumeration getEnumeration();
}
