package $CSP;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpSession;

import mLibrary.mVar;
import dataaccess.mDataAccess;
import dataaccess.mDataAccessMemory;

public class Session {
	private mDataAccess mDataSession = new mDataAccessMemory();

	private HttpSession originalSession;

	public Session(HttpSession httpSession) {
		setSessionId(httpSession.getId());
		originalSession = httpSession;
	}

	public Object getAppTimeout() {
		// TODO REVISAR TIMEOUT PROVISÓRIO;
		return 900;
	}

	public mVar getData(Object... args) {
		return new mVar(args, mDataSession);
	}

	public Object getLogin(Object object, Object object2, int i) {
		// TODO Auto-generated method stub
		return 1;
	}

	public HttpSession getOriginalSession() {
		return originalSession;
	}

	public Object getSessionId() {
		return originalSession.getId();
	}

	public boolean isNewSession() {
		return originalSession.isNew();
	}

	public void populateParameter(Map<String, String[]> map) {
		Set<Entry<String, String[]>> results = map.entrySet();
		for (Entry<String, String[]> result : results) {
			for (int i = 0; i < result.getValue().length; i++) {
				mDataSession.subs(result.getKey(), i + 1).set(result.getValue()[i]);
			}
		}
	}

	public void setData(Object subs, Object idx, Object value) {
		mDataSession.subs(subs, idx).set(value);
	}

	public void setSessionId(String sessionId) {
		setData("sessionId", 1, sessionId);
	}
}
