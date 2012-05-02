package compserver.abstraction.capture.xmlmanagers;

import java.util.*;

/**
 * The instance of this class (which is a singleton) generates integers that will be used
 * for avoid names conflicts
 */
public class IndexGenerator {
    
    private static IndexGenerator indexGenerator;
    private int index;
    

    /**
     * Creates intances of IndexGenerator.
     */
	private IndexGenerator(){
		index = 0;
	}

    /**
     * Gets the instance of this class, creating it if necessary.
     * @return
     */
	public static IndexGenerator getInstance(){	
		if(indexGenerator==null){
			indexGenerator = new IndexGenerator();
		}
		return indexGenerator;
	}

    /**
     * Gets the next index to be used
     * @return
     */
	public int get(){
		return index++;
	}
    /**
     * Resets to zero the index
     * @return
     */
	public void reset(){
		index = 0;  // Ver donde lo reseteo
	}

}