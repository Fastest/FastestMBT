package compserver.abstraction.tcasegeneration;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.math.BigInteger;
import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.impl.*;
import common.z.AbstractTCase;
import common.z.SpecUtils;
import common.z.UtilSymbols;
import compserver.abstraction.types.impltypes.*;
import compserver.abstraction.types.spectypes.*;
import compserver.abstraction.capture.*;
import client.blogic.testing.refinement.ConstantStore;
import compserver.abstraction.*;


public class VarAbstraction{


	public static List<Expr> abstractRule(AbstractionRule rule, List<CapturedVar> capturedVars) throws IllegalArgumentException{
		List<Expr> exprs = new ArrayList<Expr>();
		// We analyze the cardinality of the variables of the specification and the
		// cardinality of variables of the implementation
		List<SpecNode> specNodes = rule.getSpecNodes();
		List<ImplNode> implNodes = rule.getImplNodes();
		if(specNodes.size()==1 && implNodes.size()==1){
		// Simple case
		SpecNode specNode = specNodes.get(0);
		ImplNode implNode = implNodes.get(0);
		String implNodeID = implNode.getImplID(); 
		CapturedVar capturedVar = AbstractionUtils.getCapturedVar(capturedVars, implNodeID);
		Expr expr = abstractSimpleCase(capturedVar,rule,specNode);
		if(expr!=null)
			exprs.add(expr);
		return exprs;
		}
		else if(specNodes.size()==1 && implNodes.size()>1){
		// Composition case
			Expr expr = abstractCompositionCase(rule, capturedVars);
			if(expr!=null)
				exprs.add(expr);
			return exprs;
		}
		return null;
	}

	private static Expr abstractSimpleCase(CapturedVar capturedVar, AbstractionRule rule, SpecNode specNode){
		Expr expr;
		// We analyze the type of the variable to be abstracted
		if(specNode instanceof SpecNodeBasicType)
			expr = abstractToBasicType(capturedVar,rule,specNode);
		else if(specNode instanceof SpecNodeFreeType)
			expr = abstractToFreeType(capturedVar,rule,specNode);
		else if(specNode instanceof SpecNodeCartesianProd)
			expr = abstractToCartesianProd(capturedVar,rule,specNode);
		else if(specNode instanceof SpecNodeBinRelation)
			expr = abstractToBinRelation(capturedVar,rule,specNode);
		else if(specNode instanceof SpecNodeInt)
			expr = abstractToInt(capturedVar,rule,specNode);
		else if(specNode instanceof SpecNodeNat)
			expr = abstractToNat(capturedVar,rule,specNode);
		else if(specNode instanceof SpecNodePowerSet)
			expr = abstractToPowerSet(capturedVar,rule,specNode);
		else if(specNode instanceof SpecNodeSeq)
			expr = abstractToSeq(capturedVar,rule,specNode);
		else{
			return null;
		}
		return expr;
	}

	private static Expr abstractCompositionCase(AbstractionRule rule, List<CapturedVar> capturedVars){

		ZFactory zFactory = new ZFactoryImpl();
		List<VarComposition> compositions = rule.getCompositions();
		SpecNode specNode = rule.getSpecNodes().get(0);
		String specID = specNode.getSpecID();
		// Por ahora solo estan soportadas las relaciones binarias con Composition
		if(specNode instanceof SpecNodeBinRelation){
		SpecNodeBinRelation specNodeBin = (SpecNodeBinRelation) specNode;
		SpecNode domNode = specNodeBin.getDomType();
		SpecNode ranNode = specNodeBin.getRanType(); 

		// We create SpecNodePowerSet objects from the nodes of the domain and range
		// for send to abstract separately
		SpecNode domType = new SpecNodePowerSet(domNode);
		SpecNode ranType = new SpecNodePowerSet(ranNode);
		domType.setSpecID(specID);
		ranType.setSpecID(specID);
		SetExpr domExpr = null;
		SetExpr ranExpr = null;
		for(int i=0;i<compositions.size();i++){
			VarComposition composition = compositions.get(i);
			String implID = composition.getImplID();
			String preOperator = composition.getPreOperator();
			CapturedVar capturedVar = AbstractionUtils.getCapturedVar(capturedVars, implID);
			Expr expr = null;
			if(preOperator.equals("dom")){
				expr = abstractSimpleCase(capturedVar, rule, domType);
				if(expr!=null)
					if(expr instanceof SetExpr)
						domExpr = (SetExpr) expr;
			}
			else if(preOperator.equals("ran")){
				expr = abstractSimpleCase(capturedVar, rule, ranType);
				if(expr!=null)
					if(expr instanceof SetExpr)
						ranExpr = (SetExpr) expr;
			}
			else{
				System.out.println("Fastest not support the operator "+ preOperator +" in rules with the COMPOSITION token");
				return null;
			}
		}
		if(ranExpr != null && domExpr != null){
			ZExprList setZList = zFactory.createZExprList();
			ZExprList domList = domExpr.getZExprList();
			ZExprList ranList = ranExpr.getZExprList();
			if(ranList.size()!=domList.size()){
				System.out.println("There was an error in the abstraction of "+specID+"."); // Dar mas detalle
				return null;
			}
			for(int i=0;i<ranList.size();i++){	
				List<Expr> tupleElements = new ArrayList<Expr>();
				Expr domElement = domList.get(i);
				tupleElements.add(domElement);
				Expr ranElement = ranList.get(i);
				tupleElements.add(ranElement);
				ZExprList zList = zFactory.createZExprList(tupleElements);
				TupleExpr tupleExpr = zFactory.createTupleExpr(zList);
				setZList.add(tupleExpr);
			}
			SetExpr setExpr = zFactory.createSetExpr(setZList);
			return setExpr;
		}
		}
		else{
			System.out.println(specID+" is not a binary relation. Fastest only support binary relations in abstractions with the COMPOSITION token");
			return null;
		}
		return null;
	}

	private static Expr abstractToBasicType(CapturedVar capturedVar, AbstractionRule rule, SpecNode specNode) throws IllegalArgumentException{
		ZFactory zFactory = new ZFactoryImpl();
		ConstantStore constantStore = ConstantStore.getInstance();
		String varName = capturedVar.getVarName();


		if(capturedVar instanceof CapturedVarInt){
			CapturedVarInt varInt = (CapturedVarInt) capturedVar;
			String stringValue = new Integer(varInt.getValue()).toString();
			// The variables refined from a basic type to an integer value must
			// have been stored in ConstantStore
			if(constantStore.get(varName,stringValue)!=null){
				return constantStore.get(varName,stringValue);
			}
			else{
				//System.out.println("No encontro a: "+stringValue);
				//System.out.println("El nombre de la variable capturada es:\n"+varName);
				//constantStore.show();
				return null;
			}
		}
		else if(capturedVar instanceof CapturedVarString){
			CapturedVarString varString = (CapturedVarString) capturedVar;
			String stringValue = varString.getValue();
			// If the value captured was stored in ConstanStore we return this
			// value, otherwise, we return an Expr obtained from the capturer
			// value
			if(constantStore.get(varName,stringValue)!=null){
				return constantStore.get(varName,stringValue);
			}
			else{
				if(!stringValue.equals("(null)")){
				ZName zName = zFactory.createZName(stringValue , 
				zFactory.createZStrokeList(), stringValue);
				RefExpr refExpr = zFactory.createRefExpr(zName, 
				zFactory.createZExprList(), false, false);
				return refExpr;
				}
				else{
					return null;
				}
			}
		}
		else if(capturedVar instanceof CapturedVarScreen){
			CapturedVarScreen varScreen = (CapturedVarScreen) capturedVar;
			String screenType = varScreen.getScreenType();
			String screenPath = varScreen.getScreenPath();
			Map<String,String> enumElements = varScreen.getEnumElements();

			// The screenType must be plane because a variable of basic type 
			// can assume only one value
			if(screenType.equals("plane")){
			String line = "";
			try{
			BufferedReader inOp = new BufferedReader(new FileReader(screenPath));
			line = inOp.readLine();
			}
			catch(Exception e){
				e.printStackTrace(System.out);
			}
			if(line!=null){
			String name = "";
			line = line.trim();
			//System.out.println("Busca a: "+line);
			// If the CapturedVarScreen was used with enumeration we look for
			// the entry related with this line
			if(enumElements!=null){
				boolean found = false;
				Set<Map.Entry<String,String>> set = enumElements.entrySet();
				Iterator<Map.Entry<String,String>> it = set.iterator();
				while(it.hasNext() && !found){
					Map.Entry<String,String> mapEntry = it.next();
					String implValue = mapEntry.getKey();
					//System.out.println("Inspecciona a: "+implValue);
					if(implValue.equals(line)){
						found = true;
						name = mapEntry.getValue();
					}
				}
			}
			else
				name = line;
			// Nuevamente el tema de los IDs
			ZName zNameString = zFactory.createZName(name,zFactory.createZStrokeList(), "str");
			ZExprList zExprList = zFactory.createZExprList(); 
			RefExpr refExprStr = zFactory.createRefExpr(zNameString, zExprList, false, false);
			return refExprStr;
			}

			} 
			else
				throw new IllegalArgumentException();
		}
		else
			throw new IllegalArgumentException();
		return null;
	}
	private static Expr abstractToInt(CapturedVar capturedVar, AbstractionRule rule, SpecNode specNode) throws IllegalArgumentException{
		ZFactory zFactory = new ZFactoryImpl();

		if(capturedVar instanceof CapturedVarInt){
			CapturedVarInt varInt = (CapturedVarInt) capturedVar;
			int value = varInt.getValue();
			ZNumeral zNumeral = zFactory.createZNumeral(BigInteger.valueOf(value));
			NumExpr numExpr = zFactory.createNumExpr(zNumeral);
			return numExpr;
		}
		else if(capturedVar instanceof CapturedVarScreen){
			CapturedVarScreen varScreen = (CapturedVarScreen) capturedVar;
			String screenType = varScreen.getScreenType();
			String screenPath = varScreen.getScreenPath();
			Map<String,String> enumElements = varScreen.getEnumElements();
			// The screenType must be plane because an integer variable
			// can assume only one value
			if(screenType.equals("plane")){
			try{
			BufferedReader inOp = new BufferedReader(new FileReader(screenPath));
			String line = inOp.readLine();
			if(line!=null){
			BigInteger bInt = new BigInteger(line);
			ZNumeral zNumeral = zFactory.createZNumeral(bInt);
			NumExpr numExpr = zFactory.createNumExpr(zNumeral);
			return numExpr;
			}
			}
			catch(Exception e){
				e.printStackTrace(System.out);
			}
			} 
			else
				throw new IllegalArgumentException();
		}
		else
			throw new IllegalArgumentException();
		return null;
	}
	private static Expr abstractToNat(CapturedVar capturedVar, AbstractionRule rule, SpecNode specNode) throws IllegalArgumentException{
		// The process of abstraction of a natural varible uses the same logic
		// that the process of abstraction of a variable of integer type
		return abstractToInt(capturedVar, rule,specNode);
	}
	private static Expr abstractToFreeType(CapturedVar capturedVar, AbstractionRule rule, SpecNode specNode) throws IllegalArgumentException{
		// The process of abstraction of a variable of free type uses the same logic
		// that the process of abstraction of a variable of basic type
		return abstractToBasicType(capturedVar, rule,specNode);
	}

	private static Expr abstractToSeq(CapturedVar capturedVar, AbstractionRule rule, SpecNode specNode) throws IllegalArgumentException{
		ZFactory zFactory = new ZFactoryImpl();
		SpecNodeSeq specNodeSeq = (SpecNodeSeq) specNode;
		SpecNode seqType = specNodeSeq.getType();

		// We create the ApplExpr that will contain the information of the sequence
		ApplExpr applExpr =zFactory.createApplExpr();
		applExpr.setMixfix(true);
		// We create the RefExpr for sequences objects
		ZName zNameSeq = zFactory.createZName(UtilSymbols.seqAnglesSymbol(),zFactory.createZStrokeList(), "seq");
		ZExprList zExprList = zFactory.createZExprList(); 
		//Esta funcion hay que programarla
		//zExprList.add(AbstractionUtils.getTypeCZTObject(seqType));
		RefExpr refExprSeq = zFactory.createRefExpr(zNameSeq, zExprList, false, false);
		// We indicate that the applExpr is a sequence
		applExpr.setLeftExpr(refExprSeq);

		if(capturedVar instanceof CapturedVarArray){
			CapturedVarArray varArray = (CapturedVarArray) capturedVar;
			List<CapturedVar> arrayEntries = varArray.getValue();

			// We create the SetExpr
			SetExpr setExpr = zFactory.createSetExpr();
			ZExprList zListSeq = zFactory.createZExprList();
			// We recover all the entries in the array
			for(int i=0;i<arrayEntries.size();i++){
				CapturedVar arrayEntry = arrayEntries.get(i);
				// We create the position for this sequence entry and add
				// it to the tuple
				ZExprList zListEntry = zFactory.createZExprList();
				ZNumeral zNumeral = zFactory.createZNumeral(BigInteger.valueOf(i+1));
				NumExpr indexExpr = zFactory.createNumExpr(zNumeral);
				zListEntry.add(indexExpr);
				// We send to abstract this array entry and add it to the
				// tuple
				zListEntry.add(abstractSimpleCase(arrayEntry,rule,seqType));
				TupleExpr tuple = zFactory.createTupleExpr(zListEntry);
				zListSeq.add(tuple);
			}
			setExpr.setExprList(zListSeq);
			// We set the content of the sequence
			applExpr.setRightExpr(setExpr);
			return applExpr;
		}
		else if(capturedVar instanceof CapturedVarList){
			// Va a haber que chequear si los tipos de la lista y el array concuerdan con los de la secuencia en cantidad de parametros y demas
			CapturedVarList varList = (CapturedVarList) capturedVar;
			// We obtain the elements in the list
			List<List<CapturedField>> elements = varList.getValue();

			// We create the SetExpr
			SetExpr setExpr = zFactory.createSetExpr();
			ZExprList zListSeq = zFactory.createZExprList();
			for(int i=0;i<elements.size();i++){
				ZExprList zListEntry = zFactory.createZExprList();
				// We create the position of the sequence
				ZNumeral zNumeral = zFactory.createZNumeral(BigInteger.valueOf(i+1));
				NumExpr indexExpr = zFactory.createNumExpr(zNumeral);
				zListEntry.add(indexExpr);
				// We obtain the fields in the list
				List<CapturedField> element = elements.get(i);
				boolean isCartesian = false;
				if(element.size()>1)
					isCartesian = true;

				// We always assume that the list has at least one field
				if(isCartesian){ // Is a Tuple
				CapturedVarStructure auxVarStruct=new CapturedVarStructure();
				auxVarStruct.setValue(element);
				zListEntry.add(abstractSimpleCase(auxVarStruct,rule,seqType));
				} 
				else{
				for(int j=0;j<element.size();j++){ // 1 solo elemento
				CapturedField  capturedField = element.get(j);
				CapturedVar field = capturedField.getCapturedVar();
				zListEntry.add(abstractSimpleCase(field,rule,seqType));
				}
				}
				TupleExpr tuple = zFactory.createTupleExpr(zListEntry);
				zListSeq.add(tuple);
			}
			setExpr.setExprList(zListSeq);
			// We set the content of the sequence
			applExpr.setRightExpr(setExpr);
			return applExpr;
		}
		else if(capturedVar instanceof CapturedVarDB){
			// We create the SetExpr
			SetExpr setExpr = zFactory.createSetExpr();
			ZExprList zListExpr = zFactory.createZExprList();

			// We assume that a variable in the spec is refined to a database
			// iff has at least two fields - Lo puedo extender!!!
			if(seqType instanceof SpecNodeCartesianProd){
			SpecNodeCartesianProd pType = (SpecNodeCartesianProd) seqType;
			List<SpecNode> types = pType.getTypes();

			CapturedVarDB capturedVarDB = (CapturedVarDB) capturedVar;
			Map<String,List<String>> dbContent = capturedVarDB.getValue();
			Set<Map.Entry<String, List<String>>> set = dbContent.entrySet();
			Iterator<Map.Entry<String, List<String>>> iterator = set.iterator();
			// We define a new list for easy manipulation of data
			List<List<String>> values = new ArrayList<List<String>>();
			int dbCard = 0;
			while(iterator.hasNext()){
			Map.Entry<String, List<String>> mapEntry = iterator.next();
			List<String> value = mapEntry.getValue();
			if(value.size()>dbCard)
				dbCard = value.size();
			values.add(value);
			}
			// Mismatch cardinalities of the fields in the db and the types
			// in the cartesian product 
			if(types.size()!=values.size())
				throw new IllegalArgumentException();
			//We assume that all the fields in the db are filled

			int index = 0;
			while(index < dbCard){
			ZExprList zListTup = zFactory.createZExprList();
			ZExprList zListEntry = zFactory.createZExprList();
			ZNumeral zNumeral = zFactory.createZNumeral(BigInteger.valueOf(index+1));
			NumExpr indexExpr = zFactory.createNumExpr(zNumeral);
			zListEntry.add(indexExpr);
			for(int i=0;i<values.size();i++){
				List<String> column = values.get(i);
				// Posibles problemas en el chequeo
				// Chequear el uso de enteros!!!
				ZName zNameString = zFactory.createZName(column.get(index),zFactory.createZStrokeList(), "str");
				RefExpr refExprStr = zFactory.createRefExpr(zNameString, zFactory.createZExprList(), false, false);
				zListTup.add(refExprStr);
			}
			TupleExpr tupleElement = zFactory.createTupleExpr(zListTup);
			zListEntry.add(tupleElement);
			TupleExpr tupleSeq = zFactory.createTupleExpr(zListEntry);
			zListExpr.add(tupleSeq);
			index++;
			}
			setExpr.setExprList(zListExpr);
			return setExpr;
			}
			else
				throw new IllegalArgumentException();
		}
		else if(capturedVar instanceof CapturedVarFile){
			// We create the SetExpr
			SetExpr setExpr = zFactory.createSetExpr();
			ZExprList zListExpr = zFactory.createZExprList();

			// We assume that a variable in the spec is refined to a file
			// iff has at least two fields - Lo puedo extender!!!
			if(seqType instanceof SpecNodeCartesianProd){
			SpecNodeCartesianProd pType = (SpecNodeCartesianProd) seqType;
			List<SpecNode> types = pType.getTypes();

			CapturedVarFile varFile = (CapturedVarFile) capturedVar;
			List<List<String>> values = obtainFileValues(varFile);

			for(int i=0;i<values.size();i++){
				List<String> lineValues = values.get(i);
				ZExprList zListTup = zFactory.createZExprList();
				ZExprList zListEntry = zFactory.createZExprList();

				ZNumeral zNumeral = zFactory.createZNumeral(BigInteger.valueOf(i+1));
				NumExpr indexExpr = zFactory.createNumExpr(zNumeral);
				zListEntry.add(indexExpr);

				for(int j=0;j<lineValues.size();j++){
					String field = lineValues.get(j);
					ZName zNameString = zFactory.createZName(field,zFactory.createZStrokeList(), "str");
					RefExpr refExprStr = zFactory.createRefExpr(zNameString, zFactory.createZExprList(), false, false);
					zListTup.add(refExprStr);
				}
				TupleExpr tupleEntry = zFactory.createTupleExpr(zListTup);
				zListEntry.add(tupleEntry);
				TupleExpr tuple = zFactory.createTupleExpr(zListEntry);
				zListExpr.add(tuple);
			}

			setExpr.setExprList(zListExpr);
			return setExpr;
			}
			else
				throw new IllegalArgumentException();
		}
		else if(capturedVar instanceof CapturedVarScreen){
			// We create the SetExpr
			SetExpr setExpr = zFactory.createSetExpr();
			ZExprList zListExpr = zFactory.createZExprList();
			CapturedVarScreen varScreen = (CapturedVarScreen) capturedVar;
			String screenType = varScreen.getScreenType();
			String screenPath = varScreen.getScreenPath();
			Map<String,String> enumElements = varScreen.getEnumElements();
			// We assume that a variable in the spec is refined to a screen table
			// iff has at least two fields - Lo puedo extender!!!
			if(seqType instanceof SpecNodeCartesianProd){
			if(screenType.equals("table")){
			List<List<String>> values = obtainScreenTableValues(varScreen);

			for(int i=0;i<values.size();i++){
				List<String> lineValues = values.get(i);
				ZExprList zListTup = zFactory.createZExprList();
				ZExprList zListEntry = zFactory.createZExprList();

				ZNumeral zNumeral = zFactory.createZNumeral(BigInteger.valueOf(i+1));
				NumExpr indexExpr = zFactory.createNumExpr(zNumeral);
				zListEntry.add(indexExpr);

				for(int j=0;j<lineValues.size();j++){
					String field = lineValues.get(j);
					ZName zNameString = zFactory.createZName(field,zFactory.createZStrokeList(), "str");
					RefExpr refExprStr = zFactory.createRefExpr(zNameString, zFactory.createZExprList(), false, false);
					zListTup.add(refExprStr);
				}
				TupleExpr tupleEntry = zFactory.createTupleExpr(zListTup);
				zListEntry.add(tupleEntry);
				TupleExpr tuple = zFactory.createTupleExpr(zListEntry);
				zListExpr.add(tuple);
			}
			} 
			else
				throw new IllegalArgumentException();
			setExpr.setExprList(zListExpr);
			return setExpr;
			}
		}
		else
			throw new IllegalArgumentException();
		return null;
	}
	private static Expr abstractToPowerSet(CapturedVar capturedVar, AbstractionRule rule, SpecNode specNode) throws IllegalArgumentException{
		ZFactory zFactory = new ZFactoryImpl();
		SpecNodePowerSet specNodePowerSet = (SpecNodePowerSet) specNode;
		SpecNode powerType = specNodePowerSet.getType();
		// We analyze the type of the implementation variable
		if(capturedVar instanceof CapturedVarArray){
			CapturedVarArray varArray = (CapturedVarArray) capturedVar;
			List<CapturedVar> arrayEntries = varArray.getValue();
			// We create the SetExpr and fill it
			SetExpr setExpr = zFactory.createSetExpr();
			ZExprList zListExpr = zFactory.createZExprList();
			for(int i=0;i<arrayEntries.size();i++){
				CapturedVar arrayEntry = arrayEntries.get(i);
				Expr auxExpr = abstractSimpleCase(arrayEntry,rule,powerType);
				if(auxExpr!=null)
					zListExpr.add(auxExpr);
			}
			setExpr.setExprList(zListExpr);
			return setExpr;
		}
		else if(capturedVar instanceof CapturedVarList){
			CapturedVarList varList = (CapturedVarList) capturedVar;
			// We obtain the elements in the list
			//List<Map<String,CapturedVar>> elements = varList.getValue();
			List<List<CapturedField>> elements = varList.getValue();
			// We create the SetExpr
			SetExpr setExpr = zFactory.createSetExpr();
			ZExprList zListExpr = zFactory.createZExprList();

			for(int i=0;i<elements.size();i++){
				// We obtain the fields in the list
				// We always assume that the list has at least one field
				List<CapturedField> element = elements.get(i);
				boolean isCartesian = false;
				if(element.size()>1)
					isCartesian = true;
				if(isCartesian){ // Is a Tuple
				CapturedVarStructure auxVarStruct=new CapturedVarStructure();
				auxVarStruct.setValue(element);
				zListExpr.add(abstractSimpleCase(auxVarStruct,rule,powerType));
				} 
				else{
				for(int j=0;j<element.size();j++){ // 1 solo elemento
				// El problema se resolvera cuando resuelva el de campos de
				// una var de la implementacion y el tipo de la secuencia
				CapturedField  capturedField = element.get(j);
				CapturedVar field = capturedField.getCapturedVar();
				zListExpr.add(abstractSimpleCase(field,rule,powerType));
				}
				}
			}
			setExpr.setExprList(zListExpr);
			return setExpr;
		}
		else if(capturedVar instanceof CapturedVarDB){
			// Analizar detenidamente los casos de las relaciones binarias!!!
			// We assume that a variable in the spec is refined to a database
			// iff has at least two fields.
			if(powerType instanceof SpecNodeCartesianProd){
			SpecNodeCartesianProd pType = (SpecNodeCartesianProd) powerType;
			List<SpecNode> types = pType.getTypes();

			CapturedVarDB capturedVarDB = (CapturedVarDB) capturedVar;
			Map<String,List<String>> dbContent = capturedVarDB.getValue();
			Set<Map.Entry<String, List<String>>> set = dbContent.entrySet();
			Iterator<Map.Entry<String, List<String>>> iterator = set.iterator();
			// We define a new list for easy manipulation of data
			List<List<String>> values = new ArrayList<List<String>>();
			int dbCard = 0;
			while(iterator.hasNext()){
			Map.Entry<String, List<String>> mapEntry = iterator.next();
			List<String> value = mapEntry.getValue();
			if(value.size()>dbCard)
				dbCard = value.size();
			values.add(value);
			}
			// Mismatch cardinalities of the fields in the db and the types
			// in the cartesian product 
			if(types.size()!=values.size())
				throw new IllegalArgumentException();
			//We assume that all the fields in the db are filled

			// We create the SetExpr
			SetExpr setExpr = zFactory.createSetExpr();
			ZExprList zListExpr = zFactory.createZExprList();

			int index = 0;
			while(index < dbCard){
			ZExprList zListTup = zFactory.createZExprList();
			for(int i=0;i<values.size();i++){
				List<String> column = values.get(i);
				// Posibles problemas en el chequeo
				// Chequear el uso de enteros!!!
				ZName zNameString = zFactory.createZName(column.get(index),zFactory.createZStrokeList(), "str");
				RefExpr refExprStr = zFactory.createRefExpr(zNameString, zFactory.createZExprList(), false, false);
				zListTup.add(refExprStr);
			}
			TupleExpr tuple = zFactory.createTupleExpr(zListTup);
			zListExpr.add(tuple);
			index++;
			}
			setExpr.setExprList(zListExpr);
			return setExpr;
			}
			else
				throw new IllegalArgumentException();
		}
		else if(capturedVar instanceof CapturedVarFile){
			CapturedVarFile varFile = (CapturedVarFile) capturedVar;
			// We assume that a variable in the spec is refined to a file
			// iff has at least two fields.
			if(powerType instanceof SpecNodeCartesianProd){
			SpecNodeCartesianProd pType = (SpecNodeCartesianProd) powerType;

			List<List<String>> values = obtainFileValues(varFile);

			// We create the SetExpr
			SetExpr setExpr = zFactory.createSetExpr();
			ZExprList zListExpr = zFactory.createZExprList();

			for(int i=0;i<values.size();i++){
				List<String> lineValues = values.get(i);
				ZExprList zListTup = zFactory.createZExprList();
				for(int j=0;j<lineValues.size();j++){
					String field = lineValues.get(j);
					ZName zNameString = zFactory.createZName(field,zFactory.createZStrokeList(), "str");
					RefExpr refExprStr = zFactory.createRefExpr(zNameString, zFactory.createZExprList(), false, false);
					zListTup.add(refExprStr);
				}
				TupleExpr tuple = zFactory.createTupleExpr(zListTup);
				zListExpr.add(tuple);
			}

			setExpr.setExprList(zListExpr);
			return setExpr;
			}
			else
				throw new IllegalArgumentException();
		}
		else if(capturedVar instanceof CapturedVarScreen){
			// We create the SetExpr
			SetExpr setExpr = zFactory.createSetExpr();
			ZExprList zListExpr = zFactory.createZExprList();
			CapturedVarScreen varScreen = (CapturedVarScreen) capturedVar;
			String screenType = varScreen.getScreenType();
			String screenPath = varScreen.getScreenPath();
			Map<String,String> enumElements = varScreen.getEnumElements();
			// We assume that a variable in the spec is refined to a screen table
			// iff has at least two fields - Lo puedo extender!!!
			if(powerType instanceof SpecNodeCartesianProd){
			if(screenType.equals("table")){
			List<List<String>> values = obtainScreenTableValues(varScreen);

			for(int i=0;i<values.size();i++){
				List<String> lineValues = values.get(i);
				ZExprList zListTup = zFactory.createZExprList();
				for(int j=0;j<lineValues.size();j++){
					String field = lineValues.get(j);
					ZName zNameString = zFactory.createZName(field,zFactory.createZStrokeList(), "str");
					RefExpr refExprStr = zFactory.createRefExpr(zNameString, zFactory.createZExprList(), false, false);
					zListTup.add(refExprStr);
				}
				TupleExpr tuple = zFactory.createTupleExpr(zListTup);
				zListExpr.add(tuple);
			}

			setExpr.setExprList(zListExpr);
			return setExpr;
			
			}
			}
		}
		else
			throw new IllegalArgumentException();
		return null;
	}
	private static Expr abstractToCartesianProd(CapturedVar capturedVar, AbstractionRule rule, SpecNode specNode) throws IllegalArgumentException{
		ZFactory zFactory = new ZFactoryImpl();
		SpecNodeCartesianProd specNodeCartesianProd = (SpecNodeCartesianProd) specNode;
		List<SpecNode> prodTypes = specNodeCartesianProd.getTypes();
		if(capturedVar instanceof CapturedVarStructure){
			CapturedVarStructure varStructure=(CapturedVarStructure) capturedVar;
			//Map<String,CapturedVar> value = varStructure.getValue();
			List<CapturedField> value = varStructure.getValue();
			//Set<Map.Entry<String, CapturedVar>> set = value.entrySet();
			// Incluir mensaje de error
			//if(set.size()!=prodTypes.size())
			//	throw new IllegalArgumentException();
			if(value.size()!=prodTypes.size())
				throw new IllegalArgumentException();
			//Iterator<Map.Entry<String, CapturedVar>> iterator = set.iterator();
			ZExprList zListTup = zFactory.createZExprList();
			//int i = 0;
			//while(iterator.hasNext()){
			for(int i=0;i<value.size();i++){
				//Map.Entry<String, CapturedVar> mapEntry = iterator.next();
				//CapturedVar field = mapEntry.getValue();
				CapturedField capturedField = value.get(i);
				CapturedVar field = capturedField.getCapturedVar();
				zListTup.add(abstractSimpleCase(field,rule,prodTypes.get(i)));
				//i++;
			}
			TupleExpr tuple = zFactory.createTupleExpr(zListTup);
			return tuple;
		}
		else if(capturedVar instanceof CapturedVarScreen){
			ZExprList zListExpr = zFactory.createZExprList();
			CapturedVarScreen varScreen = (CapturedVarScreen) capturedVar;
			String screenType = varScreen.getScreenType();
			String screenPath = varScreen.getScreenPath();
			Map<String,String> enumElements = varScreen.getEnumElements();

			if(screenType.equals("table")){
			List<List<String>> values = obtainScreenTableValues(varScreen);

			for(int i=0;i<values.size();i++){
				List<String> lineValues = values.get(i);
				for(int j=0;j<lineValues.size();j++){
					String field = lineValues.get(j);
					ZName zNameString = zFactory.createZName(field,zFactory.createZStrokeList(), "str");
					RefExpr refExprStr = zFactory.createRefExpr(zNameString, zFactory.createZExprList(), false, false);
					zListExpr.add(refExprStr);
				}
			}


			TupleExpr tuple = zFactory.createTupleExpr(zListExpr);
			return tuple;
			}
			else
				throw new IllegalArgumentException();
		}
		else
			throw new IllegalArgumentException();
	}
	private static Expr abstractToBinRelation(CapturedVar capturedVar, AbstractionRule rule, SpecNode specNode) throws IllegalArgumentException{
		ZFactory zFactory = new ZFactoryImpl();
		SpecNodeBinRelation nodeRel = (SpecNodeBinRelation) specNode;
		SpecNode ranType = nodeRel.getDomType();
		SpecNode domType = nodeRel.getRanType();
		if(capturedVar instanceof CapturedVarList){
			CapturedVarList varList = (CapturedVarList) capturedVar;
			List<List<CapturedField>> elements = varList.getValue();
			// We create the SetExpr
			SetExpr setExpr = zFactory.createSetExpr();
			ZExprList zListExpr = zFactory.createZExprList();
			for(int i=0;i<elements.size();i++){
			ZExprList zListElement = zFactory.createZExprList();
			List<CapturedField> element = elements.get(i);
			if(element.size()!=2) // Limitacion a una variable!!!
				throw new IllegalArgumentException();

			CapturedField capturedField = element.get(0);
			CapturedVar field = capturedField.getCapturedVar();
			Expr domExpr = abstractSimpleCase(field, rule, domType);
			zListElement.add(domExpr);

			capturedField = element.get(1);
			field = capturedField.getCapturedVar();
			Expr ranExpr = abstractSimpleCase(field, rule, ranType);
			zListElement.add(ranExpr);

			TupleExpr tupleElement = zFactory.createTupleExpr(zListElement);
			zListExpr.add(tupleElement);
			}
			setExpr.setExprList(zListExpr);
			return setExpr;
		}
		else if(capturedVar instanceof CapturedVarArray){
			CapturedVarArray varArray = (CapturedVarArray) capturedVar;
			List<CapturedVar> arrayEntries = varArray.getValue();
			SetExpr setExpr = zFactory.createSetExpr();
			ZExprList zListExpr = zFactory.createZExprList();
			for(int i=0;i<arrayEntries.size();i++){
			ZExprList zListElement = zFactory.createZExprList();
			CapturedVar entry = arrayEntries.get(i);
			if(entry instanceof CapturedVarStructure){
			CapturedVarStructure entryStruct = (CapturedVarStructure) entry;

			List<CapturedField> structValue = entryStruct.getValue();

			if(structValue.size()!=2) // Limitacion a una variable!!!
				throw new IllegalArgumentException();

			CapturedField capturedField = structValue.get(0);
			CapturedVar field = capturedField.getCapturedVar();
			Expr domExpr = abstractSimpleCase(field, rule, domType);
			zListElement.add(domExpr);

			capturedField = structValue.get(1);
			field = capturedField.getCapturedVar();
			Expr ranExpr = abstractSimpleCase(field, rule, ranType);
			zListElement.add(ranExpr);

			TupleExpr tupleElement = zFactory.createTupleExpr(zListElement);
			zListExpr.add(tupleElement);
			}
			else
				throw new IllegalArgumentException();
			}
			setExpr.setExprList(zListExpr);
			return setExpr;
		}
		else if(capturedVar instanceof CapturedVarDB){
			CapturedVarDB varDB = (CapturedVarDB) capturedVar;


			Map<String,List<String>> dbContent = varDB.getValue();
			Set<Map.Entry<String, List<String>>> set = dbContent.entrySet();
			Iterator<Map.Entry<String, List<String>>> iterator = set.iterator();
			// We define a new list for easy manipulation of data
			List<List<String>> values = new ArrayList<List<String>>();
			int dbCard = 0;
			while(iterator.hasNext()){
			Map.Entry<String, List<String>> mapEntry = iterator.next();
			List<String> value = mapEntry.getValue();
			if(value.size()>dbCard)
				dbCard = value.size();
			values.add(value);
			}
			// Mismatch cardinalities of the fields in the db and the types
			// in the binary relation
			if(values.size()!=2)
				throw new IllegalArgumentException();
			//We assume that all the fields in the db are filled

			// We create the SetExpr
			SetExpr setExpr = zFactory.createSetExpr();
			ZExprList zListExpr = zFactory.createZExprList();

			int index = 0;
			while(index < dbCard){
			ZExprList zListTup = zFactory.createZExprList();
			for(int i=0;i<values.size();i++){
				List<String> column = values.get(i);
				// Posibles problemas en el chequeo
				// Chequear el uso de enteros!!!
				ZName zNameString = zFactory.createZName(column.get(index),zFactory.createZStrokeList(), "str");
				RefExpr refExprStr = zFactory.createRefExpr(zNameString, zFactory.createZExprList(), false, false);
				zListTup.add(refExprStr);
			}
			TupleExpr tuple = zFactory.createTupleExpr(zListTup);
			zListExpr.add(tuple);
			index++;
			}
			setExpr.setExprList(zListExpr);
			return setExpr;
		}
		else if(capturedVar instanceof CapturedVarFile){
			CapturedVarFile varFile = (CapturedVarFile) capturedVar;
			List<List<String>> values = obtainFileValues(varFile);

			// We create the SetExpr
			SetExpr setExpr = zFactory.createSetExpr();
			ZExprList zListExpr = zFactory.createZExprList();

			for(int i=0;i<values.size();i++){
				List<String> lineValues = values.get(i);
				ZExprList zListTup = zFactory.createZExprList();
				for(int j=0;j<lineValues.size();j++){
					String field = lineValues.get(j);
					ZName zNameString = zFactory.createZName(field,zFactory.createZStrokeList(), "str");
					RefExpr refExprStr = zFactory.createRefExpr(zNameString, zFactory.createZExprList(), false, false);
					zListTup.add(refExprStr);
				}
				TupleExpr tuple = zFactory.createTupleExpr(zListTup);
				zListExpr.add(tuple);
			}

			setExpr.setExprList(zListExpr);
			return setExpr;
		}
		else if(capturedVar instanceof CapturedVarScreen){
			// We create the SetExpr
			SetExpr setExpr = zFactory.createSetExpr();
			ZExprList zListExpr = zFactory.createZExprList();
			CapturedVarScreen varScreen = (CapturedVarScreen) capturedVar;
			String screenType = varScreen.getScreenType();
			String screenPath = varScreen.getScreenPath();
			Map<String,String> enumElements = varScreen.getEnumElements();

			if(screenType.equals("table")){
			String delimiter = varScreen.getDelimiter();
			int rowLB = varScreen.getRowLowerBound();
			int rowUB = varScreen.getRowUpperBound();
			int colLB = varScreen.getColumnLowerBound();
			int colUB = varScreen.getColumnUpperBound();
			try{
			URL url = VarAbstraction.class.getResource("VarAbstraction.class");
			String urlStr = url.toString();
			String currentDir = urlStr.substring(9,urlStr.indexOf("fastest.jar"));
			BufferedReader in = new BufferedReader(new FileReader(screenPath));
			String line;
			int row = 0;

			while((line = in.readLine())!= null){
				row++;
				int column = 0;
				ZExprList zListTup = zFactory.createZExprList();
				int index = 0;
				boolean isInRange = false;
				while(index>-1){
				column++;
				String field = line.substring(0,line.indexOf(delimiter));
				line = line.substring(line.indexOf(delimiter)+1);
				if(rowLB<=row && row<=rowUB && colLB<=column && column<=colUB){
				isInRange = true;
				ZName zNameString = zFactory.createZName(field,zFactory.createZStrokeList(), "str");
				RefExpr refExprStr = zFactory.createRefExpr(zNameString, zFactory.createZExprList(), false, false);
				zListTup.add(refExprStr);
				}
				index = line.indexOf(delimiter);
				}
				column++;
				if(rowLB<=row && row<=rowUB && colLB<=column && column<=colUB){
				isInRange = true;
				String field = line;
				ZName zNameString = zFactory.createZName(field,zFactory.createZStrokeList(), "str");
				RefExpr refExprStr = zFactory.createRefExpr(zNameString, zFactory.createZExprList(), false, false);
				zListTup.add(refExprStr);
				}
				if(isInRange){
				TupleExpr tuple = zFactory.createTupleExpr(zListTup);
				zListExpr.add(tuple);
				}
			}
			setExpr.setExprList(zListExpr);
			return setExpr;
			}
			catch(Exception e){
				e.printStackTrace(System.out);
			}
			}
		}
		else
			throw new IllegalArgumentException();
		return null;
	}

	private static List<List<String>> obtainFileValues(CapturedVarFile varFile){
		List<List<String>> values = new ArrayList<List<String>>();

		String eol = varFile.getEol();
		String eof = varFile.getEof();
		String delimiter = varFile.getDelimiter();
		String structure = varFile.getStructure();
		String fileName = varFile.getFileName();
		String path = varFile.getPath();

		try{
			// Por ahora voy a ignorar los eol y eof
			// Falta un chequeo de tipos chiquito aca!!!
			BufferedReader in = new BufferedReader(new FileReader(path+fileName));
			String line;
			while((line = in.readLine())!= null){
				line = line.trim();
				if(!line.equals("")){
				List<String> lineValues = new ArrayList<String>();
				int index = 0;
				while(index>-1){
				String field = "";
				if(line.contains(delimiter)){
				field = line.substring(0,line.indexOf(delimiter));
				lineValues.add(field);
				line = line.substring(line.indexOf(delimiter)+1);
				}
				index = line.indexOf(delimiter);
				}

				String field = line;
				lineValues.add(field);
				values.add(lineValues);

				}
			}
		}catch(Exception e){
			e.printStackTrace(System.out);
		}
		return values;
	}
	private static List<List<String>> obtainScreenTableValues(CapturedVarScreen varScreen){
		List<List<String>> values = new ArrayList<List<String>>();

		String screenType = varScreen.getScreenType();
		String screenPath = varScreen.getScreenPath();
		Map<String,String> enumElements = varScreen.getEnumElements();

		if(screenType.equals("table")){
			String delimiter = varScreen.getDelimiter();
			int rowLB = varScreen.getRowLowerBound();
			int rowUB = varScreen.getRowUpperBound();
			int colLB = varScreen.getColumnLowerBound();
			int colUB = varScreen.getColumnUpperBound();
			try{
			BufferedReader inOp = new BufferedReader(new FileReader(screenPath));
			String line;
			int row = 0;
			while((line = inOp.readLine())!= null){
				List<String> lineValues = new ArrayList<String>();
				row++;
				int column = 0;
				if(rowLB<=row && row<=rowUB){

				int index = 0;
				boolean isInRange = false;
				// Esto funciona porque asumimos al menos 2 campos
				while(index>-1){
				column++;
				String field = line.substring(0,line.indexOf(delimiter));
				line = line.substring(line.indexOf(delimiter)+1);
				if(colLB<=column && column<=colUB){
				isInRange = true;
				lineValues.add(field);
				}
				index = line.indexOf(delimiter);
				}
				column++;
				if(colLB<=column && column<=colUB){
				isInRange = true;
				String field = line;
				lineValues.add(field);
				}
				if(isInRange){
					values.add(lineValues);
				}
				}
			}
			}
			catch(Exception e){
				e.printStackTrace(System.out);
			}
		}
		return values; 
	}

}


