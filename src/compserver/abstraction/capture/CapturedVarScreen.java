package compserver.abstraction.capture;

import java.util.*;

/**
 * Stores the captured value of the Screen
 */
public class CapturedVarScreen implements CapturedVar{

	public CapturedVarScreen(){
	}
    /**
     * Sets the name of the variable
     * @param varName
     */
	public void setVarName(String varName){
		this.varName = varName;
	}
    /**
     * Gets the name of the variable
     * @return varName
     */
	public String getVarName(){
		return varName;
	}
    /**
     * Sets the type of the screen
     * @param varName
     */
	public void setScreenType(String screenType){
		this.screenType = screenType;
	}
    /**
     * Gets the type of the screen
     * @return value
     */
	public String getScreenType(){
		return screenType;
	}
    /**
     * Sets the delimiter
     * @param delimiter
     */
	public void setDelimiter(String delimiter){
		this.delimiter = delimiter;
	}
    /**
     * Gets the delimiter
     * @return delimiter
     */
	public String getDelimiter(){
		return delimiter;
	}
	public void setRowLowerBound(int rowLB){
		this.rowLB = rowLB;
	}
	public int getRowLowerBound(){
		return rowLB;
	}
	public void setRowUpperBound(int rowUB){
		this.rowUB = rowUB;
	}
	public int getRowUpperBound(){
		return rowUB;
	}
	public void setColumnLowerBound(int colLB){
		this.colLB = colLB;
	}
	public int getColumnLowerBound(){
		return colLB;
	}
	public void setColumnUpperBound(int colUB){
		this.colUB = colUB;
	}
	public int getColumnUpperBound(){
		return colUB;
	}
	public void setScreenPath(String path){
		this.path = path;
	}
	public String getScreenPath(){
		return path;
	}
	public void setEnumElements(Map<String,String> enumeration){
		this.enumeration = enumeration;
	}
	public Map<String,String> getEnumElements(){
		return enumeration;
	}
	private String screenType;
	private String delimiter;
	private int rowLB;
	private int rowUB;
	private int colLB;
	private int colUB;
	private String varName;
	private String path;
	private Map<String,String> enumeration;
}