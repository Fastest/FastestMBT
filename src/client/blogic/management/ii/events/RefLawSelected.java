package client.blogic.management.ii.events;

import java.io.*;

/**
 * @author Hache
 */

public class RefLawSelected extends Event_{
    private String refLawName;
    private File tcrlFile;

    public RefLawSelected(String refLawName, File tcrlFile){
        this.refLawName = refLawName;
        this.tcrlFile = tcrlFile;
	super.setEventName("refLawSelected");
    }

    public String getRefLawName(){
        return refLawName;
    }

    public void setRefLawName(String refLawName){
        this.refLawName = refLawName;
    }

    public File getTCRLFile(){
        return tcrlFile;
    }

    public void setTCRLFile(File tcrlFile){
        this.tcrlFile = tcrlFile;
    }
}
