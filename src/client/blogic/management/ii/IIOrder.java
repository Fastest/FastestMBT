package client.blogic.management.ii;

import java.lang.reflect.*;

import client.blogic.management.ii.events.Event_;
import client.util.AbstractOrder;


/**
 * Maintains the information about an object, a method of that object and the
 * explicit parameter for that method. This information will be used to denote
 * methods interested on some events.
 * @author Pablo Rodriguez Monetti
 */
public class IIOrder extends AbstractOrder{

	private IIComponent implicitParam;
	private String methodName;
	private Event_ explicitParam;

    
    
    /**
     * Creates intances of IIOrder.
     * @param implicitParam
     * @param methodName
     */
	public IIOrder(IIComponent implicitParam, String methodName){
		this.implicitParam = implicitParam;
		this.methodName = methodName;
		//this.myLock = new ReentrantLock();
	}

    
    
    /**
     * Sets the explicit parameter of the method to be invoked.
     * @param explicitParam
     */
	public void setExplicitParam(Event_ explicitParam){
		this.explicitParam = explicitParam;	
	}

    
    
     /**
     * Gets the explicit parameter of the method to be invoked.
     * @return
     */
	public Event_ getExplicitParam(){
			return explicitParam;
	}  
            
    
    
    /**
     * Gets the implicit parameter of the method to be invoked.
     * @return
     */
	public IIComponent getImplicitParam(){
			return implicitParam;
	}

    
    
    /**
     * Gets the name of the method to be invoked.
     * @return
     */
	public String getMethodName(){
			return methodName;
	}

    
    
    /**
     * Calls the method methodName on the object implicitParam using the 
     * explicitParam parameter.
     */
    @Override
	public void run(){
		try{
			Method method = implicitParam.getClass().getMethod(methodName, new Class[] {Event_.class});
			method.invoke(implicitParam, explicitParam);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

    
    
    /**
     * Makes a copy of this object.
     * @return
     */
    @Override
	public Object clone(){
		return new IIOrder(implicitParam, methodName);
	}
}