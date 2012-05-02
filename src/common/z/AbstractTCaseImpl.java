package common.z;

import java.util.*;

import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.SchText;
import net.sourceforge.czt.z.ast.ZSchText;
import net.sourceforge.czt.z.ast.NameList;
import net.sourceforge.czt.z.ast.ZNameList;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.Box;
import net.sourceforge.czt.util.Visitor;
import net.sourceforge.czt.z.impl.ZFactoryImpl;

import common.z.czt.visitors.CZTCloner;


/**
 * An implementation of the AbstractTCaseImpl.
 * @author Pablo Rodriguez Monetti
 */
public class AbstractTCaseImpl implements AbstractTCase{
	
	private AxPara myAxPara;
	private String schName;

	public AbstractTCaseImpl(AxPara axPara, String schName){
            setMyAxPara(axPara);
            this.schName = schName;
        }	

    
    /**
     * Creates instances of this class. The constructed test class will have the inclusion of
     * the schema whose name is given by the specified String and its predicate will consist
     * of the conjunction of the equalities taken from the specified Map.
     * @param axPara
     * @param dadName
     * @param varExprMap
     */
	public AbstractTCaseImpl(AxPara axPara, String dadName, Map<RefExpr, Expr> varExprMap){
		AxPara newAxPara = (AxPara) axPara.accept(new CZTCloner());

		ZFactory zFactory = new ZFactoryImpl();
		ZName zName = zFactory.createZName(dadName , 
                zFactory.createZStrokeList(), "IncludedTClass");
		RefExpr refExpr = zFactory.createRefExpr(zName,
                zFactory.createZExprList(), false, false);
		ZDeclList zDeclList = zFactory.createZDeclList();
		zDeclList.add(0, zFactory.createInclDecl(refExpr));
		SpecUtils.setAxParaListOfDecl(newAxPara, zDeclList);
        schName = dadName  + "_TCASE";
		SpecUtils.setAxParaName(newAxPara, schName);
		SpecUtils.setAxParaPred(newAxPara, SpecUtils.createAndPred(varExprMap));
		setMyAxPara(newAxPara);
	}



    
    /**
     * Verifies whether the specified AxPara's predicate has an equality for each explicitly 
     * declared variable. With "explicitly declared variables" we refer to variables not
     * declared in an included schema.
     * @param axPara
     * @return
     */
	public static boolean isAbstractTCase(AxPara axPara){
		if (!SchemeImpl.isScheme(axPara))
			return false;
		return true;
	}
	
	public void setMyAxPara(AxPara axPara){ 
		myAxPara = axPara;
	}

	public AxPara getMyAxPara(){
		return myAxPara;
	}

	public void setSchName(String schName){
		this.schName = schName;
	}

	public String getSchName(){
		return schName;
	}

	public ZNameList getName(){
		return myAxPara.getName();
	}

    public Box getBox(){
		return myAxPara.getBox();
	}

	public NameList getNameList(){
		return myAxPara.getNameList();
	}

	public SchText getSchText(){
		return myAxPara.getSchText();
	}

	public ZNameList getZNameList(){
		return myAxPara.getZNameList();
	}

	public ZSchText getZSchText(){
		return myAxPara.getZSchText();
	}

	public void	setBox(Box box){
		myAxPara.setBox(box);
	}

	public void	setNameList(NameList nameList){
		myAxPara.setNameList(nameList);
	}

	public void setSchText(SchText schText) {
		myAxPara.setSchText(schText);
	}

	public <R> R accept(Visitor<R> v){
		return myAxPara.accept(v);	
	}

	public Term create(Object[] args){
		return myAxPara.create(args);
	}

	public <T> T getAnn(Class<T> c){
		return myAxPara.getAnn(c);
	}

	public List getAnns(){
		return myAxPara.getAnns();
	}

	public Object[] getChildren(){
		return myAxPara.getChildren();
	}

    @Override
	public AxPara clone(){
		return new AbstractTCaseImpl((AxPara)myAxPara.accept(new CZTCloner()), schName);
	}

}

