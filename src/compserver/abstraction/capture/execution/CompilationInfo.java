package compserver.abstraction.capture.execution;

import java.util.*;


/**
 * Instances of this class encapsulates the information of compilation
 */
public class CompilationInfo{
	public CompilationInfo(String targetLanguaje,String platform, String compiler,String compileOptions, String workingDirectory){
		this.targetLanguaje = targetLanguaje;
		this.platform = platform;
		this.compiler = compiler;
		this.compileOptions = compileOptions;
		this.workingDirectory = workingDirectory;
	}
	/**
	* Gets the languaje in which is encoded the UUT
	* @return
	*/
	public String getTargetLanguaje(){
		return targetLanguaje;     
	}
	/**
	* Gets the platform in which run the UUT
	* @return
	*/
	public String getPlatform(){
		return platform;     
	}
	/**
	* Gets the compiler
	* @return
	*/
	public String getCompiler(){
		return compiler;     
	}
	/**
	* Gets the option that must be set in the compiler
	* @return
	*/
	public String getCompileOptions(){
		return compileOptions;     
	}
	/**
	* Gets the directory in which is stored the UUT
	* @return
	*/
	public String getWorkingDirectory(){
		return workingDirectory;
	}
	private String targetLanguaje;
	private String platform;
	private String compiler;
	private String compileOptions;
	private String workingDirectory;
}