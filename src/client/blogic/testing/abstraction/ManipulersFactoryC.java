package client.blogic.testing.abstraction;

import compserver.abstraction.capture.xmlmanagers.ScriptTestWriter;
import compserver.abstraction.capture.uutmanipulers.VisibilityChanger;
import compserver.abstraction.capture.xmlmanagers.ScriptTestWriterC;
import compserver.abstraction.capture.uutmanipulers.VisibilityChangerC;

/**
 * Interface that abstracts a factory of objects that manipulates the ConcreteTCase and
 * the UUT for capture the output
 */
public class ManipulersFactoryC implements ManipulersFactory{
    /**
     * Returns an instance of ScriptTestWriterC
     * @return the instance of the ScriptTestWriterC 
     */
    public ScriptTestWriter createScriptTestWriter(){
	return new ScriptTestWriterC();
    }
    /**
     * Returns an instance of VisibilityChangerV
     * @return the instance of the VisibilityChangerC
     */
    public VisibilityChanger createVisibilityChanger(){
	return new VisibilityChangerC();
    } 
} 
 
