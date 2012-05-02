package compserver.tcasegen.eval;


/**
 * An instance of this class represents the result of a serie of schema evaluations, 
 * consisting of evaluate the same schema with different assignation to values. The result
 * is structured as a pair (r,l), where r is a boolean that indicates whether the schema
 * could be satisfied and l is the log that resumes the evaluations performed.
 * @author Pablo Rodriguez Monetti
 */
public class EvaluationResp{

	private boolean result;
	private String log;

    /**
     * Creates an instance of EvaluationResp.
     * @param result
     * @param log
     */
	public EvaluationResp(boolean result, String log){
		this.result = result;
		this.log = log;
	}

    
    /**
     * Sets the log of this instance.
     * @param log
     */
	public void setLog(String log){
		this.log = log;
	}

    
    /**
     * Gets the log of this instance.
     * @return
     */
	public String getLog(){
		return log;
	}

    
    
    /**
     * Sets the result of this instance, ie. of the evaluations performed.
     * @param result
     */
	public void setResult(boolean result){
		this.result = result;
	}	

    
    
    /**
     * Gets the result of this instance, ie. of the evaluations performed.
     * @return
     */
	public boolean getResult(){
		return result;
	}
}