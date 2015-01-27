package mSystem;

import mLibrary.mClass;
import mLibrary.mContext;

public class CSP {
	mContext m$;
	public CSP(mContext m$) {
		this.m$ = m$;
	}

	public Object DisplayConfig(){

    m$.Write("\n");
    //<< Write "CSP Global Configuration parameters",!
    m$.Write("CSP Global Configuration parameters","\n");
    //<< Write "-----------------------------------",!!
    m$.Write("-----------------------------------","\n","\n");
    //<< Write "DebugErrorPage:",..GetConfig("DebugErrorPage"),!
  //  m$.Write("DebugErrorPage:",m$.call(this,"GetConfig","DebugErrorPage"),"\n");REVIEW
    //<< Write "If true then when there is an error on the error page it will display the",!
    m$.Write("If true then when there is an error on the error page it will display the","\n");
    //<< Write "default CSP error page. This can help in debugging the error page, however",!
    m$.Write("default CSP error page. This can help in debugging the error page, however","\n");
    //<< Write "the default behavior is to log the error in ^%ETN and display a criptic",!
    m$.Write("the default behavior is to log the error in ^%ETN and display a criptic","\n");
    //<< Write "message that something has gone wrong. This is more secure for a live site.",!
    m$.Write("message that something has gone wrong. This is more secure for a live site.","\n");
    //<< Write !
    m$.Write("\n");
    //<< Write "DefaultErrorPage:",..GetConfig("DefaultErrorPage"),
//    m$.Write("DefaultErrorPage:",m$.call(this,"GetConfig","DefaultErrorPage"),"\n");REVIEW
    //<< Write "If defined this is the default error page to use when no application error",
    m$.Write("If defined this is the default error page to use when no application error","\n");
    //<< Write "page is defined. This applies if the user references an application that",!
    m$.Write("page is defined. This applies if the user references an application that","\n");
    //<< Write "does not exist, or an error occurs in an application that does not have",!
    m$.Write("does not exist, or an error occurs in an application that does not have","\n");
    //<< Write "an error page setup",!
    m$.Write("an error page setup","\n");
    //<< Write !
    m$.Write("\n");
    //<< Write "ClearSessionsOnRestart:",..GetConfig("ClearSessionsOnRestart"),
 //   m$.Write("ClearSessionsOnRestart:",m$.call(this,"GetConfig","ClearSessionsOnRestart"),"\n");REVIEW
    //<< Write "If true this will clear all the session data in ^%cspSession on a",
    m$.Write("If true this will clear all the session data in ^%cspSession on a","\n");
    //<< Write "Cache system restart. This means that a user can not continue their",!
    m$.Write("Cache system restart. This means that a user can not continue their","\n");
    //<< Write "CSP session after the restart, it also means that no licenses are",!
    m$.Write("CSP session after the restart, it also means that no licenses are","\n");
    //<< Write "taken out for these restarted sessions",!
    m$.Write("taken out for these restarted sessions","\n");
    //<< Write !
    m$.Write("\n");
    //<< Write "SessionLockTimeout:",..GetConfig("SessionLockTimeout"),
  //  m$.Write("SessionLockTimeout:",m$.call(this,"GetConfig","SessionLockTimeout"),"\n");REVIEW
    //<< Write "Number of seconds to wait to obtain a lock on the session object",!
    m$.Write("Number of seconds to wait to obtain a lock on the session object","\n");
    //<< Write "if it still can not obtain a lock in this period of time it will",!
    m$.Write("if it still can not obtain a lock in this period of time it will","\n");
    //<< Write "not be able to open the session object.",!
    m$.Write("not be able to open the session object.","\n");
    //<< Write !
    m$.Write("\n");
    //<< Write "Expire:",..GetConfig("Expire"),
  //  m$.Write("Expire:",m$.call(this,"GetConfig","Expire"),"\n");REVIEW
    //<< Write "Number of days to keep the CSP performance data for before being",
    m$.Write("Number of days to keep the CSP performance data for before being","\n");
    //<< Write "removed.",!
    m$.Write("removed.","\n");
    //<< Write !
    m$.Write("\n");
    //<< Write "DefaultServerName:",..GetConfig("DefaultServerName"),
  //  m$.Write("DefaultServerName:",m$.call(this,"GetConfig","DefaultServerName"),"\n");REVIEW
    //<< Write "In CSP we support virtual servers, so a request for /csp/samples/menu.csp",
    m$.Write("In CSP we support virtual servers, so a request for /csp/samples/menu.csp","\n");
    //<< Write "from web server 'X' can be dispatched to a different namespace than the",!
    m$.Write("from web server 'X' can be dispatched to a different namespace than the","\n");
    //<< Write "same request from web server 'Y'. If this DefaultServerName is defined this",!
    m$.Write("same request from web server 'Y'. If this DefaultServerName is defined this","\n");
    //<< Write "is the web server name to default to if none is specified in the url.",!
    m$.Write("is the web server name to default to if none is specified in the url.","\n");
    //<< Write !
    m$.Write("\n");
    //<< Write "CSPConfigName:",..GetConfig("CSPConfigName"),
  //  m$.Write("CSPConfigName:",m$.call(this,"GetConfig","CSPConfigName"),"\n");REVIEW
    //<< Write "Machine configuration name used by CSP. This defaults to the system",
    m$.Write("Machine configuration name used by CSP. This defaults to the system","\n");
    //<< Write "config name unless manually set. CSP urls can be prefixed by this configuration",!
    m$.Write("config name unless manually set. CSP urls can be prefixed by this configuration","\n");
    //<< Write "name to allow one web server to talk to multiple Cache instance",!
    m$.Write("name to allow one web server to talk to multiple Cache instance","\n");
    //<< Write !
    m$.Write("\n");
    //<< Write "DefaultLoginPage:",..GetConfig("DefaultLoginPage"),
   // m$.Write("DefaultLoginPage:",m$.call(this,"GetConfig","DefaultLoginPage"),"\n");REVIEW
    //<< Write "System default login page if no login page is specified for this application",
    m$.Write("System default login page if no login page is specified for this application","\n");
    //<< Write !
    m$.Write("\n");
    //<< Write "DefaultPasswordChangePage:",..GetConfig("DefaultPasswordChangePage"),
  //  m$.Write("DefaultPasswordChangePage:",m$.call(this,"GetConfig","DefaultPasswordChangePage"),"\n");REVIEW
    //<< Write "System default password change page if no password change page is specified",
    m$.Write("System default password change page if no password change page is specified","\n");
    //<< Write "for this application",!
    m$.Write("for this application","\n");
    //<< Write !
    m$.Write("\n");
    //<< Write "OptionalBrokerJS:",..GetConfig("OptionalBrokerJS"),!
    //m$.Write("OptionalBrokerJS:",m$.call(this,"GetConfig","OptionalBrokerJS"),"\n");REVIEW
    //<< Write "If true, suppress the loading of cspBroker.js in the case of HyperEvents",!
    m$.Write("If true, suppress the loading of cspBroker.js in the case of HyperEvents","\n");
    //<< Write "which use XMLHttpRequest.",!
    m$.Write("which use XMLHttpRequest.","\n");
    //<< Write !
    m$.Write("\n");
    //<< Write "FileServerPage:",..GetConfig("FileServerPage"),
  //  m$.Write("FileServerPage:",m$.call(this,"GetConfig","FileServerPage"),"\n");REVIEW
    //<< Write "The class to call to serve up static files from this Cache server. The",
    m$.Write("The class to call to serve up static files from this Cache server. The","\n");
    //<< Write "url of the page is passed in as the 'FILE' parameter",!
    m$.Write("url of the page is passed in as the 'FILE' parameter","\n");
    //<< Write !
    m$.Write("\n");
    //<< Quit
    return null;
    }
}
