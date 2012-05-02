package client.blogic.testing.prunning;

import java.net.InetAddress;
import java.util.*;

import client.blogic.management.ii.events.PrunningResult;
import client.blogic.management.ii.EventAdmin;
import common.z.TClass;
import client.blogic.management.communic.*;
import net.sourceforge.czt.session.SectionManager;
import compserver.prunning.TreePruner;
import compserver.prunning.ResultPrune;
import client.presentation.ClientUI;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import common.z.czt.visitors.CZTReplacer;
import common.z.SpecUtils;
import net.sourceforge.czt.typecheck.z.TypeCheckUtils;
import net.sourceforge.czt.typecheck.z.ErrorAnn;
import net.sourceforge.czt.z.ast.SectTypeEnvAnn;
import net.sourceforge.czt.z.ast.Pred;
import compserver.prunning.typechecking.TypecheckingUtils;
import compserver.prunning.SpecInfo;

/**
 * Instances of this class has the responsibility of request, to a computation 
 * server, for the prune of a test class. Each request must be done through a new instance
 * of ServiceMediator, which is the only one that has the knowledge about how
 * the connection with the servers are implemented.
 */
public class TClassPruneClientRunner implements Runnable
{
	private TClass tClass;
	private SectionManager manager;
	private String sectionName;
	private ServerConfig serverConfig;
	private SpecInfo specInfo;

    /**
     * Creates new instances of TClassPruneClientRunner
     * @param tClass
     */
	public TClassPruneClientRunner(TClass tClass, SectionManager manager, String sectionName, ServerConfig serverConfig, SpecInfo specInfo){
		this.tClass = tClass;
		this.manager = manager;
		this.sectionName = sectionName;
		this.serverConfig = serverConfig;
		this.specInfo = specInfo;
	}

    /**
     * Requests the prune of a test class either to the client 
     * itself or to a computation server server. After a response arrives, it
     * announces a TClassPruned event.
     */
	public void run(){
		String serverName = serverConfig.getServerName();
		InetAddress inetAddress = serverConfig.getInetAddress();
		int port = serverConfig.getPort();

		AxPara axParaAux = tClass.getMyAxPara();


		Pred auxPred = SpecUtils.getAxParaPred(axParaAux);
		if(auxPred==null){
		String tClassName = SpecUtils.getAxParaName(axParaAux);
		PrunningResult prunningResult = new PrunningResult(tClassName, null, null, false, serverConfig);
		try{
		EventAdmin eventAdmin = EventAdmin.getInstance();
		eventAdmin.announceEvent(prunningResult);
		}
		catch(Exception e){
			System.out.println("ExceptioN");
			e.printStackTrace();
		}
		}
		AxPara axPara = TypecheckingUtils.deleteUnnecessaryParenthesis(axParaAux, manager);
		Pred tClassPred = null;
		try{
		// We force the typechecking of tClass to guarantee TypeAnns
		List<? extends ErrorAnn> errors = 
				TypeCheckUtils.typecheck(axPara, manager, false, sectionName);
		if(errors.size() >0)
			System.out.println("ErroreS: "+errors.toString());
		tClassPred = SpecUtils.getAxParaPred(axPara);
		SpecUtils.setAxParaPred(tClass, tClassPred);
		}
		catch(Exception e){
			System.out.println("There was an error in the typechecking stage of the prunning!");
			System.out.println(e);
		}

		/*System.out.println("El predicado antes del reemplazo:\n"+SpecUtils.termToLatex(tClass));
		// We replace in predicate the values for axiomatic definitions
		// Analizar la posibilidad de clonar!
		if (axDefsValues != null) {
	
		Pred pred = SpecUtils.getAxParaPred(tClass);
		Set<Map.Entry<RefExpr, Expr>> set = axDefsValues.entrySet();
		Iterator<Map.Entry<RefExpr, Expr>> iterator = set.iterator();
		CZTReplacer replaceVisitor = new CZTReplacer();
	
		while (iterator.hasNext()) {
			Map.Entry<RefExpr, Expr> mapEntry = iterator.next();
			RefExpr refExpr = mapEntry.getKey();
			Expr expr = mapEntry.getValue();
			replaceVisitor.setOrigTerm(refExpr);
			replaceVisitor.setNewTerm(expr);
			pred = (Pred) pred.accept(replaceVisitor);
		}
		SpecUtils.setAxParaPred(tClass, pred);
		System.out.println("El predicado tras el reemplazo:\n"+SpecUtils.termToLatex(tClass));
		}*/



		ResultPrune deleted = null;
		if(inetAddress.isLoopbackAddress()){
			TreePruner treePruner = new TreePruner();
			deleted = treePruner.pruneTree(tClass,specInfo);
		}
		else{
			// We want ServiceMediator to do the request. The analysis of
            		// the prunning will be realized in a computation server
			deleted = (new ServiceMediator(inetAddress, port)).pruneTree(tClass,specInfo);
		}
		String tClassName = deleted.getTClassName();
		String theoremName = deleted.getTheoremName();
		List<String> params = deleted.getParams();
		boolean result = deleted.getResult();
		PrunningResult prunningResult = new PrunningResult(tClassName, theoremName, params, result, serverConfig);
		try{
		EventAdmin eventAdmin = EventAdmin.getInstance();
		eventAdmin.announceEvent(prunningResult);
		}
		catch(Exception e){
			System.out.println("ExceptioN");
			e.printStackTrace();
		}
	}


}
