package compserver.tcasegen.fm;

import java.util.*;

import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.impl.ZFactoryImpl;


import common.z.SpecUtils;

/**
 * An instance of this class represents a finite model generator associated to a basic type
 * of the Z specification language.
 * @author Pablo Rodriguez Monetti
 */
public class GivenFiniteModel implements FiniteModel {

    private int size;
    private int btSize;
    private int index;
    private Expr normalizedType;
    private String typeName;
    private ZFactory zFactory;
    private List<String> originalConstants;
    private List<String> allConstants;

    /**
     * Creates an instance of this class.
     * @param size
     * @param typeName
     */
    public GivenFiniteModel(int btSize, String typeName,
            List<String> constantsList) {
        this.btSize = btSize;
        this.originalConstants = constantsList;
        // We add the constants to a set in order to remove duplicates
        Set<String> constantsSet = new HashSet();
        for (int i = 0; i < btSize; i++) {
            constantsSet.add(typeName + i);
        }

        if (constantsList != null) {
            for (int i = 0; i < constantsList.size(); i++) {
                constantsSet.add(constantsList.get(i));
            }
        }

        allConstants = new ArrayList<String>();
        Iterator<String> constantsSetIt = constantsSet.iterator();
        while (constantsSetIt.hasNext()) {
            String constant = constantsSetIt.next();
            allConstants.add(constant);
        }

        size = allConstants.size();
        this.typeName = typeName.toLowerCase();
        index = 0;
        normalizedType = SpecUtils.getNumRefExpr();
        zFactory = new ZFactoryImpl();
    }

    /**
     * Initializes the iterator related to this FiniteModel.
     */
    public void initialize() {
        index = 0;
    }

    /**
     * Returns true whether the iteration has more elements.
     * @return true whether the iterator has more elements.
     */
    public boolean hasNext() {
        return (index < size);
    }

    /**
     * Returns the next element in the iteration.
     * @return the next element in the iteration.
     */
    public Expr next() {

        String constantName = allConstants.get(index);
        ZName lowerZName = zFactory.createZName(constantName,
                zFactory.createZStrokeList(), "lower");

        RefExpr refExpr = zFactory.createRefExpr(lowerZName,
                zFactory.createZExprList(), false, false);
        index++;
        return refExpr;
    }

    public Expr getNormalizedType() {
        return normalizedType;
    }

    public int getFMSize() {
        return size;
    }

    @Override
    public Object clone() {
        return new GivenFiniteModel(btSize, typeName, originalConstants);
    }
}
