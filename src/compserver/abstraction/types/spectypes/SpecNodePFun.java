package compserver.abstraction.types.spectypes;

/**
 * Represents a node in the AST generated with the abstraction laws that contains
 * the information of type of a partial function
 */
public class SpecNodePFun implements SpecNodeBinRelation{

	public SpecNodePFun(){
	}
	public SpecNodePFun(SpecNode domType, SpecNode ranType){
		this.domType = domType;
		this.ranType = ranType;
	}
	public void setSpecID(String id){
		this.id = id;
	}
	public String getSpecID(){
		return id;
	}
	public SpecNode getDomType(){
		return domType;
	}
	public void setDomType(SpecNode domType){
		this.domType = domType;
	}
	public SpecNode getRanType(){
		return ranType;
	}
	public void setRanType(SpecNode ranType){
		this.ranType = ranType;
	}
	private String id;
	private SpecNode domType;
	private SpecNode ranType;
}
