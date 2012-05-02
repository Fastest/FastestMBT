/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.z.czt.visitors;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.OrExpr;
import net.sourceforge.czt.z.visitor.ExprVisitor;
import net.sourceforge.czt.z.visitor.OrExprVisitor;

/**
 *
 * @author Pablo Rodriguez Monetti
 */
public class OrExprExtractor implements OrExprVisitor<List<Expr>>,
        ExprVisitor<List<Expr>>{


    public List<Expr> visitOrExpr(OrExpr orExpr){
        List<Expr> leftExprList = (orExpr.getLeftExpr()).accept(this);
        List<Expr> rightExprList = (orExpr.getRightExpr()).accept(this);
        List<Expr> exprList = new ArrayList<Expr>();
        if(leftExprList != null)
            exprList.addAll(leftExprList);
        if(rightExprList != null)
            exprList.addAll(rightExprList);
        return exprList;
    }

    public List<Expr> visitExpr(Expr expr){
        List<Expr> exprList = new ArrayList<Expr>();
        exprList.add(expr);
        return exprList;
    }


}


