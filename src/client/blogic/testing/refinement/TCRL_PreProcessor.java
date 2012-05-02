package client.blogic.testing.refinement;

import java.util.*;

/**
 * Represents a JAVA preprocessor for TCRL.
 * 
 * @author  Pablo D. Coca
 *
 * @since   v2.0
 *
 * @version 2.0
 */
public class TCRL_PreProcessor {

    /**
     * Returns a TCRL AST without synonyms rules.
     *
     * @param	ast		an TCRL AST with synonyms rules
     *
     * @return	an TCRL AST without synonyms rules
     *
     * @author  Pablo D. Coca
     *
     * @since   v2.0
     *
     * @version 2.0
     */
    static HashMap<String, NodeType> synonymsTable = new HashMap<String, NodeType>();

    static public TCRL_AST preProcess(TCRL_AST ast) {

        //--------------------------------------------------------------------------------
        // VARIABLES
        //--------------------------------------------------------------------------------
        NodeRules oldRules = ast.getRules();
        NodeRules newRules = new NodeRules();
        //--------------------------------------------------------------------------------
        Set<String> setRuleNames = oldRules.getKeys();
        Iterator<String> iterRuleNames = setRuleNames.iterator();
        //--------------------------------------------------------------------------------

        //--------------------------------------------------------------------------------
        // Extracts the synonyms inside the REFINEMENT LAW
        //--------------------------------------------------------------------------------
        while (iterRuleNames.hasNext()) {
            String ruleName = iterRuleNames.next();

            NodeRule rule = oldRules.getRule(ruleName);

            if (rule instanceof RuleSynonym) {
                synonymsTable.put(((RuleSynonym) rule).getName(),
                        ((RuleSynonym) rule).getNodeType());
            }
        }
        //--------------------------------------------------------------------------------

        //--------------------------------------------------------------------------------  
        // RESET the ITERATOR
        //--------------------------------------------------------------------------------
        setRuleNames = oldRules.getKeys();
        iterRuleNames = setRuleNames.iterator();
        //--------------------------------------------------------------------------------

        //--------------------------------------------------------------------------------
        // Replaces the SYNONYMS in the REFINEMENT LAW and discards them.
        //--------------------------------------------------------------------------------
        while (iterRuleNames.hasNext()) {
            String ruleName = iterRuleNames.next();

            NodeRule rule = oldRules.getRule(ruleName);

            //--------------------------------------------------------------------------------
            // * RuleRefinement
            //--------------------------------------------------------------------------------
            if (rule instanceof RuleRefinement) {

                RuleRefinement newRule = replaceSynonymsInRule((RuleRefinement) rule,
                        oldRules);

                newRules.addRule(ruleName, newRule);
            }
            //--------------------------------------------------------------------------------


            //--------------------------------------------------------------------------------
            // * RuleSynonym
            //--------------------------------------------------------------------------------
            // If the rule is not a refinement rule is a synonym rule, and it is
            // discarded in the new rules.
            //--------------------------------------------------------------------------------      
        }

        //--------------------------------------------------------------------------------
        // RETURN the NEW AST
        //--------------------------------------------------------------------------------
        return new TCRL_AST(ast.getName(),
                ast.getPreamble(),
                newRules, // The RULES without SYNONYNMS
                ast.getEpilogue());
        //--------------------------------------------------------------------------------
    }

    static public RuleRefinement replaceSynonymsInRule(RuleRefinement rule, NodeRules rules) {
        //--------------------------------------------------------------------------------
        // VARIABLES
        //--------------------------------------------------------------------------------
        Set<String> implIDs = rule.getImplIDs();
        Iterator<String> iterImplIDs = implIDs.iterator();
        //--------------------------------------------------------------------------------

        //--------------------------------------------------------------------------------
        // Replaces the SYNONYMS in each NodeType
        //--------------------------------------------------------------------------------
        while (iterImplIDs.hasNext()) {
            String implID = iterImplIDs.next();

            NodeType oldNodeType = rule.getNodeType(implID);

            NodeType newNodeType = replaceSynonymsInType(oldNodeType);

            rule.setNodeType(implID, newNodeType);	// Replaces the SYNONYMS in the NodeType

        }

        return rule;
    }
    //--------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------
    // Replaces the SYNONYMS in each NodeType
    //--------------------------------------------------------------------------------
    static public NodeType replaceSynonymsInType(NodeType nodeType) {

        //--------------------------------------------------------------------------------
        // VARIABLES
        //--------------------------------------------------------------------------------    
        // NodeType newType = new NodeType();
        //--------------------------------------------------------------------------------

        //--------------------------------------------------------------------------------
        // Determines wich type of NODE is "nodeType" and replaces recursively its SYNONYMS
        //--------------------------------------------------------------------------------


        //--------------------------------------------------------------------------------
        // * NodeSynonym
        //--------------------------------------------------------------------------------
        if (nodeType instanceof NodeSynonym) {
            String SynonymID = ((NodeSynonym) nodeType).getID();

            NodeType newType = new NodeType();

            // Gets the Node associated with SynonymID
            newType = synonymsTable.get(SynonymID);

            if ((newType instanceof NodeArray) || (newType instanceof NodePointer) || (newType instanceof NodeStructure) || (newType instanceof NodeList)) {
                NodeType newType2 = replaceSynonymsInType(newType);

                return newType2;
            } else {
                return newType;
            }
        } //--------------------------------------------------------------------------------
        // * NodePLType
        //--------------------------------------------------------------------------------
        else if (nodeType instanceof NodePLType) {
            // NOTHING to DO. RETURN the SAME.
            // PLType cannot contains a SYNONYM
            // newType = nodeType;

            return (NodePLType) nodeType;
        } //--------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------
        // * NodeEnumeration
        //--------------------------------------------------------------------------------
        else if (nodeType instanceof NodeEnumeration) {
            NodeType enumerationType = ((NodeEnumeration) nodeType).getType();
            HashMap<String, String> enumerationElements = ((NodeEnumeration) nodeType).getElements();

            NodeType newEnumerationType = replaceSynonymsInType(enumerationType);

            NodeEnumeration newEnumeration = new NodeEnumeration(newEnumerationType,
                    enumerationElements);

            NodeType newType = newEnumeration;

            return newType;
        } //--------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------
        // * NodePointer
        //--------------------------------------------------------------------------------
        else if (nodeType instanceof NodePointer) {
            NodeType pointerType = ((NodePointer) nodeType).getType();

            NodeType newPointerType = replaceSynonymsInType(pointerType);

            NodePointer newPointer = new NodePointer(newPointerType);

            NodeType newType = newPointer;


            return newType;
        } //--------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------
        // * NodeArray
        //--------------------------------------------------------------------------------
        else if (nodeType instanceof NodeArray) {
            NodeType arrayType = ((NodeArray) nodeType).getType();

            int arraySize = ((NodeArray) nodeType).getSize();

	    String lastPosPointerID = ((NodeArray) nodeType).getLastPosPointerID();

            NodeType newArrayType = replaceSynonymsInType(arrayType);

            NodeArray newArray = new NodeArray(newArrayType, arraySize, lastPosPointerID);

            NodeType newType = newArray;


            return newType;
        } //--------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------
        // * NodeStructure
        //--------------------------------------------------------------------------------
        else if (nodeType instanceof NodeStructure) {
            String oldStructureName = ((NodeStructure) nodeType).getName();
            List<NodeElement> oldStructureElements = ((NodeStructure) nodeType).getElements();
            List<NodeElement> newStructureElements = new ArrayList<NodeElement>();

            for (int i = 0; i < oldStructureElements.size(); i++) {
                //--------------------------------------------------------------------------------
                NodeElement oldElement = oldStructureElements.get(i);
                //--------------------------------------------------------------------------------
                String oldElementID = oldElement.getID();
                NodeType oldElementType = oldElement.getType();
                String oldElementConstant = oldElement.getConstant();
                //--------------------------------------------------------------------------------
                NodeType newElementType = replaceSynonymsInType(oldElementType);

                NodeElement newElement = new NodeElement(oldElementID,
                        newElementType,
                        oldElementConstant);
                //--------------------------------------------------------------------------------
                newStructureElements.add(newElement);
                //--------------------------------------------------------------------------------
            }

            NodeStructure newStructure = new NodeStructure(oldStructureName,
                    newStructureElements);

            NodeType newType = newStructure;


            return newType;
        } //--------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------
        // * NodeList
        //--------------------------------------------------------------------------------
        else if (nodeType instanceof NodeList) {
	    NodeList newList = null;
            String oldListName = ((NodeList) nodeType).getName();
            List<NodeElement> oldListFields = ((NodeList) nodeType).getFields();

	    if(oldListFields!=null){
	    // Is a list in C languanje
             List<NodeElement> newListFields = new ArrayList<NodeElement>();
             String oldListLinkType = ((NodeList) nodeType).getLinkType();
             String oldListLinkNextName = ((NodeList) nodeType).getLinkNextName();
             String oldListLinkPrevName = ((NodeList) nodeType).getLinkPrevName();
             String oldMemAlloc = ((NodeList) nodeType).getMemAlloc();
            for (int i = 0; i < oldListFields.size(); i++) {

                NodeElement oldElement = oldListFields.get(i);

                String oldElementID = oldElement.getID();
                NodeType oldElementType = oldElement.getType();
                String oldElementConstant = oldElement.getConstant();
                //--------------------------------------------------------------------------------
                NodeType newElementType = replaceSynonymsInType(oldElementType);

                NodeElement newElement = new NodeElement(oldElementID,
                        newElementType,
                        oldElementConstant);
                //--------------------------------------------------------------------------------
                newListFields.add(newElement);
                //--------------------------------------------------------------------------------
            }

            newList = new NodeList(oldListName,
                    oldListLinkType,
                    oldListLinkNextName,
                    oldListLinkPrevName,
                    newListFields,
                    oldMemAlloc);
	    }
	    else{
		// Is a list in Java Languaje
		String oldListType = ((NodeList) nodeType).getListType();
		NodeType oldNodeType = ((NodeList) nodeType).getNodeType();
		NodeType newNodeType = replaceSynonymsInType(oldNodeType);
		newList = new NodeList(oldListName,oldListType,newNodeType);
	    }

            NodeType newType = newList;


            return newType;
        } //--------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------
        // * NodeFile
        //--------------------------------------------------------------------------------
        else if (nodeType instanceof NodeFile) {
            // newType = (NodeFile)nodeType;

            return (NodeFile) nodeType;
        } //--------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------
        // * NodeDB
        //--------------------------------------------------------------------------------
        else if (nodeType instanceof NodeDB) {
            // newType = (NodeDB)nodeType;
            return (NodeDB) nodeType;
        } //--------------------------------------------------------------------------------
        else {
            throw new IllegalArgumentException();

        }

    }
}
