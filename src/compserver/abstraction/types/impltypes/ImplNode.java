package compserver.abstraction.types.impltypes;

/**
 * Interface that represents a node in the AST generated with the abstraction laws
 * that contains information of type of variables in the implementation
 */
public interface ImplNode{
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
}
