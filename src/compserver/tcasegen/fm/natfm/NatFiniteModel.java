package compserver.tcasegen.fm.natfm; 

import compserver.tcasegen.fm.*;
import java.util.*;
import java.math.*;

/**
 * Interface that abstracts a finite model generator associated to the type of 
 * the Z specification language that corresponds to the naturals numbers.
 * @author Pablo Rodriguez Monetti
 */
public interface NatFiniteModel extends FiniteModel{
    public void initialize(List<BigInteger> list);
    public void initialize(int fMSize); 
} 