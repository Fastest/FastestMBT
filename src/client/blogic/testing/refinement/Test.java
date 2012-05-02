package client.blogic.testing.refinement;

import net.sourceforge.czt.z.ast.*;

import java.util.*;
/**
 *
 * @author Hache
 */
public class Test {
    public static List<AxPara> getAxParaList(Spec spec){
    List<AxPara> list = new ArrayList<AxPara>();
    for (Sect sect : spec.getSect()) {
        if (sect instanceof ZSect){
            ZSect zSect = (ZSect)sect;
            ParaList paraList = zSect.getParaList();
            if (paraList instanceof ZParaList) {
                ZParaList zParaList = (ZParaList) paraList;
                for(int i=0; i < zParaList.size(); i++){
                    Para para = zParaList.get(i);
                    if(para instanceof AxPara){
                        AxPara axPara = (AxPara) para;
                        String strBox=(axPara.getBox()).name();
                        if (strBox.equals("SchBox"))
                            list.add(axPara);
                    }
                }
            }
        }
    }
    return list;
}

}