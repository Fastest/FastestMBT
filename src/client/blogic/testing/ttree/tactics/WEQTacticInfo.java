/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.blogic.testing.ttree.tactics;

import java.util.*;

import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.Expr;

/**
 *
 * @author Pablo Rodriguez Monetti
 */
public class WEQTacticInfo extends TacticInfo{


    private Map<RefExpr, Expr> values;
    private Pred pred;


    public void setValues(Map<RefExpr, Expr> values){
        this.values = values;
    }

    public Map<RefExpr,Expr> getValues(){
        return values;
    }


    public void setPred(Pred pred){
        this.pred = pred;
    }

    public Pred getPred(){
        return pred;
    }

}
