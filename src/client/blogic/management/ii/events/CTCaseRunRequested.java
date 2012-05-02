package client.blogic.management.ii.events;

//import client.blogic.testing.refinement.ConcreteTCase;
import compserver.abstraction.capture.execution.CompilationInfo;
//import compserver.abstraction.AbstractionLaw;
//import net.sourceforge.czt.z.ast.AxPara;

public class CTCaseRunRequested extends Event_{

	//public CTCaseRunRequested(ConcreteTCase cTCase, CompilationInfo compilationInfo, String runCode, String workingDirectory, AbstractionLaw absLaw, AxPara originalAxPara){
	public CTCaseRunRequested(String runCode, CompilationInfo compilationInfo){
		//this.cTCase = cTCase;
		this.compilationInfo = compilationInfo;
		this.runCode = runCode;
		//this.workingDirectory = workingDirectory;
		//this.absLaw = absLaw;
		//this.originalAxPara = originalAxPara;
		super.setEventName("runCTCRequested");
	}

/*	public ConcreteTCase getConcreteTCase(){
		return cTCase;
	}*/

	/**
	* Gets the information of compilation asociated to this object
	* @return
	*/
	public CompilationInfo getCompilationInfo(){
		return compilationInfo;
	}
	/**
	* Gets the code asociated to this object
	* @return
	*/
	public String getRunCode(){
		return runCode;
	}

/*	public String getWorkingDirectory(){
		return workingDirectory;
	}

	public AbstractionLaw getAbsLaw(){
		return absLaw;
	}

	public AxPara getOriginalAxPara(){
		return originalAxPara;
	}*/

	//private ConcreteTCase cTCase;
	private CompilationInfo compilationInfo;
	private String runCode;
	//private String workingDirectory;
	//private AbstractionLaw absLaw;
	//private AxPara originalAxPara;
}