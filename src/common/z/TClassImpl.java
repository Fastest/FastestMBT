package common.z;

import java.util.*;

import net.sourceforge.czt.util.Visitor;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Box;
import net.sourceforge.czt.z.ast.SchText;
import net.sourceforge.czt.z.ast.ZSchText;
import net.sourceforge.czt.z.ast.NameList;
import net.sourceforge.czt.z.ast.ZNameList;
import net.sourceforge.czt.base.ast.Term;

import common.z.czt.visitors.CZTCloner;

/**
 * An implementation of the interface TClass.
 * @author Pablo Rodriguez Monetti
 */
public class TClassImpl implements TClass {

    private AxPara myAxPara;
    private String schName;

    public TClassImpl(AxPara axPara, String schName)
            throws IllegalArgumentException {

        if (!isTClass(axPara)) {
            throw new IllegalArgumentException();
        } else {
            setMyAxPara(axPara);
            this.schName = schName;
            SpecUtils.setAxParaName(axPara, schName);
        }
    }

    public static boolean isTClass(AxPara axPara) {
        if (!OpSchemeImpl.isOpScheme(axPara)) {
            return true;
        }
        return true;
        //return false;
    }

    public void setMyAxPara(AxPara axPara) {
        myAxPara = axPara;
    }

    public AxPara getMyAxPara() {
        return myAxPara;
    }

    public void setSchName(String schName) {
        this.schName = schName;
    }

    public String getSchName() {
        return schName;
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
        return new TClassImpl((AxPara) myAxPara.accept(new CZTCloner()), schName);
    }
}
