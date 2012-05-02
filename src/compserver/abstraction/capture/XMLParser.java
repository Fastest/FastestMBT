package compserver.abstraction.capture;

import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

/**
 * Parses the file with the information of the captured variables and stores them in the
 * corresponding inheritor of CapturedVar
 */
public class XMLParser {

	public XMLParser(String filePath){
		this.filePath = filePath;
		vars = new ArrayList<CapturedVar>();
	}
	public List<CapturedVar> parse() throws SAXException, IOException {
	DocumentBuilder builder = null;
	try{
	builder = (DocumentBuilderFactory.newInstance()).newDocumentBuilder();
	}
	catch(Exception e){}
	// We parse the file and create the Document object
	Document document = builder.parse(filePath);
	// We obtain the nodes of depth 1
	NodeList level1 
	= document.getDocumentElement().getChildNodes();
	for (int i = 0; i < level1.getLength(); i++) {
	Node node_i = level1.item(i);
	if (node_i.getNodeType() == Node.ELEMENT_NODE
	&& ((Element) node_i).getTagName().equals("assign")) {
	Element assignment = (Element) node_i;
	NodeList nodes_j = assignment.getChildNodes();
	// We analyze the child nodes of the object that abstract the assign label
	analizeAssignment(nodes_j);
	}
	}
	return vars;
	}
	private void analizeAssignment(NodeList nodes){
		String varName = "";
		for (int i = 0; i < nodes.getLength(); i++){
			Node node_i = nodes.item(i);
			if (isElementNode(node_i) && matchTagName((Element) node_i, "varID")){
				Element varIDElement = (Element) node_i;
				NodeList nodes_j = varIDElement.getChildNodes();
				//We obtain the name of the variable
				Node nameNode = nodes_j.item(0);
				if (isTextNode(nameNode)){
					Text nameText = (Text) nameNode;
					varName = getText(nameText);
				}
			}
			else if(isElementNode(node_i) && matchTagName((Element) node_i, "varValue")){
				// We find the object that abstract the varValue label and
				// we analyze it for create the appropriate CapturedVar
				Element varValueElement = (Element) node_i;
				NodeList nodes_j = varValueElement.getChildNodes();
				CapturedVar capturedVar = analizeValue(nodes_j,varName);
				vars.add(capturedVar);
			}
		}
	}

	private CapturedVar analizeValue(NodeList nodes, String varName){
		CapturedVar capturedVar = null;
		boolean found = false;
		for (int i = 0; i < nodes.getLength() && !found; i++){
			Node node_i = nodes.item(i);
			if (isElementNode(node_i) && matchTagName((Element) node_i, "intValue")) {
				found = true;
				Element varValueElement = (Element) node_i;
				NodeList nodes_j = varValueElement.getChildNodes();
				capturedVar = analizeIntValue(nodes_j,varName);
			}
			else if (isElementNode(node_i) && matchTagName((Element) node_i, "floatValue")) {
				found = true;
				Element varValueElement = (Element) node_i;
				NodeList nodes_j = varValueElement.getChildNodes();
				capturedVar = analizeRealValue(nodes_j,varName);
			}
			else if (isElementNode(node_i) && matchTagName((Element) node_i, "stringValue")) {
				found = true;
				Element varValueElement = (Element) node_i;
				NodeList nodes_j = varValueElement.getChildNodes();
				capturedVar = analizeStringValue(nodes_j,varName);
			}
			else if (isElementNode(node_i) && matchTagName((Element) node_i, "pointerValue")) {
				found = true;
				Element varValueElement = (Element) node_i;
				NodeList nodes_j = varValueElement.getChildNodes();
				capturedVar = analizePointerValue(nodes_j,varName);
			}
			else if (isElementNode(node_i) && matchTagName((Element) node_i, "structure")) {
				found = true;
				Element varValueElement = (Element) node_i;
				NodeList nodes_j = varValueElement.getChildNodes();
				capturedVar = analizeStructureValue(nodes_j,varName);
			}
			else if (isElementNode(node_i) && matchTagName((Element) node_i, "array")) {
				found = true;
				Element varValueElement = (Element) node_i;
				NodeList nodes_j = varValueElement.getChildNodes();
				capturedVar = analizeArrayValue(nodes_j,varName);
			}
			else if (isElementNode(node_i) && matchTagName((Element) node_i, "screen")) {
				found = true;
				Element varValueElement = (Element) node_i;
				NodeList nodes_j = varValueElement.getChildNodes();
				capturedVar = analizeScreenValue(nodes_j,varName);
			}
			else if (isElementNode(node_i) && matchTagName((Element) node_i, "list")) {
				found = true;
				Element varValueElement = (Element) node_i;
				NodeList nodes_j = varValueElement.getChildNodes();
				capturedVar = analizeListValue(nodes_j,varName);
			}
			else if (isElementNode(node_i) && matchTagName((Element) node_i, "db")) {
				found = true;
				Element varValueElement = (Element) node_i;
				NodeList nodes_j = varValueElement.getChildNodes();
				capturedVar = analizeDBValue(nodes_j,varName);
			}
			else if (isElementNode(node_i) && matchTagName((Element) node_i, "file")) {
				found = true;
				Element varValueElement = (Element) node_i;
				NodeList nodes_j = varValueElement.getChildNodes();
				capturedVar = analizeFileValue(nodes_j,varName);
			}
//			else
//				System.out.println("Ninguna de las anteriores!!!");
		}
		return capturedVar;
	}
	private CapturedVar analizeIntValue(NodeList nodes, String varName){
		CapturedVarInt capturedVar = new CapturedVarInt();
		capturedVar.setVarName(varName);
		for (int i = 0; i < nodes.getLength(); i++){
			Node node_i = nodes.item(i);	
			if (node_i.getNodeType() == Node.TEXT_NODE){
					Text nameText = (Text) node_i;
					int valor = Integer.parseInt(nameText.getWholeText().trim());
					capturedVar.setValue(valor);
			}
		}
		return capturedVar;
	}
	private CapturedVar analizeRealValue(NodeList nodes, String varName){
		CapturedVarReal capturedVar = new CapturedVarReal();
		capturedVar.setVarName(varName);
		for (int i = 0; i < nodes.getLength(); i++){
			Node node_i = nodes.item(i);	
			if (node_i.getNodeType() == Node.TEXT_NODE){
					Text nameText = (Text) node_i;
					double valor = new Double(nameText.getWholeText().trim());
					capturedVar.setValue(valor);
			}
		}
		return capturedVar;
	}
	private CapturedVar analizeStringValue(NodeList nodes, String varName){
		CapturedVarString capturedVar = new CapturedVarString();
		capturedVar.setVarName(varName);
		for (int i = 0; i < nodes.getLength(); i++){
			Node node_i = nodes.item(i);	
			if (node_i.getNodeType() == Node.TEXT_NODE){
					Text nameText = (Text) node_i;
					String valor = nameText.getWholeText().trim();
					capturedVar.setValue(valor);
			}
		}
		return capturedVar;
	}
	private CapturedVar analizePointerValue(NodeList nodes, String varName){
		CapturedVarPointer capturedVar = new CapturedVarPointer();
		capturedVar.setVarName(varName);
		capturedVar.setValue(analizeValue(nodes,varName));
		return capturedVar;
	}
	private CapturedVar analizeStructureValue(NodeList nodes, String varName){
		//Map<String,CapturedVar> fields = new HashMap<String,CapturedVar>();
		List<CapturedField> fields = new ArrayList<CapturedField>();
		CapturedVarStructure capturedVar = new CapturedVarStructure();
		capturedVar.setVarName(varName);
		for (int i = 0; i < nodes.getLength(); i++){
			Node node_i = nodes.item(i);	
			if (node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("field")) {
				Element varValueElement = (Element) node_i;
				NodeList nodes_j = varValueElement.getChildNodes();
				fields.add(obtainField(nodes_j,varName));
			}
		}
		capturedVar.setValue(fields);
		return capturedVar;
	}
	private CapturedVar analizeArrayValue(NodeList nodes, String varName){
		List<CapturedVar> elements = new ArrayList<CapturedVar>();
		CapturedVarArray capturedVar = new CapturedVarArray();
		capturedVar.setVarName(varName);
		for (int i = 0; i < nodes.getLength(); i++){
			Node node_i = nodes.item(i);	
			if (node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("arrayEntry")) {
				Element varValueElement = (Element) node_i;
				NodeList nodes_j = varValueElement.getChildNodes();
				elements.add(obtainArrayEntry(nodes_j,varName));
			}
		}
		capturedVar.setValue(elements);
		return capturedVar;
	}
	private CapturedVar analizeListValue(NodeList nodes, String varName){
		// In ImplNodeList we assume that the links for travel the list
		// aren't fields of the list
		//List<Map<String,CapturedVar>> elements = new ArrayList<Map<String,CapturedVar>>();
		List<List<CapturedField>> elements = new ArrayList<List<CapturedField>>();
		CapturedVarList capturedVar = new CapturedVarList();
		capturedVar.setVarName(varName);
		for(int j=0;j<nodes.getLength();j++){
			Node node_j = nodes.item(j);
			if (node_j.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_j).getTagName().equals("listNode")){
				//Map<String,CapturedVar> element = new HashMap<String,CapturedVar>();
				List<CapturedField> element = new ArrayList<CapturedField>();
				Element listNodeElement = (Element) node_j;
				NodeList nodes_j = listNodeElement.getChildNodes();
				for (int i = 0; i < nodes_j.getLength(); i++){
					Node node_i = nodes_j.item(i);
					if (node_i.getNodeType() == Node.ELEMENT_NODE
					&& ((Element) node_i).getTagName().equals("field")) {
						Element varValueElement = (Element) node_i;
						NodeList nodes_i = varValueElement.getChildNodes();
						element.add(obtainField(nodes_i,varName));
					}
				}
				elements.add(element);
			}
		}
		capturedVar.setValue(elements);
		return capturedVar;
	}
	private CapturedVar analizeDBValue(NodeList nodes, String varName){
		Map<String,List<String>> columns = new HashMap<String,List<String>>();
		CapturedVarDB capturedVar = new CapturedVarDB();
		capturedVar.setVarName(varName);
		for (int i = 0; i < nodes.getLength(); i++){
			Node node_i = nodes.item(i);	
			if (node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("column")) {
				Element varValueElement = (Element) node_i;
				NodeList nodes_j = varValueElement.getChildNodes();
				columns.putAll(obtainColumn(nodes_j));
			}
		}
		capturedVar.setValue(columns);
		return capturedVar;
	}
	private CapturedVar analizeFileValue(NodeList nodes, String varName){
		CapturedVarFile capturedVar = new CapturedVarFile();
		capturedVar.setVarName(varName);
		for (int i = 0; i < nodes.getLength(); i++){
			Node node_i = nodes.item(i);	
			if (node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("name")) {
				Element fileNameElement = (Element) node_i;
				NodeList nodes_j = fileNameElement.getChildNodes();
				Node nameNode = nodes_j.item(0);
				if (nameNode.getNodeType() == Node.TEXT_NODE){
					Text nameText = (Text) nameNode;
					String fileName = nameText.getWholeText().trim();
					capturedVar.setFileName(fileName);
				}
			}
			else if(node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("path")){
				Element pathElement = (Element) node_i;
				NodeList nodes_j = pathElement.getChildNodes();
				Node nameNode = nodes_j.item(0);
				if (nameNode.getNodeType() == Node.TEXT_NODE){
					Text nameText = (Text) nameNode;
					String path = nameText.getWholeText().trim();
					capturedVar.setPath(path);
				}
			}
			else if(node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("delimiter")){
				Element delimiterElement = (Element) node_i;
				NodeList nodes_j = delimiterElement.getChildNodes();
				Node nameNode = nodes_j.item(0);
				if (nameNode.getNodeType() == Node.TEXT_NODE){
					Text nameText = (Text) nameNode;
					String delimiter = nameText.getWholeText();
					if(delimiter.endsWith("\n")){
						delimiter = delimiter.substring(0, delimiter.length()-1);
					}
					if(delimiter.startsWith("\n")){
						delimiter = delimiter.substring(1);
					}
					capturedVar.setDelimiter(delimiter);
				}
			}
			else if(node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("structure")){
				Element structureElement = (Element) node_i;
				NodeList nodes_j = structureElement.getChildNodes();
				Node nameNode = nodes_j.item(0);
				if (nameNode.getNodeType() == Node.TEXT_NODE){
					Text nameText = (Text) nameNode;
					String structure = nameText.getWholeText().trim();
					capturedVar.setStructure(structure);
				}
			}
			else if(node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("eol")){
				Element eolElement = (Element) node_i;
				NodeList nodes_j = eolElement.getChildNodes();
				Node nameNode = nodes_j.item(0);
				if (nameNode.getNodeType() == Node.TEXT_NODE){
					Text nameText = (Text) nameNode;
					String eol = nameText.getWholeText().trim();
					capturedVar.setEol(eol);
				}
			}
			else if(node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("eof")){
				Element eofElement = (Element) node_i;
				NodeList nodes_j = eofElement.getChildNodes();
				Node nameNode = nodes_j.item(0);
				if (nameNode.getNodeType() == Node.TEXT_NODE){
					Text nameText = (Text) nameNode;
					String eof = nameText.getWholeText().trim();
					capturedVar.setEof(eof);
				}
			}
		}
		return capturedVar;
	}

	private CapturedVar analizeScreenValue(NodeList nodes, String varName){
		CapturedVarScreen capturedVar = new CapturedVarScreen();
		capturedVar.setVarName(varName);
		Map<String,String> enumElements = null;
		for (int i = 0; i < nodes.getLength(); i++){
			Node node_i = nodes.item(i);	
			if (node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("screentype")) {
				Element fileNameElement = (Element) node_i;
				NodeList nodes_j = fileNameElement.getChildNodes();
				Node nameNode = nodes_j.item(0);
				if (nameNode.getNodeType() == Node.TEXT_NODE){
					Text nameText = (Text) nameNode;
					String screenType = nameText.getWholeText().trim();
					capturedVar.setScreenType(screenType);
				}
			}
			else if (node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("enum")) {
				Element fileNameElement = (Element) node_i;
				NodeList nodes_j = fileNameElement.getChildNodes();
				enumElements = obtainEnumElements(nodes_j,varName);
			}
			else if (node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("delimiter")) {
				Element fileNameElement = (Element) node_i;
				NodeList nodes_j = fileNameElement.getChildNodes();
				Node nameNode = nodes_j.item(0);
				if (nameNode.getNodeType() == Node.TEXT_NODE){
					Text nameText = (Text) nameNode;
					String delimiter = nameText.getWholeText().trim();
					capturedVar.setDelimiter(delimiter);
				}
			}
			else if (node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("rowLowerBound")) {
				Element fileNameElement = (Element) node_i;
				NodeList nodes_j = fileNameElement.getChildNodes();
				Node nameNode = nodes_j.item(0);
				if (nameNode.getNodeType() == Node.TEXT_NODE){
					Text nameText = (Text) nameNode;
					String rowLBStr = nameText.getWholeText().trim();
					Integer rowLBInte = new Integer(rowLBStr);
					capturedVar.setRowLowerBound(rowLBInte.intValue());
				}
			}
			else if (node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("rowUpperBound")) {
				Element fileNameElement = (Element) node_i;
				NodeList nodes_j = fileNameElement.getChildNodes();
				Node nameNode = nodes_j.item(0);
				if (nameNode.getNodeType() == Node.TEXT_NODE){
					Text nameText = (Text) nameNode;
					String rowUBStr = nameText.getWholeText().trim();
					Integer rowUBInte = new Integer(rowUBStr);
					capturedVar.setRowUpperBound(rowUBInte.intValue());
				}
			}
			else if (node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("columnLowerBound")) {
				Element fileNameElement = (Element) node_i;
				NodeList nodes_j = fileNameElement.getChildNodes();
				Node nameNode = nodes_j.item(0);
				if (nameNode.getNodeType() == Node.TEXT_NODE){
					Text nameText = (Text) nameNode;
					String colLBStr = nameText.getWholeText().trim();
					Integer colLBInte = new Integer(colLBStr);
					capturedVar.setColumnLowerBound(colLBInte.intValue());
				}
			}
			else if (node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("columnUpperBound")) {
				Element fileNameElement = (Element) node_i;
				NodeList nodes_j = fileNameElement.getChildNodes();
				Node nameNode = nodes_j.item(0);
				if (nameNode.getNodeType() == Node.TEXT_NODE){
					Text nameText = (Text) nameNode;
					String colUBStr = nameText.getWholeText().trim();
					Integer colUBInte = new Integer(colUBStr);
					capturedVar.setColumnUpperBound(colUBInte.intValue());
				}
			}
		}
		capturedVar.setEnumElements(enumElements);
		return capturedVar;
	}

	private Map<String,String> obtainEnumElements(NodeList nodes, String varName){
		Map<String,String> enumElements = new HashMap<String,String>();
		for (int i = 0; i < nodes.getLength(); i++){
			Node node_i = nodes.item(i);	
			if (node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("enumentry")) {
				String implValue = "";
				String specValue = "";
				Element element_i = (Element) node_i;
				NodeList nodes_j = element_i.getChildNodes();
				for(int j=0;j<nodes_j.getLength();j++){
					Node node_j = nodes_j.item(j);
					if (node_j.getNodeType() == Node.ELEMENT_NODE
					&& ((Element) node_j).getTagName().equals("implvalue")) {
					Element element_j = (Element) node_j;
					NodeList nodes_k = element_j.getChildNodes();
					Node node_k = nodes_k.item(0);
					implValue = getText(node_k);
					}
					else if (node_j.getNodeType() == Node.ELEMENT_NODE
					&& ((Element) node_j).getTagName().equals("specvalue")) {
					Element element_j = (Element) node_j;
					NodeList nodes_k = element_j.getChildNodes();
					Node node_k = nodes_k.item(0);
					specValue = getText(node_k);
					}
				}
			enumElements.put(implValue,specValue);
			}
		}
		return enumElements;
	}

	private String getText(Node node){
		String text = "";
		if (node.getNodeType() == Node.TEXT_NODE){
			Text auxText = (Text) node;
			text = auxText.getWholeText().trim();
		}
		return text;
	}

	private Map<String,List<String>> obtainColumn(NodeList nodes){
		Map<String,List<String>> column = new HashMap<String,List<String>>();
		String columnName = "";
		List<String> columnValues = new ArrayList<String>();
		for (int i = 0; i < nodes.getLength(); i++){
			Node node_i = nodes.item(i);	
			if (node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("columnID")) {
				Element varValueElement = (Element) node_i;
				NodeList nodes_j = varValueElement.getChildNodes();
				columnName = extractText(nodes_j);
			}
			else if (node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("columnValue")) {
				Element varValueElement = (Element) node_i;
				NodeList nodes_j = varValueElement.getChildNodes();
				String cell = extractText(nodes_j);
				columnValues.add(cell);
			}	
		}
		column.put(columnName, columnValues);
		return column;
	}

	private CapturedField obtainField(NodeList nodes, String varName){
		CapturedField capturedField = new CapturedField();
		//Map<String,CapturedVar> field = new HashMap<String,CapturedVar>();
		String fieldName="";
		CapturedVar capturedVar = null;
		for (int i = 0; i < nodes.getLength(); i++){
			Node node_i = nodes.item(i);	
			if (node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("elementID")) {
				Element varValueElement = (Element) node_i;
				NodeList nodes_j = varValueElement.getChildNodes();
				fieldName = extractText(nodes_j);
			}
			else if (node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("elementValue")) {
				Element varValueElement = (Element) node_i;
				NodeList nodes_j = varValueElement.getChildNodes();
				capturedVar = analizeValue(nodes_j,varName+"."+fieldName);
			}
		}
		capturedField.setFieldName(fieldName);
		capturedField.setCapturedVar(capturedVar);
		return capturedField;
	}
	private CapturedVar obtainArrayEntry(NodeList nodes, String varName){
		CapturedVar capturedVar = null;
		for (int i = 0; i < nodes.getLength(); i++){
			Node node_i = nodes.item(i);	
			if (node_i.getNodeType() == Node.ELEMENT_NODE
			&& ((Element) node_i).getTagName().equals("indexValue")) {
				Element varValueElement = (Element) node_i;
				NodeList nodes_j = varValueElement.getChildNodes();
				capturedVar = analizeValue(nodes_j,varName);
			}
		}
		return capturedVar;
	}
	private String extractText(NodeList nodes){
		String text = "";
		for (int i = 0; i < nodes.getLength(); i++){
			Node node_i = nodes.item(i);	
			if (node_i.getNodeType() == Node.TEXT_NODE){
					Text nameText = (Text) node_i;
					text = nameText.getWholeText().trim();
			}
		}
		return text;
	}

	/**
	* Returns a boolean value that indicates if a node is an element node
	* @param node
	*/
	private boolean isElementNode(Node node){
		return (node.getNodeType() == Node.ELEMENT_NODE);
	}
	/**
	* Returns a boolean value that indicates if a node is an text node
	* @param node
	*/
	private boolean isTextNode(Node node){
		return (node.getNodeType() == Node.TEXT_NODE);
	}

	/**
	* Returns a boolean value that indicates if a tag match with the tag of a node
	* @param element
	* @param tag
	*/
	private boolean matchTagName(Element element, String tag){
		return (element.getTagName().equals(tag));
	}

	/**
	* Obtains the string associated to a text node
	* @param textNode
	*/
	private String getText(Text textNode){
		return textNode.getWholeText().trim();
	}


	private String filePath;
	private List<CapturedVar> vars;
}