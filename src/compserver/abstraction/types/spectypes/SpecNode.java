package compserver.abstraction.types.spectypes;

/**
 * Interface that represents a node in the AST generated with the abstraction laws
 * that contains information of type of variables in the specification 
 */
public interface SpecNode{
    /**
     * Sets the identifier of the variable in the specification
     * @param id
     */
	public void setSpecID(String id);
    /**
     * Gets the identifier of the variable in the specification
     * @return id
     */
	public String getSpecID();
}
