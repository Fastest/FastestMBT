package common.z.czt;

import net.sourceforge.czt.animation.eval.ZLive;

public class UniqueZLive{

	private static ZLive zLive;

	private UniqueZLive(){
		zLive = new ZLive();
	}

	public static ZLive getInstance(){
	
		if(zLive==null)
			new UniqueZLive();
		return zLive;
	}
}

 
