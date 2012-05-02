package client.blogic.testing.refinement;

import java.util.*;

/**
 * Stores the information related to the key of ConstantStore's elements
 */
public class ConstantStoreKey{

	public ConstantStoreKey(){
	}

	public ConstantStoreKey(String implID, String constant){
		this.implID = implID;
		this.constant = constant;
	}
    /**
     * Sets the identifier in the implementation of the variable related to this key
     * @param implID
     */
	public void setImplID(String implID){
		this.implID = implID;
	}
    /**
     * Gets the identifier in the implementation of the variable related to this key
     * @return implID
     */
	public String getImplID(){
		return implID;
	}
    /**
     * Sets the value associated to this key
     * @param constant
     */
	public void setConstant(String constant){
		this.constant = constant;
	}
    /**
     * Gets the value associated to this key
     * @return constant
     */
	public String getConstant(){
		return constant;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ConstantStoreKey){
			ConstantStoreKey key = (ConstantStoreKey) obj;
			if(key.getImplID().equals(this.implID) && key.getConstant().equals(this.constant))
				return true;
			else
				return false;
		}
		else
			return false;
	}

	@Override
	public int hashCode() {
		return implID.hashCode() + constant.hashCode();
	}

	private String implID;
	private String constant;
}