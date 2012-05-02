package client.blogic.testing.ttree.tactics;


/**
 * Instances of this class (more commonly, instances of the subclasses of this
 * class) contains relevant information about the related tactic, such as the
 * tactic name and other necessary parameters.
 * @author Pablo Rodriguez Monetti
 */
public class TacticInfo{
	
	private String tacticName;

    
    /**
     * Sets the name of the associated tactic.
     * @param tacticName
     */
	public void setTacticName(String tacticName){
		this.tacticName = tacticName;
	}

    
    
    /**
     * Gets the name of the associated tactic.
     * @return
     */
	public String getTacticName(){
		return tacticName;
	}
} 
 
