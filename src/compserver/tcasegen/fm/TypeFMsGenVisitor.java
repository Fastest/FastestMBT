package compserver.tcasegen.fm;

import java.util.*;
import java.math.*;

import net.sourceforge.czt.z.ast.TupleExpr;
import net.sourceforge.czt.z.ast.NumExpr;
import net.sourceforge.czt.z.ast.PowerExpr;
import net.sourceforge.czt.z.ast.ProdExpr;
import net.sourceforge.czt.z.ast.ApplExpr;
import net.sourceforge.czt.z.ast.ZNumeral;
import net.sourceforge.czt.z.ast.Box;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.SetExpr;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.ast.Name;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.ZNameList;
import net.sourceforge.czt.z.ast.BranchList;
import net.sourceforge.czt.z.ast.Freetype;
import net.sourceforge.czt.z.ast.GivenPara;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.Decl;
import net.sourceforge.czt.z.ast.ConstDecl;
import net.sourceforge.czt.z.ast.DeclList;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.ZSchText;
import net.sourceforge.czt.z.impl.ZBranchListImpl;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.visitor.ExprVisitor;
import net.sourceforge.czt.z.visitor.RefExprVisitor;
import net.sourceforge.czt.z.visitor.ApplExprVisitor;
import net.sourceforge.czt.z.visitor.SetExprVisitor;
import net.sourceforge.czt.z.visitor.ProdExprVisitor;
import net.sourceforge.czt.z.visitor.PowerExprVisitor;
import net.sourceforge.czt.z.visitor.FreetypeVisitor;
import net.sourceforge.czt.z.visitor.GivenParaVisitor;
import net.sourceforge.czt.z.visitor.AxParaVisitor;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.base.visitor.VisitorUtils;
import net.sourceforge.czt.base.visitor.TermVisitor;

import common.z.UtilSymbols;
import common.z.SpecUtils;
import compserver.tcasegen.eval.NormalTypeAndFM;


/**
 * An instance of this class allow the generation of finite models and normalized types for
 * for each type defined globally in the specification. When this functionality is applied,
 * the specification does not result modified, but an instance of MapExpr,NormalTypeAndFM 
 * (which can be obtained by calling the method getExprMap()) in order to get the
 * correspondences between instances of Expr (which denotes types) and instances of 
 * NormalTypeFiniteModel (which denotes pairs with the normalized type as first component and the
 * finite model generator as second one).
 * @author Pablo Rodriguez Monetti
 */
public class TypeFMsGenVisitor implements RefExprVisitor<NormalTypeAndFM>,
	ApplExprVisitor<NormalTypeAndFM>, PowerExprVisitor<NormalTypeAndFM>, 
	ProdExprVisitor<NormalTypeAndFM>, SetExprVisitor<NormalTypeAndFM>,
	ExprVisitor<NormalTypeAndFM>, TermVisitor<NormalTypeAndFM>,
	AxParaVisitor<NormalTypeAndFM>,GivenParaVisitor<NormalTypeAndFM>,
	FreetypeVisitor<NormalTypeAndFM>{

	private Map<Expr,NormalTypeAndFM> exprMap;
	private int fMSize;
	

	public TypeFMsGenVisitor(Map<Expr,NormalTypeAndFM> exprMap, int fMSize){
		if(exprMap == null)
			exprMap = new HashMap<Expr, NormalTypeAndFM>();
		
		this.exprMap = exprMap;
		this.fMSize = fMSize;
	}

	public NormalTypeAndFM visitTerm(Term term){
    	VisitorUtils.visitTerm(this, term);
    	return null;
	}


	public NormalTypeAndFM visitAxPara(AxPara axPara){
		if(axPara.getBox() != Box.OmitBox)
			//Ver que hacer cuando el axPara es una definición axiomática o un esquema vertical
			return null;
		// Si es una definicion horizaontal...
		// Que para este momento solo pueden ser abreviaturas
		// Pues se supone que ya se expandieron los esquemas
		ZSchText zSchText = axPara.getZSchText();
		DeclList declList = zSchText.getDeclList();
 

		NormalTypeAndFM fMPair = null;
		if(declList instanceof ZDeclList){
			ZDeclList zDeclList = (ZDeclList) declList;
			for(int j=0; j< zDeclList.size(); j++){
				Decl decl = zDeclList.get(j);
				if(decl instanceof ConstDecl){
					ConstDecl constDecl = (ConstDecl) decl;
					ZName zName = constDecl.getZName();
					String schemaName = zName.getWord();
					Expr expr = constDecl.getExpr();
					fMPair = expr.accept(new TypeFMsGenVisitor(exprMap, fMSize));
					// We create the RefExpr
					ZFactory zFactory = new ZFactoryImpl();
					ZExprList zExprList = zFactory.createZExprList(); 
					RefExpr refExpr = zFactory.createRefExpr(zName, zExprList, false, false);
					//We add the mappings
//					System.out.println(setExpr.getZExprList().size());
					exprMap.put(refExpr, fMPair);
				}
			}
		}
		return fMPair;	
	}

	public NormalTypeAndFM visitGivenPara(GivenPara givenPara){

		ZNameList zNameList = givenPara.getZNameList();
		ZFactory zFactory = new ZFactoryImpl();
		NormalTypeAndFM fMPair = null;
		for(int i=0; i<zNameList.size();i++){
			// We create the RefExpr for the ith ZName
			ZExprList zExprList = zFactory.createZExprList(); 
			Name name = zNameList.get(i);
			RefExpr refExpr = zFactory.createRefExpr(name, zExprList, false, false);
			
			// We create the SetExpr 
			String lowerZNameString = name.toString().toLowerCase();
			ZExprList setZExprList = zFactory.createZExprList();
			for(int j=0; j<fMSize; j++){
				String constantName = lowerZNameString + j;
				ZName lowerZName = zFactory.createZName(constantName, zFactory.createZStrokeList(), "lower");
				ZExprList refZExprList = zFactory.createZExprList(); 
				RefExpr constantRefExpr = zFactory.createRefExpr(lowerZName, refZExprList, false, false);
				setZExprList.add(j,constantRefExpr);
			}
			SetExpr setExpr = zFactory.createSetExpr(setZExprList);

			fMPair = new NormalTypeAndFM(setExpr, SpecUtils.getNumRefExpr());

			//We add the mapping
			exprMap.put(refExpr, fMPair);
		}
		return fMPair;
	}


	public NormalTypeAndFM visitFreetype(Freetype freetype){
		ZName freeTypeZName = freetype.getZName();
		ZFactory zFactory = new ZFactoryImpl();

		// We create the RefExpr for the ZName
		ZExprList zExprList = zFactory.createZExprList(); 
		RefExpr refExpr = zFactory.createRefExpr(freeTypeZName, zExprList, false, false);

		NormalTypeAndFM fMPair = null;
		BranchList branchList = freetype.getBranchList();
		if(branchList instanceof ZBranchListImpl){
			// We create the SetExpr 
			ZBranchListImpl zBranchListImpl = (ZBranchListImpl) branchList;
			ZExprList setZExprList = zFactory.createZExprList();
			for(int j=0; j<zBranchListImpl.size(); j++){
				Name name = zBranchListImpl.get(j).getName();
				ZExprList refZExprList = zFactory.createZExprList(); 
				RefExpr branchRefExpr = zFactory.createRefExpr(name, refZExprList, false, false);
				setZExprList.add(j,branchRefExpr);
			}
			SetExpr setExpr = zFactory.createSetExpr(setZExprList);
			
			fMPair = new NormalTypeAndFM(setExpr, SpecUtils.getNumRefExpr());
			//We add the mapping
			exprMap.put(refExpr, fMPair);
		}

		return fMPair;
	}








	public NormalTypeAndFM visitRefExpr(RefExpr refExpr){
		String refExprName = refExpr.getZName().getWord().toString();
		//Guarda que el ultimo elemento de codePint hace ref al conjunto vacio

		ZFactory zFactory = new ZFactoryImpl();
		ZExprList setZExprList = zFactory.createZExprList();

		NormalTypeAndFM fMPair = null;

		if(refExprName.equals(UtilSymbols.integerSymbol())){
			//La refExpr denota a los enteros
			for(int i=0; i<fMSize; i++){
				int number = i -fMSize/2;
				int abs = Math.abs(number);
				ZNumeral zNumeral = zFactory.createZNumeral(BigInteger.valueOf(abs));
				NumExpr numExpr = zFactory.createNumExpr(zNumeral);
				if(number >= 0)
					setZExprList.add(numExpr);
				else{
					ZName zName = zFactory.createZName("- _ ", zFactory.createZStrokeList(), "neg");
					RefExpr negRefExpr = zFactory.createRefExpr(zName, zFactory.createZExprList(), false, false);
					ZExprList zExprList = zFactory.createZExprList();
					zExprList.add(negRefExpr);
					zExprList.add(numExpr);
					ApplExpr applExpr = zFactory.createApplExpr(zExprList, true);
					setZExprList.add(applExpr);
				}
			}

			fMPair = new NormalTypeAndFM(zFactory.createSetExpr(setZExprList), SpecUtils.getNumRefExpr());
			//We add the mapping
			exprMap.put(refExpr, fMPair);
		}
		else if(refExprName.equals(UtilSymbols.naturalSymbol())){
			//La refExpr denota a los naturales
			for(int i=0; i<fMSize; i++){
				ZNumeral zNumeral = zFactory.createZNumeral(BigInteger.valueOf(i));
				NumExpr numExpr = zFactory.createNumExpr(zNumeral);
				setZExprList.add(numExpr);
			}
			fMPair = new NormalTypeAndFM(zFactory.createSetExpr(setZExprList), SpecUtils.getNumRefExpr());
			//We add the mapping
			exprMap.put(refExpr, fMPair);
		}
		else if(refExprName.equals(UtilSymbols.relationSymbol())){
			//La refExpr denota al conjunto de relaciones entre dos conjuntos A y B
			ZExprList refZExprList = refExpr.getZExprList();

			//El <-> es un operador binario entre A y B, aunque A<->B puede denotar A'<->A''<->B si A = A'<->A''
			//  o puede denotar A<->B'<->B'' si B = B'<->B'' 
			if(refZExprList.size() != 2)
				return null;
			// Como A<->B = P(AxB) buscamos el modelo finito/tipo normalizado de P(AxB)
			ProdExpr prodExpr = zFactory.createProdExpr(refZExprList);
			PowerExpr powerExpr = zFactory.createPowerExpr(prodExpr);
			fMPair = powerExpr.accept(this);
			exprMap.put(refExpr, fMPair);

		}
		else if(refExprName.equals(UtilSymbols.partialFunctionSymbol())){
			//La refExpr denota a las funciones parciales
			ZExprList refZExprList = refExpr.getZExprList();
			//El -> es un operador binario entre A y B, aunque A->B puede denotar A'->A''->B si A = A'->A''
			//  o puede denotar A->B'->B'' si B = B'->B'' 
			if(refZExprList.size() != 2)
				return null;

			NormalTypeAndFM fMPairA = getFMPair(refZExprList.get(0));
			NormalTypeAndFM fMPairB = getFMPair(refZExprList.get(1));	
			
			//Obtenemos el tipo normalizado del conjunto de funciones parciales de A en B.
			ZName zName = zFactory.createZName(UtilSymbols.partialFunctionSymbol(), zFactory.createZStrokeList(), "pfunc");
			ZExprList refZExprL = zFactory.createZExprList(); 
			refZExprL.add(fMPairA.getNormalType());
			refZExprL.add(fMPairB.getNormalType());
			RefExpr typeRefExpr = zFactory.createRefExpr(zName, refZExprL, false, false);

			ZExprList zExprListA = fMPairA.getFM().getZExprList();
			ZExprList zExprListB = fMPairB.getFM().getZExprList();

			int n = zExprListA.size();
			int m = zExprListB.size();
			
			for(int k=0; k< Math.pow(2,n); k++){
				ZExprList auxZExprList = zFactory.createZExprList();
				int aux = k;
				for(int j=0; j<n; j++)
					if((aux & 1<<j) != 0)
						auxZExprList.add(zExprListA.get(j));
				int auxSize = auxZExprList.size();
				if(auxSize == 0){
					//Agregamos al conjunto vacio con el tipo explicito
					zName = zFactory.createZName(UtilSymbols.emptySetSymbol(), zFactory.createZStrokeList(), "pfunc");
					refZExprL = zFactory.createZExprList(); 
					refZExprL.add(typeRefExpr);
					RefExpr emptySetRefExpr = zFactory.createRefExpr(zName, refZExprL, false, false);
					setZExprList.add(emptySetRefExpr);
				}
				else{
					int[] indexes = new int[auxSize];
					for(int i=0; i<auxSize; i++)
						indexes[i]=0;
					
					for(int i=0; i< Math.pow(m, auxSize); i++){
						ZExprList funcZExprList = zFactory.createZExprList();
						for(int j=0; j<auxSize; j++){
							ZExprList tupleZExprList = zFactory.createZExprList();
							tupleZExprList.add(auxZExprList.get(j));
							tupleZExprList.add(zExprListB.get(indexes[j]));
							funcZExprList.add(zFactory.createTupleExpr(tupleZExprList));
						}
						setZExprList.add(zFactory.createSetExpr(funcZExprList));
						indexes[0]++;
						for(int j=0; j< auxSize -1; j++){
							if(indexes[j]==m){
								indexes[j]=0;
								indexes[j+1]++;	
							}
						}
					}
				}
			}

			fMPair = new NormalTypeAndFM(zFactory.createSetExpr(setZExprList), typeRefExpr);
			exprMap.put(refExpr, fMPair);
		}
		else if(refExprName.equals(UtilSymbols.totalFunctionSymbol())){
			//La refExpr denota a las funciones totales
			ZExprList refZExprList = refExpr.getZExprList();
			//El -> es un operador binario entre A y B, aunque A->B puede denotar A'->A''->B si A = A'->A''
			//  o puede denotar A->B'->B'' si B = B'->B'' 
			if(refZExprList.size() != 2)
				return null;

			NormalTypeAndFM fMPairA = getFMPair(refZExprList.get(0));
			NormalTypeAndFM fMPairB = getFMPair(refZExprList.get(1));	
			
			ZExprList zExprListA = fMPairA.getFM().getZExprList();
			ZExprList zExprListB = fMPairB.getFM().getZExprList();
			
			int n = zExprListA.size();
			int m = zExprListB.size();


			int[] indexes = new int[n];

			for(int i=0; i<n; i++)
				indexes[i] = 0;

			for(int i=0; i< Math.pow(m,n); i++){
				ZExprList funcZExprList = zFactory.createZExprList();
				for(int j=0; j<n; j++){
					ZExprList tupleZExprList = zFactory.createZExprList();
					tupleZExprList.add(zExprListA.get(j));
					tupleZExprList.add(zExprListB.get(indexes[j]));
					funcZExprList.add(zFactory.createTupleExpr(tupleZExprList));
				}
				setZExprList.add(zFactory.createSetExpr(funcZExprList));
				
				indexes[0]++;
				for(int j=0; j< n-1; j++){
					if(indexes[j]==m){
						indexes[j]=0;
						indexes[j+1]++;
					}

				}
			}
			//Obtenemos el tipo normalizado del conjunto de funciones totales de A en B.
			ZName zName = zFactory.createZName(UtilSymbols.totalFunctionSymbol(), zFactory.createZStrokeList(), "func");
			ZExprList refZExprL = zFactory.createZExprList(); 
			refZExprL.add(fMPairA.getNormalType());
			refZExprL.add(fMPairB.getNormalType());
			RefExpr typeRefExpr = zFactory.createRefExpr(zName, refZExprL, false, false);
			fMPair = new NormalTypeAndFM(zFactory.createSetExpr(setZExprList), typeRefExpr);
			exprMap.put(refExpr, fMPair);
		}
		else if(refExprName.equals("seq _ ")){
			//La refExpr denota a las secuencias
		}
		return fMPair;
		
	}

	public NormalTypeAndFM visitApplExpr(ApplExpr applExpr){

		// Generamos el modelo finitos solo si la expresión es de la forma "a .. b" donde a y b son números
		if(!applExpr.getMixfix())
			return null;
		
		Expr leftExpr = applExpr.getLeftExpr();
		if(!(leftExpr instanceof RefExpr))
			return null;
		
		RefExpr refExpr = (RefExpr) leftExpr;
		String refExprName = refExpr.getName().toString();
		if(!refExprName.equals(" _ .. _ "))
			return null;
		
		Expr rightExpr = applExpr.getRightExpr();
		if(!(rightExpr instanceof TupleExpr))
			return null;
		
		TupleExpr tupleExpr = (TupleExpr) rightExpr;
		ZExprList zExprList = tupleExpr.getZExprList();
		if (zExprList.size() !=2)
			return null;

		if(!(zExprList.get(0) instanceof NumExpr))
			return null;

		if(!(zExprList.get(1) instanceof NumExpr))
			return null;

		BigInteger bigInteger0 = ((NumExpr)zExprList.get(0)).getValue();
		double double0 = bigInteger0.doubleValue();
		BigInteger bigInteger1 = ((NumExpr)zExprList.get(1)).getValue();
		double double1 = bigInteger1.doubleValue();


		int difference = (int)(double1 - double0);
		if(difference <= 0)
			return null;
		
		ZFactory zFactory = new ZFactoryImpl();
		ZExprList setZExprList = zFactory.createZExprList();


		if(difference < fMSize){
			//Tomamos los enteros entre double0 y double1
			for(int i=(int)double0; i<(int)double1; i++){
				ZNumeral zNumeral = zFactory.createZNumeral(BigInteger.valueOf(i));
				NumExpr numExpr = zFactory.createNumExpr(zNumeral);
				setZExprList.add(numExpr);
			}
		}
		else{
			//Tomamos fMSize enteros equiespaciados entre double0 y double1
			int offset = difference / (fMSize+1);
			for(int i=(int)(double0 + offset); i<(int)double1; i = i + offset){
				ZNumeral zNumeral = zFactory.createZNumeral(BigInteger.valueOf(i));
				NumExpr numExpr = zFactory.createNumExpr(zNumeral);
				setZExprList.add(numExpr);
			}
		}

		// VER SI SE PUEDE MEJORAR LA CREACION DE LA Expr \\power \\nat para que sea mas eficiente debido a la creacion de un ZLive
		

		NormalTypeAndFM fMPair = new NormalTypeAndFM(zFactory.createSetExpr(setZExprList), SpecUtils.getNumRefExpr());
		exprMap.put(applExpr, fMPair);
		return fMPair;
	}



	public NormalTypeAndFM visitPowerExpr(PowerExpr powerExpr){

		Expr expr = powerExpr.getExpr();
		NormalTypeAndFM fMPair = getFMPair(expr);

		ZExprList zExprList = fMPair.getFM().getZExprList();

		//Si la lista zExprList esta vacia no salimos (como hacemos en visitProdExpr o al visitar una función parcial o total)
		//porque igual queremos agregar el conjunto vacio.
		if(zExprList == null)
			return null;

		ZFactory zFactory = new ZFactoryImpl();
		ZExprList setZExprList = zFactory.createZExprList();

		//Agregamos al conjunto vacio con el tipo explicito

		ZName zName = zFactory.createZName(UtilSymbols.emptySetSymbol(), zFactory.createZStrokeList(), "pfunc");
		
		ZExprList refZExprL = zFactory.createZExprList(); 
		refZExprL.add(fMPair.getNormalType());
		RefExpr emptySetRefExpr = zFactory.createRefExpr(zName, refZExprL, false, false);
		setZExprList.add(emptySetRefExpr);

		int size = zExprList.size();
		int powerSize = (int) Math.pow((double)2,(double)size);


		//EMPEZAMOS EN UNO PARA NO AGREGAR EL CONJUNTO VACIO CON EL TIPO IMPLICITO
		for(int i=1; i<powerSize; i++){
			ZExprList auxZExprList = zFactory.createZExprList();
			int aux = i;
			for(int j=0; j<size; j++)
				if((aux & 1<<j) != 0)
					auxZExprList.add(zExprList.get(j));	
			setZExprList.add(zFactory.createSetExpr(auxZExprList));
		}
		
		fMPair = new NormalTypeAndFM(zFactory.createSetExpr(setZExprList),zFactory.createPowerExpr(fMPair.getNormalType()));
		exprMap.put(powerExpr, fMPair);
		return fMPair;
	}	



	public NormalTypeAndFM visitProdExpr(ProdExpr prodExpr){
		ZFactory zFactory = new ZFactoryImpl(); 
		ZExprList zExprList = prodExpr.getZExprList();
		if(zExprList == null || zExprList.size()==0)
			return null;
		
		ZExprList typeList = zFactory.createZExprList();
		List<ZExprList> zExprListList = new ArrayList<ZExprList>();
		for(int i=0; i < zExprList.size(); i++){
			NormalTypeAndFM fMPair = getFMPair(zExprList.get(i));
			ZExprList auxZExprList = fMPair.getFM().getZExprList();

			if(auxZExprList==null || zExprList.size()==0)
				return null;

			zExprListList.add(auxZExprList);
			typeList.add(fMPair.getNormalType());
		}

		int m = zExprListList.size();

		int product=1;
		//el entero i-esimo de listsSizes indica el tamaño de la i-esima lista de zExprListList
		int[] listsSizes = new int[m];
		//el entero i-esimo de listsSizes indica el indice actual de la i-esima lista de zExprListList
		int[] currentIndex = new int[m];

		//Inicializamos los tamaños de las listas de zExprListList y a 0 los indices actuales 
		for(int i=0; i<m; i++){
			int size = zExprListList.get(i).size();
			product *= size;
			listsSizes[i] = size;
			currentIndex[i] = 0;
		}


		ZExprList setZExprList = zFactory.createZExprList();

		for(int i=0; i<product; i++){
			ZExprList tupleZExprList = zFactory.createZExprList();
			for(int j=0; j<m; j++){
				Expr expr = zExprListList.get(j).get(currentIndex[j]);
				tupleZExprList.add(expr);
			}
			TupleExpr tupleExpr = zFactory.createTupleExpr(tupleZExprList);
			setZExprList.add(tupleExpr);

			currentIndex[0]++;
			for(int j=0; j<m-1; j++){
				if(currentIndex[j] == listsSizes[j]){
					currentIndex[j+1]++;
					currentIndex[j] = 0;
				}
			}
		}
		NormalTypeAndFM fMPair = new NormalTypeAndFM(zFactory.createSetExpr(setZExprList),zFactory.createProdExpr(typeList));
		exprMap.put(prodExpr, fMPair);
		return fMPair;
	}


	public NormalTypeAndFM visitSetExpr(SetExpr setExpr){
		NormalTypeAndFM fMPair = new NormalTypeAndFM(setExpr, null);
		exprMap.put(setExpr, fMPair);
		return fMPair;
	}


	public NormalTypeAndFM visitExpr(Expr expr){
		return null;
	}



	public Map<Expr,NormalTypeAndFM> getExprMap(){
		return exprMap;
	}




	//Devuelve la lista de expresiones del SetExpr del SetExpr asociado al modelo finito de expr
	//buscandolo en mapExpr o generandolo pasandole a expr este visitor
	private NormalTypeAndFM getFMPair(Expr expr){
		NormalTypeAndFM fMPair = exprMap.get(expr);
		if(fMPair == null)
			fMPair =  expr.accept(this);
		return fMPair;
	}


	



}
