/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.z.czt.visitors;

import java.util.*;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.base.visitor.TermVisitor;
import net.sourceforge.czt.z.ast.FreePara;
import net.sourceforge.czt.z.ast.Freetype;
import net.sourceforge.czt.z.ast.FreetypeList;
import net.sourceforge.czt.z.impl.ZFreetypeListImpl;
import net.sourceforge.czt.z.visitor.FreeParaVisitor;

/**
 * This visitor makes it possible to extract the names of the defined free types
 * of an specification.
 * @author Pablo Rodriguez Monetti
 */
public class FreeTypeNamesExtractor implements FreeParaVisitor<List<String>>,
        TermVisitor<List<String>> {



    public List<String> visitFreePara(FreePara freePara) {
        List<String> freeTypeNames = new ArrayList<String>();

        FreetypeList freetypeList =
                freePara.getFreetypeList();
        if (freetypeList instanceof ZFreetypeListImpl) {

            ZFreetypeListImpl zFreetypeListImpl =
                    (ZFreetypeListImpl) freetypeList;
            for (int j = 0; j < zFreetypeListImpl.size(); j++) {
                Freetype freetype = zFreetypeListImpl.get(j);
                freeTypeNames.add(freetype.getName().toString());
            }
        }


        return freeTypeNames;
    }

    public List<String> visitTerm(Term term) {
        Object[] array = term.getChildren();
        List<String> freeTypeNames = new ArrayList<String>();
        for (int i = 0; i < array.length; i++) {
            final Object object = array[i];
            if (object instanceof Term) {
                List<String> auxFreeTypeNames = ((Term) object).accept(this);
                for (int j = 0; j < auxFreeTypeNames.size(); j++) {
                    freeTypeNames.add(auxFreeTypeNames.get(j));
                }
            }
        }
        return freeTypeNames;
    }

}
