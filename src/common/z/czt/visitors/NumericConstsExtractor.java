/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.z.czt.visitors;

import java.util.*;
import java.math.BigInteger;

import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.base.visitor.TermVisitor;
import net.sourceforge.czt.z.visitor.NumExprVisitor;
import net.sourceforge.czt.z.visitor.ApplExprVisitor;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.NumExpr;
import net.sourceforge.czt.z.ast.ApplExpr;

/**
 * An instance of this class allow the extraction of every numeric constant that appear in
 * a term. This class is based on the Visitor design pattern.
 * @author Pablo Rodriguez Monetti
 */
public class NumericConstsExtractor implements
        TermVisitor<ArrayList<BigInteger>>,
        NumExprVisitor<ArrayList<BigInteger>>,
        ApplExprVisitor<ArrayList<BigInteger>> {

    public ArrayList<BigInteger> visitTerm(Term term) {
        Object[] array = term.getChildren();
        ArrayList<BigInteger> arrayList = new ArrayList<BigInteger>();
        for (int i = 0; i < array.length; i++) {
            final Object object = array[i];
            if (object instanceof Term) {
                ArrayList<BigInteger> auxArrayList = ((Term) object).accept(this);
                for (int j = 0; j < auxArrayList.size(); j++) {
                    arrayList.add(auxArrayList.get(j));
                }
            }
        }
        return arrayList;
    }

    public ArrayList<BigInteger> visitNumExpr(NumExpr numExpr) {
        ArrayList<BigInteger> arrayList = new ArrayList<BigInteger>();
        arrayList.add(numExpr.getValue());
        return arrayList;
    }

    public ArrayList<BigInteger> visitApplExpr(ApplExpr applExpr) {
        ArrayList<BigInteger> arrayList = new ArrayList<BigInteger>();
        Expr leftExpr = applExpr.getLeftExpr();
        Expr rightExpr = applExpr.getRightExpr();
        if (leftExpr instanceof RefExpr
                && ((RefExpr) leftExpr).getZName().toString().equals("- _ ")
                && rightExpr instanceof NumExpr) {
            BigInteger bigInteger = ((NumExpr) rightExpr).getValue();
            long value = bigInteger.longValue();
            arrayList.add(BigInteger.valueOf(value * -1));
        } else {
            ArrayList<BigInteger> arrayList1 = leftExpr.accept(this);
            ArrayList<BigInteger> arrayList2 = rightExpr.accept(this);
            for (int i = 0; i < arrayList1.size(); i++) {
                arrayList.add(arrayList1.get(i));
            }
            for (int i = 0; i < arrayList2.size(); i++) {
                arrayList.add(arrayList2.get(i));
            }

        }

        return arrayList;
    }
}
