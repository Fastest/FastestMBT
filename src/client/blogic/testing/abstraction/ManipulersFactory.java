package client.blogic.testing.abstraction;

import compserver.abstraction.capture.xmlmanagers.ScriptTestWriter;
import compserver.abstraction.capture.uutmanipulers.VisibilityChanger;

/**
 * Interface that abstracts a factory of objects that manipulates the ConcreteTCase and
 * the UUT for capture the output
 */
public interface ManipulersFactory{
    /**
     * Returns an instance of ScriptTestWriter
     * @return the instance of the ScriptTestWriter appropiate
     */
    public ScriptTestWriter createScriptTestWriter(); 
    /**
     * Returns an instance of VisibilityChanger
     * @return the instance of the VisibilityCganger appropiate
     */
    public VisibilityChanger createVisibilityChanger(); 
} 
 
