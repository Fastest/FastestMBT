grammar TRAL;
options {
    language=Java;
}
@header {
package compserver.abstraction.parser;

import compserver.abstraction.types.spectypes.*;
import compserver.abstraction.types.impltypes.*;
import compserver.abstraction.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

}
@members{
	List<ImplNodeField> fields;
	List<ImplNodeField> fieldsList;
	List<AbstractionRule> rulesList;
	List<SpecNode> specNodes;
	List<ImplNode> implNodes;
	String code;
	String linkPrevNode;
	String lastPosPointedID;
	List<ImplNodeDBColumn> columnsList;
	List<SpecNode> cartesianMembers;
	Map<String,String> enumElements;
	List<VarComposition> compositions;
	ImplNodeEnumeration nodeEnumeration;
}

abstractionLaw returns [AbstractionLaw law]
: Text NEWLINE 
  rules {$law=new AbstractionLaw($Text.text,$rules.listRules);};

rules returns[List<AbstractionRule> listRules]
@init{
	rulesList = new ArrayList<AbstractionRule>();
}
: '@RULES' NEWLINE
   (rule {rulesList.add($rule.ruleObject);} NEWLINE)+ {$listRules = rulesList;} ;

rule returns[AbstractionRule ruleObject]
: Text ':' ruleAbstraction {$ruleObject = $ruleAbstraction.ruleObject; $ruleObject.setRuleName($Text.text);};

ruleAbstraction returns[AbstractionRule ruleObject]
@init{
	specNodes = new ArrayList<SpecNode>();
	implNodes = new ArrayList<ImplNode>();
	compositions = new ArrayList<VarComposition>();
	code = "";
}
: impId1 = Text':'impT1 = implType {ImplNode impNode1 = $impT1.implNode; impNode1.setImplID($impId1.text); implNodes.add(impNode1); }(',' impId2 = Text':'impT2 = implType {ImplNode impNode2 = $impT2.implNode; impNode2.setImplID($impId2.text); implNodes.add(impNode2); })* '==>' specId1 = Text':'specT1 = specType {SpecNode specNode1 = $specT1.specNode; specNode1.setSpecID($specId1.text); specNodes.add(specNode1); }(',' specId2 = Text':'specT2 = specType {SpecNode specNode2 = $specT2.specNode; specNode2.setSpecID($specId2.text); specNodes.add(specNode2); })* (',' NEWLINE composition {compositions = $composition.varCompositions;})* (',' NEWLINE decomposition)* (',' NEWLINE captureCode {code+=$captureCode.toCapture;})* {$ruleObject = new AbstractionRule("",implNodes,specNodes,code,compositions); };

composition returns[List<VarComposition> varCompositions]
@init{
	$varCompositions = new ArrayList<VarComposition>();
}
: 'COMPOSITION' 
  (NEWLINE '@SET' implID = Text 'AS' preOperator specID = Text  {$varCompositions.add(new VarComposition($specID.text,$preOperator.text,$implID.text)); } )+ ;

preOperator
: 'dom'|'ran'|'cardinal' ;

decomposition
: 'DECOMPOSITION' NEWLINE
  ('@SET' Text 'AS' Text NEWLINE)* ;

captureCode returns[String toCapture]
: '@CAPTURECODE' NEWLINE
  Text {$toCapture = $Text.text;};

specType returns [SpecNode specNode]
: basicType {$specNode = $basicType.specNode;}
| freeType {$specNode = $freeType.specNode;}
| pFunction {$specNode = $pFunction.specNode;}
| tFunction {$specNode = $tFunction.specNode;}
| relation {$specNode = $relation.specNode;}
| sequence {$specNode = $sequence.specNode;}
| powerSet {$specNode = $powerSet.specNode;}
| cartesianProd {$specNode = $cartesianProd.specNode;}
| integer {$specNode = $integer.specNode;}
| nat {$specNode = $nat.specNode;} ;


basicType returns [SpecNodeBasicType specNode]
: 'BASICTYPE' '[' Text ']' {$specNode=new SpecNodeBasicType($Text.text);};

freeType returns [SpecNodeFreeType specNode]
: 'FREETYPE' '[' Text ']' {$specNode=new SpecNodeFreeType($Text.text);};

pFunction returns [SpecNodePFun specNode]
: 'PFUN' '[' t1 = specType ',' t2 = specType ']' {$specNode=new SpecNodePFun($t1.specNode, $t2.specNode); };

tFunction returns [SpecNodeFun specNode]
: 'FUN' '[' t1 = specType ',' t2 = specType ']' {$specNode=new SpecNodeFun($t1.specNode, $t2.specNode); };

relation returns [SpecNodeRel specNode]
: 'REL' '[' t1 = specType ',' t2 = specType ']' {$specNode=new SpecNodeRel($t1.specNode, $t2.specNode); };

cartesianProd returns [SpecNodeCartesianProd specNode]
@init{
	cartesianMembers = new ArrayList<SpecNode>();
}
: 'PROD' '[' t1 = specType {cartesianMembers.add($t1.specNode);} ',' t2 = specType {cartesianMembers.add($t2.specNode);} (',' t3 = specType {cartesianMembers.add($t3.specNode);})*']' {$specNode=new SpecNodeCartesianProd(cartesianMembers); };

sequence returns [SpecNodeSeq specNode]
: 'SEQUENCE' '[' specType ']' {$specNode=new SpecNodeSeq($specType.specNode); };

powerSet returns [SpecNodePowerSet specNode]
: 'POWER' '[' specType ']' {$specNode=new SpecNodePowerSet($specType.specNode); };

integer returns [SpecNodeInt specNode]
: 'INT' {$specNode=new SpecNodeInt();};

nat returns [SpecNodeNat specNode]
: 'NAT' {$specNode=new SpecNodeNat();};

implType returns [ImplNode implNode]
: plType {$implNode = $plType.implNode;}
| pointer {$implNode = $pointer.implNode;}
| structure {$implNode = $structure.implNode;}
| array {$implNode = $array.implNode;}
| list {$implNode = $list.implNode;}
| file {$implNode = $file.implNode;}
| db {$implNode = $db.implNode;}
| enumeration {$implNode = $enumeration.implNode;} 
| screen {$implNode = $screen.implNode;} ;

plType returns [ImplNodePLType implNode]
: 'PLTYPE' '[' Text ']' {$implNode=new ImplNodePLType($Text.text);} ;

pointer returns [ImplNodePointer implNode]
:'POINTER' '[' implType ']' {$implNode=new ImplNodePointer($implType.implNode);} ;

structure returns [ImplNodeStructure implNode]
@init{
	fields = new ArrayList<ImplNodeField>();
}
:'STRUCTURE' '[' Text ',' f1 = field {fields.add($f1.implNode);}(',' f2 = field {fields.add($f2.implNode);})* ']' {$implNode=new ImplNodeStructure($Text.text,fields); };

field returns [ImplNodeField implNode]
: id1 = Text ':' t1 = implType {$implNode = new ImplNodeField($id1.text, $t1.implNode);};

array returns [ImplNodeArray implNode]
@init{
	fieldsList = new ArrayList<ImplNodeField>();
	lastPosPointedID = null;
}
: 'ARRAY' '[' implType ',' NATURAL (',' l2 = Text  {lastPosPointedID = $l2.text;})* ']' {$implNode=new ImplNodeArray($implType.implNode, Integer.parseInt($NATURAL.text),lastPosPointedID);} ;

list returns [ImplNodeList implNode]
@init{
	fieldsList = new ArrayList<ImplNodeField>();
	linkPrevNode = "";
}
: 'LIST' '[' id1 = Text ',' linkType ',' l1 = Text ',' (l2 = Text ',' {linkPrevNode = $l2.text;})* f1 = field {fieldsList.add($f1.implNode);}(',' f2 = field {fieldsList.add($f2.implNode);})*']' {$implNode = new ImplNodeList($id1.text,$linkType.text,$l1.text,linkPrevNode,fieldsList,"");} ;

linkType
: 'SLL'|'DLL'|'CLL'|'DCLL' ;

file returns [ImplNodeFile implNode]
: 'FILE' '[' n1 = Text ',' p1 = Text ',' d1 = Text ',' ('ENDOFLINE' '[' e1 = Text ']'',')* ('ENDOFFILE' '[' e2 = Text ']'',')*  fileStructure ']' {$implNode = new ImplNodeFile($n1.text, $p1.text,$d1.text,"","",$fileStructure.text);} ;

fileStructure
: 'LINEAR'|'RPL'|'FPL' ;

db returns [ImplNodeDB implNode]
@init{
	columnsList = new ArrayList<ImplNodeDBColumn>();
}
: 'DB' '[' d1 = Text ',' c1 = Text ',' t1 = Text ',' n1 = nodeDBColumn {columnsList.add($n1.columnObject);}(',' n2 = nodeDBColumn {columnsList.add($n2.columnObject);}) ']' {$implNode = new ImplNodeDB($d1.text, $c1.text, $t1.text, columnsList);} ;

nodeDBColumn returns [ImplNodeDBColumn columnObject]
: columnName = Text ',' columnType = Text {$columnObject = new ImplNodeDBColumn($columnName.text, $columnType.text);} ;

enumeration returns [ImplNodeEnumeration implNode]
@init{
	enumElements = new HashMap<String,String>();
}
: 'ENUMERATION' '[' implType (',' '\"' idImpl = ( Text | INTEGER | NATURAL ) '\"' '-->' idSpec = Text {
enumElements.put($idImpl.text,$idSpec.text);})+ ']'
//enumElements.put($idImpl.text.substring(1,$idImpl.text.length()-1),$idSpec.text);})+ ']'
{$implNode = new ImplNodeEnumeration($implType.implNode,enumElements);} ;

screen returns [ImplNodeScreen implNode]
: screenPlane {$implNode = $screenPlane.implNode;}
| screenTable {$implNode = $screenTable.implNode;};

screenPlane returns [ImplNodeScreen implNode]
: 'SCREEN' '[' 'plane' (',' enumeration )* ']' {$implNode=new ImplNodeScreenPlane(); 
$implNode.setEnumeration($enumeration.implNode);} ;

screenTable returns [ImplNodeScreenTable implNode]
: 'SCREEN' '[' 'table' ',' delimiter = Text (',' 'COLRANGE' '['columnRange = Range ']')* (',' 'ROWRANGE' '['rowRange = Range ']')* ']'{$implNode=new ImplNodeScreenTable();
	String strColRange = $columnRange.text;
	String strRanRange = $rowRange.text;
	$implNode.setDelimiter($delimiter.text);
	if(strColRange!=null && strColRange.matches("\\d+\\.\\.\\d+")){
		String[] parts = strColRange.split("\\.\\.");
		Integer cLB = new Integer(parts[0]);
		int columnLB = cLB.intValue();
		Integer cUB = new Integer(parts[1]);
		int columnUB = cUB.intValue();
		$implNode.setRowLowerBound(columnLB);
		$implNode.setRowUpperBound(columnUB);
	}
	if(strRanRange!=null &&  strRanRange.matches("\\d+\\.\\.\\d+")){
		String[] parts = strRanRange.split("\\.\\.");
		Integer rLB = new Integer(parts[0]);
		int rowLB = rLB.intValue();
		Integer rUB = new Integer(parts[1]);
		int rowUB = rUB.intValue();
		$implNode.setColumnLowerBound(rowLB);
		$implNode.setColumnUpperBound(rowUB);
	}

} ;

Text: ('a'..'z' |'A'..'Z' |'_'| '\\'| '\.' | '\/' ) ('a'..'z' |'A'..'Z' | '_' |'0'..'9'| '\\'| '\.'| '\?' | '\/' | '\'' | '\!' )* ;
//Phrase: Text (' ' Text)* ;
//Text: ('a'..'z' |'A'..'Z' |'_'| '\\'| '\.' | '\/' | '\"') ('a'..'z' |'A'..'Z' | '_' |'0'..'9'| '\\'| '\"'| '\.'| '\?' | '\/' | '\'' | '\!' )* ;
Range: '0'..'9'+ '..' '0'..'9'+ ;
NEWLINE:('\r' | '\n')+ ;
INTEGER :   '-''0'..'9'+ ;
NATURAL : '0'..'9'+ ;
WS : (' ' |'\t')+ {skip();} ;