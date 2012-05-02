package client.blogic.management.ii.events;

import common.z.TClass;

/**
 * Represents the implicit invocation event that must be announced when a test
 * case for a test class is requested.
 * @author Pablo Rodriguez Monetti
 */
public class TCaseRequested extends Event_{

	private String opName;
	private TClass tClass;
        private int maxEval;

    
    /**
     * Creates instances of TCaseRequested.
     * @param opName
     * @param tClass
     * @param tCaseStrategy
     */
	public TCaseRequested(String opName, TClass tClass, int maxEval){
		this.opName = opName;
		this.tClass = tClass;
                this.maxEval = maxEval;
		super.setEventName("tCaseRequested");
	}

    
    
    /**
     * Sets the name of the operation associated to this object.
     * @param opName
     */
	public void setOpName(String opName){
		this.opName = opName;
	}

    
    
    /**
     * Gets the name of the operation associated to this object.
     * @return
     */
	public String getOpName(){
		return opName;
	}

    
    
    /**
     * Sets the test class associated to this object.
     * @param tClass
     */
 	public void setTClass(TClass tClass){
		this.tClass = tClass;
	}

    
    
    /**
     * Gets the test class associated to this object.
     * @return
     */
	public TClass getTClass(){
		return tClass;
	}


        /**
         * Sets the maximum number of evaluations that will be performed in
         * each test class for which a test case is requested.
         * @param maxEval the maximum number of evaluations that will be
         * performed in each test class for which a test case is requested.
         */
        public void setMaxEval(int maxEval){
            this.maxEval = maxEval;
        }


        /**
         * Gets the maximum number of evaluations that will be performed in
         * each test class for which a test case is requested.
         * @return the maximum number of evaluations that will be
         * performed in each test class for which a test case is requested.
         */
        public int getMaxEval(){
            return maxEval;
        }
} 
