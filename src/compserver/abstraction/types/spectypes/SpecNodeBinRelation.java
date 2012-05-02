package compserver.abstraction.types.spectypes;

/**
 * Interface that represents a node in the AST generated with the abstraction laws
 * that contains information of type of variables in the specification 
 */
public interface SpecNodeBinRelation extends SpecNode{
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
    /**
     * Sets the type of the domain
     * @param domType
     */
	public void setDomType(SpecNode domType);
    /**
     * Gets the type of the domain
     * @return type
     */
	public SpecNode getDomType();
    /**
     * Sets the type of the range
     * @param ranType
     */
	public void setRanType(SpecNode ranType);
    /**
     * Gets the type of the range
     * @return type
     */
	public SpecNode getRanType();
}
