package $CSP;

import util.mFunctionUtil;
import mLibrary.mClass;
import mLibrary.mOperation;
import br.com.innovatium.mumps2java.todo.REVIEW;
import br.com.innovatium.mumps2java.todo.TODO;

public abstract class Page extends mClass {

	@TODO
	public static String Decrypt(String string) {
		return string;// TODO REVISAR
	}

	@TODO
	public static String Encrypt(String string) {
		return string;// TODO REVISAR
	}

	public static Object EscapeURL(Object url) {
		return mFunctionUtil.escapeURL(String.valueOf(url));
	}

	public static Object QuoteJS(Object js) {
		return "'" + mFunctionUtil.escapeJS(String.valueOf(js)) + "'";
	}

	/*
	 * public void Page(boolean skipheader){ }
	 */

	public static String UnescapeURL(Object url) {
		return mFunctionUtil.unescapeURL(String.valueOf(url));
	}

	@TODO
	public String HyperEventCall(String method, String args, Integer type) {
		return "cspHttpServerMethod(\"" + method + "\"," + args + ")";
	}

	public Object OnPage() {
		return null;
	}

	public void OnPostHTTP() {
	}

	public Object OnPreHTTP() {
		return true;
	}

	@REVIEW(description = "Revisar ordem de execução dos métodos")
	public void Page() {
		if (mOperation.Logical(OnPreHTTP())) {
			OnPage();
			OnPostHTTP();
		}
	}
}
