package compserver.tcasegen.strategies;

import java.util.*;
import net.sourceforge.czt.z.ast.Pred;
import common.z.SpecUtils;

/**
 * This module stores the information related to aa atomic predicate that have a precalculated finite
 * model
 */
public class PreCalculatedPredicate{

	public PreCalculatedPredicate(){
	}
	public PreCalculatedPredicate(String tClassName, Pred atomicPred){
		this.tClassName = tClassName;
		this.atomicPred = atomicPred;
	}
	public void setAtomicPred(Pred atomicPred){
		this.atomicPred = atomicPred;
	}
	public Pred getAtomicPred(){
		return atomicPred;
	}
	public void setTClassName(String tClassName){
		this.tClassName = tClassName;
	}
	public String getTClassName(){
		return tClassName;
	}

	@Override
	public int hashCode() {
		//int hashCode = atomicPred.hashCode() + tClassName.hashCode();
		int hashCode = tClassName.hashCode();
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PreCalculatedPredicate){
			PreCalculatedPredicate other = (PreCalculatedPredicate) obj;
			if(!tClassName.equals(other.getTClassName()))
				return false;
			//if(!SpecUtils.areEqualTerms(atomicPred,other.getAtomicPred()))
			//	return false;
			String strAtomicPred = SpecUtils.termToLatex(atomicPred);
			String strOtherAtomicPred = SpecUtils.termToLatex(other.getAtomicPred());
			if(!strAtomicPred.equals(strOtherAtomicPred))
				return false;
			return true;
		}
		else
			return false;
	}

	private Pred atomicPred;
	private String tClassName;
}
