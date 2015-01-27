package $Dictionary;

import util.mFunctionUtil;
import mLibrary.mListBuild;

public class MethodDefinition {
	public Object $Exists(Object oid) {
		
		if(oid instanceof mListBuild){
			mListBuild lb = (mListBuild)oid;
			String method = lb.firstElement().toString().replace("||", ".");
			return mFunctionUtil.existsToDispath(method)?1:0;
		}
		return 0;
	}

	public Object $ExistsId(Object id) {
		return 0;
	}
}
