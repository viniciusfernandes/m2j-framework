package $CSP;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import mLibrary.mListBuild;
import mLibrary.mContext;
import mLibrary.mFunction;
import mLibrary.mVar;

public class Request {

	private Stream content;
	private mContext m$;
	private HttpServletRequest originalRequest;
	private String queryString;

	public Request(HttpServletRequest request) {
		originalRequest = request;
	}

	public Request(HttpServletRequest request, String queryString) {
		this.originalRequest = request;
		this.queryString = queryString;
		// forceRequestData(queryString);
	}

	public Object getCgiEnv(Object string, Object pDefault) {
		String cgiVarName = String.valueOf(string);
		Object result = pDefault;
		switch (cgiVarName) {
		case "AUTH_TYPE":
			result = originalRequest.getAuthType();
			break;
		case "CONTENT_LENGTH":
			result = originalRequest.getContentLength();
			break;
		case "CONTEXT_PATH":
			result = originalRequest.getContextPath();
			break;
		case "CONTENT_TYPE":
			result = originalRequest.getContentType();
			break;
		case "PATH_INFO":
			result = originalRequest.getPathInfo();
			break;
		case "PATH_TRANSLATED":
			result = originalRequest.getPathTranslated();
			break;
		case "QUERY_STRING":
			result = originalRequest.getQueryString();
			break;
		case "REMOTE_ADDR":
			result = originalRequest.getRemoteAddr();
			break;
		case "REMOTE_HOST":
			result = originalRequest.getRemoteHost();
			break;
		case "REMOTE_USER":
			result = originalRequest.getRemoteUser();
			break;
		case "REQUEST_METHOD":
			result = originalRequest.getMethod();
			break;
		case "SCRIPT_NAME":
			result = originalRequest.getServletPath();
			break;
		case "SERVER_NAME":
			result = originalRequest.getServerName();
			break;
		case "SERVER_PORT":
			result = originalRequest.getServerPort();
			break;
		case "SERVER_PORT_SECURE":
			result = originalRequest.isSecure();
			break;
		case "SERVER_PROTOCOL":
			result = originalRequest.getProtocol();
			break;
		case "SERVER_SOFTWARE":
			result = originalRequest.getHeader("Server-Software");
			break;
		case "HTTP_ACCEPT":
			result = originalRequest.getHeader("Accept");
			break;
		case "HTTP_ACCEPT_CHARSET":
			result = originalRequest.getHeader("Accept-Charset");
			break;
		case "HTTP_ACCEPT_ENCODING":
			result = originalRequest.getHeader("Accept-Encoding");
			break;
		case "HTTP_ACCEPT_LANGUAGE":
			result = originalRequest.getHeader("Accept-Language");
			break;
		case "HTTP_CONNECTION":
			result = originalRequest.getHeader("Connection");
			break;
		case "HTTP_COOKIE":
			result = originalRequest.getHeader("Cookie");
			break;
		case "HTTP_HOST":
			result = originalRequest.getHeader("Host");
			break;
		case "HTTP_REFERER":
			result = originalRequest.getHeader("Referer");
			break;
		case "HTTP_USER_AGENT":
			result = originalRequest.getHeader("User-Agent");
			break;
		default:
			break;
		}
		return (result != null) ? result : pDefault;
	}

	public mVar getCgiEnvs(Object... key) {
		mVar var = null;
		if (key != null && key.length > 0) {
			var = m$.var("%request", "CgiEnvs").s$(key);
		}
		return var;
	}

	public Stream getContent() {
		// TODO Auto-generated method stub
		return content;
	}

	public Object getData(Object... args) {
		return varData(args).get();
	}

	public HttpServletRequest getOriginalRequest() {
		return originalRequest;
	}

	public Object getURL() {
		// TODO Auto-generated method stub
		return null;
	}

	public void killData(Object... subs) {
		m$.var("%request", "Data").s$(subs).kill();
	}

	public void setCgiEnvs(Map<String, String[]> cgiEnvs) {
		throw new UnsupportedOperationException();
	}

	public void setCgiEnvs(String string, Object object) {
		throw new UnsupportedOperationException();

	}

	public void setContext(mContext m$) {
		this.m$ = m$;
		if (queryString != null) {
			forceRequestData(queryString);
		}
		else {
			populateRequest();
		}
		forceRequestData(originalRequest.getQueryString());
	}

	public void setData(Object subs, Object idx, Object value) {
		if ((subs == null) || (subs.toString().isEmpty())) {
			return;
		}
		mListBuild lo = mListBuild.getList(value.toString());
		if (lo != null) {
			value = lo;
		}
		m$.var("%request", "Data", subs, idx).set(value);
	}

	public mVar varData(Object... args) {
		mVar var = null;
		if (args != null && args.length > 0) {
			var = m$.var("%request", "Data").s$(args);
		}
		return var;
	}

	private void forceRequestData(String queryString) {
		if (queryString != null) {
			String[] atributos = queryString.split("&");
			String[] parameter = null;
			Object key, value;
			for (String attr : atributos) {
				parameter = attr.split("=");
				key = parameter[0];
				value = mFunction.$zconvert((parameter.length == 1) ? "" : parameter[1], "i", "URL");
				setData(key, 1, value);
			}
		}
	}

	private void populateCgiEnv() {
		m$.var("%request", "CgiEnvs", "AUTH_TYPE").set(originalRequest.getAuthType());
		m$.var("%request", "CgiEnvs", "CONTENT_LENGTH").set(originalRequest.getContentLength());
		m$.var("%request", "CgiEnvs", "CONTEXT_PATH").set(originalRequest.getContextPath());
		m$.var("%request", "CgiEnvs", "CONTENT_TYPE").set(originalRequest.getContentType());
		m$.var("%request", "CgiEnvs", "PATH_INFO").set(originalRequest.getPathInfo());
		m$.var("%request", "CgiEnvs", "PATH_TRANSLATED").set(originalRequest.getPathTranslated());
		m$.var("%request", "CgiEnvs", "QUERY_STRING").set(originalRequest.getQueryString());
		m$.var("%request", "CgiEnvs", "REMOTE_ADDR").set(originalRequest.getRemoteAddr());
		m$.var("%request", "CgiEnvs", "REMOTE_HOST").set(originalRequest.getContentLength());
		m$.var("%request", "CgiEnvs", "REMOTE_USER").set(originalRequest.getRemoteUser());
		m$.var("%request", "CgiEnvs", "REQUEST_METHOD").set(originalRequest.getMethod());
		m$.var("%request", "CgiEnvs", "SCRIPT_NAME").set(originalRequest.getServletPath());
		m$.var("%request", "CgiEnvs", "SERVER_NAME").set(originalRequest.getServerName());
		m$.var("%request", "CgiEnvs", "SERVER_PORT").set(originalRequest.getServerPort());
		m$.var("%request", "CgiEnvs", "SERVER_PORT_SECURE").set(originalRequest.isSecure());
		m$.var("%request", "CgiEnvs", "SERVER_PROTOCOL").set(originalRequest.getProtocol());
		m$.var("%request", "CgiEnvs", "SERVER_SOFTWARE").set(originalRequest.getHeader("Server-Software"));
		m$.var("%request", "CgiEnvs", "HTTP_ACCEPT").set(originalRequest.getHeader("Accept"));
		m$.var("%request", "CgiEnvs", "HTTP_ACCEPT_CHARSET").set(originalRequest.getHeader("Accept-Charset"));
		m$.var("%request", "CgiEnvs", "HTTP_ACCEPT_ENCODING").set(originalRequest.getHeader("Accept-Encoding"));
		m$.var("%request", "CgiEnvs", "HTTP_ACCEPT_LANGUAGE").set(originalRequest.getHeader("Accept-Language"));
		m$.var("%request", "CgiEnvs", "HTTP_CONNECTION").set(originalRequest.getHeader("Connection"));
		m$.var("%request", "CgiEnvs", "HTTP_COOKIE").set(originalRequest.getHeader("Cookie"));
		m$.var("%request", "CgiEnvs", "HTTP_HOST").set(originalRequest.getHeader("Host"));
		m$.var("%request", "CgiEnvs", "HTTP_REFERER").set(originalRequest.getHeader("Referer"));
		m$.var("%request", "CgiEnvs", "HTTP_USER_AGENT").set(originalRequest.getHeader("User-Agent"));
	}

	private void populateRequest() {
		Set<Entry<String, String[]>> entries = originalRequest.getParameterMap().entrySet();
		for (Entry<String, String[]> entry : entries) {
			String key = entry.getKey();
			int keyCount = 0;
			for (String value : entry.getValue()) {
				keyCount++;
				setData(key, keyCount, value);
			}
			/*
			 * for (int keyCount = 1; keyCount <= entry.getValue().length; keyCount++)
			 * { setData(entry.getKey(), keyCount, entry.getValue()[keyCount-1]); }
			 */
		}
		populateCgiEnv();
	}
}
