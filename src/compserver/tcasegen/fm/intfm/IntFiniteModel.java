package compserver.tcasegen.fm.intfm; 

import compserver.tcasegen.fm.*;
import java.util.*;
import java.math.*;

/**
 * Interface that abstracts a finite model generator associated to the type of 
 * the Z specification language that corresponds to the integer numbers.
 * @author Pablo Rodriguez Monetti
 */
public interface IntFiniteModel extends FiniteModel{
    
    public void initialize(List<BigInteger> list);
    public void initialize(int fMSize);
} 