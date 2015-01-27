package $CSP;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import mLibrary.mVar;
import dataaccess.mDataAccess;
import dataaccess.mDataAccessMemory;

public class Response {
	private mDataAccess mDataResponse = new mDataAccessMemory();

	private HttpServletResponse originalResponse;

	public Response(HttpServletResponse httpResponse) {
		originalResponse = httpResponse;
	}

	public Object getAppTimeout() {
		// TODO REVISAR TIMEOUT PROVISÓRIO;
		return 900;
	}

	public mVar getData(Object... args) {
		return new mVar(args, mDataResponse);
	}

	public Object getLogin(Object object, Object object2, int i) {
		// TODO Auto-generated method stub
		return 1;
	}

	public HttpServletResponse getOriginalResponse() {
		return originalResponse;
	}

	public void populateParameter(Map<String, String[]> map) {
		Set<Entry<String, String[]>> results = map.entrySet();
		for (Entry<String, String[]> result : results) {
			for (int i = 0; i < result.getValue().length; i++) {
				mDataResponse.subs(result.getKey(), i + 1).set(result.getValue()[i]);
			}
		}
	}

	public void setContentType(String string) {
		originalResponse.setContentType(string);

	}

	public void setData(Object subs, Object idx, Object value) {
		mDataResponse.subs(subs, idx).set(value);
	}

	public void setHeaders(String string, String string2) {
		originalResponse.setHeader(string, string2);

	}

	public void setHTTPVersion(String string) {
		// originalResponse.set

	}

	public void setSessionId(String sessionId) {
		setData("sessionId", 1, sessionId);
	}
}
