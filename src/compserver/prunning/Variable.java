package compserver.prunning;

/**
 * Instances of this class represents variables of the theorems
 */
public class Variable{

	public Variable()
	{}

	public Variable(String name, String type)
	{
		this.name = name;
		this.type = type;
	}
    /**
     * Sets the name of the variable
     * @param name
     */
	public void setName(String name){
        	this.name = name;
    	}

    /**
     * Gets the name of the variable
     */
	public String getName(){
        	return name;
    }

    /**
     * Sets the type of the variable
     * @param type
     */
	public void setType(String type){
        	this.type = type;
    	}

    /**
     * Gets the type of the variable
     */
	public String getType(){
        	return type;
	}
	private String name;
	private String type;
}