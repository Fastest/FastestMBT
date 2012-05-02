package compserver.abstraction.capture;

import java.util.*;

/**
 * Stores the captured value of a field of a list or of a structure
 */
public class CapturedField{

	public CapturedField(){
	}

	public CapturedField(String fieldName, CapturedVar capturedVar){
		this.fieldName = fieldName;
		this.capturedVar = capturedVar;
	}
    /**
     * Sets the name of the field
     * @param varName
     */
	public void setFieldName(String fieldName){
		this.fieldName = fieldName;
	}
    /**
     * Gets the name of the field
     * @return fieldName
     */
	public String getFieldName(){
		return fieldName;
	}
    /**
     * Sets the value of the field
     * @param capturedVar
     */
	public void setCapturedVar(CapturedVar capturedVar){
		this.capturedVar = capturedVar;
	}
    /**
     * Gets the value of the field
     * @return capturedVar
     */
	public CapturedVar getCapturedVar(){
		return capturedVar;
	}

	private String fieldName;
	private CapturedVar capturedVar;
}