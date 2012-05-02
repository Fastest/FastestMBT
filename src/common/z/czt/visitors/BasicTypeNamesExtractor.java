/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.z.czt.visitors;

import java.util.*;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.base.visitor.TermVisitor;
import net.sourceforge.czt.z.ast.GivenPara;
import net.sourceforge.czt.z.ast.Name;
import net.sourceforge.czt.z.ast.ZNameList;
import net.sourceforge.czt.z.visitor.GivenParaVisitor;

/**
 * This visitor makes it possible to extract the names of the defined basic types
 * of an specification.
 * @author Pablo Rodriguez Monetti
 */
public class BasicTypeNamesExtractor implements GivenParaVisitor<List<String>>,
        TermVisitor<List<String>>{


    public List<String> visitGivenPara(GivenPara givenPara) {
        List<String> basicTypeNames = new ArrayList<String>();
        ZNameList zNameList = givenPara.getZNameList();

        Boolean result = new Boolean(false);

        if(zNameList.size() != 0){
            result = Boolean.TRUE;
        }

        for (int j = 0; j < zNameList.size(); j++) {
            Name name = zNameList.get(j);
            basicTypeNames.add(name.toString());
        }


        return basicTypeNames;
    }


    public List<String> visitTerm(Term term) {
        Object[] array = term.getChildren();
        List<String> basicTypeNames = new ArrayList<String>();
        for (int i = 0; i < array.length; i++) {
            final Object object = array[i];
            if (object instanceof Term) {
                List<String> auxBasicTypeNames = ((Term) object).accept(this);
                for (int j = 0; j < auxBasicTypeNames.size(); j++) {
                    basicTypeNames.add(auxBasicTypeNames.get(j));
                }
            }
        }
        return basicTypeNames;
    }


}
