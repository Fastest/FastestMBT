package compserver.abstraction;

import java.util.*;
import compserver.abstraction.types.impltypes.ImplNode;
import compserver.abstraction.types.spectypes.SpecNode;
/**
 * Stores the information needed for compose more than one variables of the implementation
 * in one variable of the specification
 */
public class VarComposition{

	public VarComposition(){
	}

/**
* This constructor is used when the composition is done with a preOperator
*/
	public VarComposition(String specID, String preOperator, String implID){
		this.specID = specID;
		this.preOperator = preOperator;
		this.implID = implID;
	}

	public void setSpecID(String specID){
		this.specID = specID;
	}
	public void setPreOperator(String preOperator){
		this.preOperator = preOperator;
	}
	public void setImplID(String implID){
		this.implID = implID;
	}
	public String getImplID(){
		return implID;
	}
	public String getSpecID(){
		return specID;
	}
	public String getPreOperator(){
		return preOperator;
	}
	private String specID;
	private String implID;
	private String preOperator;
}
