package compserver.abstraction;

import java.util.*;
import compserver.abstraction.types.impltypes.ImplNode;
import compserver.abstraction.types.spectypes.SpecNode;
/**
 * Abstract an abstraction rule
 */
public class AbstractionRule{

	public AbstractionRule(){
	}
	public AbstractionRule(String ruleName, List<ImplNode> implNodes, List<SpecNode> specNodes, String captureCode, List<VarComposition> compositions){
		this.ruleName = ruleName;
		this.implNodes = implNodes;
		this.specNodes = specNodes;
		this.captureCode = captureCode;
		this.compositions = compositions;
	}
	public void setRuleName(String ruleName){
		this.ruleName = ruleName;
	}
	public String getRuleName(){
		return ruleName;
	}
	public void setCaptureCode(String captureCode){
		this.captureCode = captureCode;
	}
	public String getCaptureCode(){
		return captureCode;
	}
	public List<SpecNode> getSpecNodes(){
		return specNodes;
	}
	public void setSpecNodes(List<SpecNode> specNodes){
		this.specNodes = specNodes;
	}
	public List<ImplNode> getImplNodes(){
		return implNodes;
	}
	public void setImplNodes(List<ImplNode> implNodes){
		this.implNodes = implNodes;
	}
	public List<VarComposition> getCompositions(){
		return compositions;
	}
	public void setCompositions(List<VarComposition> compositions){
		this.compositions = compositions;
	}
	private String ruleName;
	private String captureCode;
	private List<SpecNode> specNodes;
	private List<ImplNode> implNodes;
	private List<VarComposition> compositions;
}
