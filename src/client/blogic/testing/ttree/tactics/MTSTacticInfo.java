/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.blogic.testing.ttree.tactics;

import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.SetExpr;


/**
 *
 * @author Pablo Rodriguez Monetti
 */
public class MTSTacticInfo extends TacticInfo{

   private Expr expr;
   private SetExpr setExpr;

   public void setExpr(Expr expr) {
       this.expr = expr;
   }

   public Expr getExpr(){
       return expr;
   }

   public void setSetExpr(SetExpr setExpr) {
       this.setExpr = setExpr;
   }

   public SetExpr getSetExpr(){
       return setExpr;
   }



}