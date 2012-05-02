package client.blogic.testing.prunning;

import java.util.*;
import client.blogic.management.ii.events.PruneTClassRequested;
import client.blogic.management.ii.events.UnfoldedSpecLoaded;
import client.blogic.management.ii.events.Event_;
import client.blogic.management.ii.EventAdmin;
import client.blogic.management.ii.IIComponent;
import common.z.TClass;
import common.z.TClassImpl;
import common.z.czt.visitors.CZTCloner;
import common.z.czt.visitors.CZTReplacer;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.session.SectionManager;
import net.sourceforge.czt.z.ast.Sect;
import net.sourceforge.czt.z.ast.FreePara;
import net.sourceforge.czt.z.ast.ZSect;
import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.AxPara;
import client.presentation.ClientUI;
import client.blogic.management.Controller;
import client.blogic.management.communic.*;
import common.z.SpecUtils;
import compserver.prunning.SpecInfo;

/**
 * Instances of this class (although we assume there is only one in the system)
 * manages the requests for prune test classes . These requests are
 * done in each event of type PruneTClassRequested that is announced in the system and
 * each of them are processed in a different thread (to favour performance
 * issues), running the method run() of TClassPruneClientRunner in each new thread.
 */
public class TClassPruneClient extends IIComponent {

	private TClass tClass;
	private String sectionName;
	private SectionManager manager;
	private ServerConfig serverConfig;
    /**
     * Manages an implicit invocation event.
     * @param event_
     * @throws java.lang.IllegalArgumentException if event_ is not instance of
     * PruneTClassRequested
     */
	public synchronized void manageEvent(Event_ event_)
		throws IllegalArgumentException{

		if(event_ instanceof UnfoldedSpecLoaded){
			Spec spec = ((UnfoldedSpecLoaded)event_).getSpec();
			for (Sect sect : spec.getSect()) {
					if (sect instanceof ZSect) {
						ZSect zSect = (ZSect) sect;
						sectionName = zSect.getName();
					}
			}
		}
		else if(event_ instanceof PruneTClassRequested){
			PruneTClassRequested pruneTClassRequested = (PruneTClassRequested) event_;
			ClientUI clientUI = getMyClientUI();
			Controller controller = clientUI.getMyController();
			tClass = pruneTClassRequested.getTClass();

			serverConfig = pruneTClassRequested.getServerConfig();
			manager = controller.getTypeCheckerManager();
			Map<RefExpr, Expr> axDefsValues = controller.getAxDefsValues();
			Spec spec = controller.getOriginalSpec();
			List<FreePara> freeParaList = SpecUtils.getFreeTypes(spec);
			SpecInfo specInfo = new SpecInfo();
			specInfo.setAxDefsValues(axDefsValues);
			specInfo.setFreeParaList(freeParaList);

			// Reemplazamos los valores seteados de las definiciones axiomaticas
			TClass newTClass = null;

			// We replace in predicate the values for axiomatic definitions
			if (axDefsValues != null) {
			    AxPara tClassAxPara = (AxPara) tClass.getMyAxPara().accept(new CZTCloner());
			    String tClassName = SpecUtils.getAxParaName(tClass);
			    Pred pred = SpecUtils.getAxParaPred(tClassAxPara);
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
			    SpecUtils.setAxParaPred(tClassAxPara, pred);
			    newTClass = new TClassImpl(tClassAxPara,tClassName);
			}
			else
			    newTClass = tClass;


			(new Thread(new TClassPruneClientRunner(newTClass, manager, sectionName, serverConfig, specInfo))).start();
		}
		else
			throw new IllegalArgumentException();
	}
}
