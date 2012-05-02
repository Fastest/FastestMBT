package common.z;

import java.util.*;

import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.SchText;
import net.sourceforge.czt.z.ast.ZSchText;
import net.sourceforge.czt.z.ast.NameList;
import net.sourceforge.czt.z.ast.ZNameList;
import net.sourceforge.czt.z.ast.Box;
import net.sourceforge.czt.util.Visitor;

import common.z.czt.visitors.CZTCloner;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;

/**
 * An implementation of the interface OpScheme.
 * @author Pablo Rodriguez Monetti
 */
public class OpSchemeImpl implements OpScheme {

    private AxPara myAxPara;

    public OpSchemeImpl(AxPara axPara)
            throws IllegalArgumentException {

        if (!isOpScheme(axPara)) {
            throw new IllegalArgumentException();
        } else {
            setMyAxPara(axPara);
        }
    }

    public static boolean isOpScheme(AxPara axPara) {
        /*

        if (!SchemeImpl.isScheme(axPara))
        return false;
        AbstractRepository<String> varNameRep = SpecUtils.getVarNames(axPara);
        return(isThereTransition(varNameRep) ||
        isThereIO(varNameRep));
         */
        return true;
    }

    public void setMyAxPara(AxPara axPara) {
        myAxPara = axPara;
    }

    public AxPara getMyAxPara() {
        return myAxPara;
    }

    public ZNameList getName() {
        return myAxPara.getName();
    }

    public Box getBox() {
        return myAxPara.getBox();
    }

    public NameList getNameList() {
        return myAxPara.getNameList();
    }

    public SchText getSchText() {
        return myAxPara.getSchText();
    }

    public ZNameList getZNameList() {
        return myAxPara.getZNameList();
    }

    public ZSchText getZSchText() {
        return myAxPara.getZSchText();
    }

    public void setBox(Box box) {
        myAxPara.setBox(box);
    }

    public void setNameList(NameList nameList) {
        myAxPara.setNameList(nameList);
    }

    public void setSchText(SchText schText) {
        myAxPara.setSchText(schText);
    }

    public <R> R accept(Visitor<R> v) {
        return myAxPara.accept(v);
    }

    public Term create(Object[] args) {
        return myAxPara.create(args);
    }

    public <T> T getAnn(Class<T> c) {
        return myAxPara.getAnn(c);
    }

    public List getAnns() {
        return myAxPara.getAnns();
    }

    public Object[] getChildren() {
        return myAxPara.getChildren();
    }

    @Override
    public AxPara clone() {
        return new OpSchemeImpl((AxPara) myAxPara.accept(new CZTCloner()));
    }

    /**
    Determines whether the specified repository has primed and not-primed ocurrencies for evey name
     */
    private static boolean isThereTransition(AbstractRepository<String> varNameRep) {
        boolean transitionFound = false;

        AbstractIterator<String> it = varNameRep.createIterator();
        while (it.hasNext() && !transitionFound) {
            String varName = it.next();
            int varNameLen = varName.length();
            String primeChar = UtilSymbols.primeSymbol();
            if (varName.substring(varNameLen - 1, varNameLen).equals(primeChar)) {
                transitionFound = lookUp(varNameRep, varName.substring(0, varNameLen - 1));
            } else {
                transitionFound = lookUp(varNameRep, varName.concat(primeChar));
            }
        }
        return (transitionFound);
    }

    private static boolean isThereIO(AbstractRepository<String> varNameRep) {
        boolean inputVarFound = false;
        boolean outputVarFound = false;
        AbstractIterator<String> it = varNameRep.createIterator();
        while (it.hasNext() && (!inputVarFound || !outputVarFound)) {
            String varName = it.next();
            int varNameLen = varName.length();
            if (varName.substring(varNameLen - 1, varNameLen).equals("?")) {
                inputVarFound = true;
            }
            if (varName.substring(varNameLen - 1, varNameLen).equals("!")) {
                outputVarFound = true;
            }
        }
        return (inputVarFound && outputVarFound);
    }

    /**
    Determines whether the specified repository has the specified name or no
     */
    private static boolean lookUp(AbstractRepository<String> varNameRep, String varName) {
        boolean varFound = false;
        AbstractIterator<String> it = varNameRep.createIterator();
        while (it.hasNext() && !varFound) {
            varFound = it.next().equals(varName);
        }
        return (varFound);
    }
}
