package compserver.abstraction.capture;

import java.util.*;

/**
 * Stores the captured value of a file variable in the UUT
 */
public class CapturedVarFile implements CapturedVar{

	public CapturedVarFile(){
		eol = "";
		eof = "";
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
	public void setFileName(String fileName){
		this.fileName = fileName;
	}
	public String getFileName(){
		return fileName;
	}
	public void setPath(String path){
		this.path = path;
	}
	public String getPath(){
		return path;
	}
	public void setDelimiter(String delimiter){
		this.delimiter = delimiter;
	}
	public String getDelimiter(){
		return delimiter;
	}
	public void setEol(String eol){
		this.eol = eol;
	}
	public String getEol(){
		return eol;
	}
	public void setEof(String eof){
		this.eof = eof;
	}
	public String getEof(){
		return eof;
	}
	public void setStructure(String structure){
		this.structure = structure;
	}
	public String getStructure(){
		return structure;
	}

	private String varName;
	private String fileName;
	private String path;
	private String delimiter;
	private String eof;
	private String eol;
	private String structure;
}