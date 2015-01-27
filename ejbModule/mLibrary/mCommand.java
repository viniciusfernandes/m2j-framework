package mLibrary;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import mLibrary.exceptions.CommandException;
import mLibrary.implementation.xecute.Parser;
import util.mFunctionUtil;
import br.com.innovatium.mumps2java.todo.TODO;

public class mCommand {
	private final mContext m$;

	public mCommand(mContext m$) {
		this.m$ = m$;
	}

	private void callDispatch(mClass objClass, String methodName, Object... parameters) {
		methodName = m$.defineMethodName(objClass, methodName);
		if ((parameters == null) || (parameters.length == 0)) {
			m$.dispatch(false, false, objClass, methodName);
		} else {
			m$.dispatch(false, false, objClass, methodName, parameters);
		}
	}

	public void Close(Object io) {
		m$.removeIO(String.valueOf(io));
		m$.var("$ZEOF").set(0);
	}

	public void Do(mClass objClass, String methodName, Object... parameters) {
		callDispatch(objClass, methodName, parameters);
	}

	public void Do(Object methodName) {
		Do(mFunctionUtil.castString(methodName));
	}

	public void Do(String methodName) {
		Do(methodName, (Object[]) null);
	}

	public void Do(String methodName, Object... parameters) {
		if (isIndirectionExecution(methodName)) {
			mVar var = m$.indirectVar(methodName);
			methodName = var.getName();

			if (isMethodExecution(methodName)) {
				Do(methodName, var.getParameters());
			} else {
				Do(methodName, var.getParameters());
			}
		} else {
			if (isMethodExecution(methodName)) {
				callDispatch(null, methodName, parameters);
			} else {
				callDispatch(null, methodName, parameters);
			}
		}
	}

	public Object Goto(Object label) {
		return m$.fnc$(label);
	}

	/*
	 * Pausa o processamento por um determinado número de milisegundos
	 */
	public void Hang(Object obj) {
		Double time = mFunctionUtil.numberConverter(obj);
		time = time * 1000;

		try {
			Thread.sleep(time.longValue());
		} catch (NumberFormatException e) {
			Logger.getLogger(mClass.class.getName()).log(Level.SEVERE, null, e);
		} catch (InterruptedException e) {
			Logger.getLogger(mClass.class.getName()).log(Level.SEVERE, null, e);
		}

	}

	private boolean isIndirectionExecution(String content) {
		if (content == null) {
			return false;
		}
		return content.indexOf("(") != -1;
	}

	private boolean isMethodExecution(String content) {
		if (content == null) {
			return false;
		}
		return content.indexOf("^") != -1;
	}

	/*
	 * Revisar implementacao
	 */
	@TODO
	public void Lock(mVar var) {
		// TODO REVISAR IMPLEMENTAÇÃO
		// throw new UnsupportedOperationException();
	}

	/*
	 * Revisar implementacao
	 */
	@TODO
	public void Lock(mVar var, int index) {
		// TODO REVISAR IMPLEMENTAÇÃO
		// throw new UnsupportedOperationException();
	}

	public void Lock(String string, String string2, String string3) {
		// TODO REVISAR IMPLEMENTAÇÃO
		throw new UnsupportedOperationException();
	}

	public void LockInc(mVar var) {
		// TODO REVISAR IMPLEMENTAÇÃO
	}

	public void LockInc(mVar var, int i) {

	}

	public void Merge(mVar target, mVar source) {
		m$.merge(target, source);
	}

	public void Open(Object fileName) {
		Open(fileName, "", 0);
	}

	public void Open(Object devicename, Object parameters, Object timeout) {
		if (devicename.toString().contains("TCP")) {
			URL url = null;
			try {

				url = new URL("http", parameters.toString().split(":")[0], mFunctionUtil.integerConverter(
						parameters.toString().split(":")[1]).intValue(), "");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			m$.putIO(String.valueOf(devicename), new Object[] { url, parameters });
		}
	}

	public void Open(Object devicename, String parameters, int timeout) {
		m$.putIO(String.valueOf(devicename), new Object[] { new File(String.valueOf(devicename)), parameters });
	}

	public void Open(Object devicename, String parameters, Object concat, int timeout) {
		// TODO Auto-generated method stub
		m$.putIO(String.valueOf(devicename), new Object[] { new File(String.valueOf(devicename)), parameters });
	}

	/*
	 * Exibe mensagem no console solicitando entrada de dados.
	 */
	public void Read(Object... parameters) {
		try {
			Object variable = null;
			Object scan = null;
			if (parameters != null && parameters.length > 0) {
				variable = parameters[0];
			}
			scan = m$.getReader().readLine();
			if (variable instanceof mVar) {
				mVar var = (mVar) variable;
				if (scan == null) {
					var.set("");
					m$.var("$ZEOF").set(-1);
				} else {
					var.set(scan);
				}
			}
		} catch (Exception e) {
			m$.var("$test").set(0);
			m$.var("$ZERROR").set(e.getMessage());
			Logger.getLogger(mClass.class.getName()).log(Level.SEVERE, null, e);
			throw new CommandException(e.getMessage());
		} finally {
		}
	}

	public void SQL() {
		m$.var("SQLCODE").set("1");
		// TODO REVISAR IMPLEMENTAÇÃO
	}

	public void Unlock(mVar var) {
		// TODO REVISAR IMPLEMENTAÇÃO
	}

	public void Unlock(mVar var, String str) {
		// TODO REVISAR IMPLEMENTAÇÃO
	}

	public void Unlock(String string) {
		// TODO REVISAR IMPLEMENTAÇÃO
	}

	public void Use(Object io) {
		m$.useIO(String.valueOf(io));
	}

	/*
	 * Remove toString to wirte faster
	 */
	@TODO
	public void Write(Object... string) {
		try {
			Writer writer = m$.getWriter();
			for (Object str : string) {
				try {
					String strWr = mFunctionUtil.toString(str);
					if (strWr.contains("/gif") && strWr.contains(".gif")) {
						strWr = strWr.replace("/SESDF-BAZAAR", m$.getRequest().getOriginalRequest().getServletContext()
								.getContextPath()
								+ "/resources");
					}
					writer.write(strWr);
					if (!m$.getIO().equals("response")) {
						writer.flush();
						m$.var("$zwrite").set(strWr);
					}
					// *
					if (strWr.contains("1004-14/000022")) {
						System.out.println(strWr);
					}
					// */

				} catch (IOException e) {
					throw new IllegalArgumentException("Fail to write the string HTML " + str.toString());
				}
			}
		} catch (NullPointerException e) {
			Logger.getLogger(mClass.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public void WriteHtml(Object... string) {
		Write(string);
	}

	public void WriteJS(Object... string) {
		for (int i = 0; i < string.length; i++) {
			string[i] = String.valueOf(string[i]).replaceAll(Pattern.quote("&lt;"), "<");
			string[i] = String.valueOf(string[i]).replaceAll(Pattern.quote("&gt;"), ">");
		}
		Write(string);
	}

	public void Xecute(Object command) {
		String cmdStr = String.valueOf(command).trim();

		// do VARAlertaLocalLinha.OnLineAdded

		int pos = 0;
		while ((pos = cmdStr.indexOf(";", pos)) != -1) {

			int doubleQuoteCount = (cmdStr.substring(0, pos) + "END").split("\"").length - 1;
			if (doubleQuoteCount % 2 == 0) {
				cmdStr = cmdStr.substring(0, pos).trim(); // Remove da linha o
				// comentário.
				break;
			}
			pos++;
		}

		while ((pos = cmdStr.indexOf("//", pos)) != -1) {

			int doubleQuoteCount = (cmdStr.substring(0, pos) + "END").split("\"").length - 1;
			if (doubleQuoteCount % 2 == 0) {
				cmdStr = cmdStr.substring(0, pos).trim(); // Remove da linha o
				// comentário.
				break;
			}
			pos++;
		}

		while ((pos = cmdStr.indexOf("/*", pos)) != -1) {

			int doubleQuoteCount = (cmdStr.substring(0, pos) + "END").split("\"").length - 1;
			if (doubleQuoteCount % 2 == 0) {
				cmdStr = cmdStr.substring(0, pos).trim(); // Remove da linha o
				// comentário.
				break;
			}
			pos++;
		}

		if (cmdStr.isEmpty()) {
			return;
		}
		if (cmdStr.indexOf("OnLineAdded^VARAlertaLocalLinha") > -1
				|| cmdStr.indexOf("SetRefresh^VARAlertaLocalLinha") > -1
				|| cmdStr.indexOf("OnBeforePrimaryKey^VARAlertaLocalLinha") > -1
				|| cmdStr.indexOf("UpdatePrimaryKeyFormat^VARAlertaLocalLinha") > -1
				|| cmdStr.indexOf("OnBeforeClick^VARAlertaLocalLinha") > -1
				|| cmdStr.indexOf("OnClick^VARAlertaLocalLinha") > -1
				|| cmdStr.indexOf("GetRemovalCode^VARAlertaLocalLinha") > -1
				|| cmdStr.contains("SourceControl.Exporter")
				|| cmdStr.contains("set strResult=##class(alSOH.iStockHistory).ItemHasTransactions(YKEY)")) {
			// No action.
			return;
		}
		Parser p = new Parser(m$, cmdStr);
		p.parse();
	}

	@Deprecated
	public void XecuteOld(Object command) {
		// m$.var("^MXecute", "cmd", ++m$.xecuteCount).set(command.toString());
		// m$.var("^MXecute", command.toString()).set("");
		String cmdStr = String.valueOf(command).trim();

		// do VARAlertaLocalLinha.OnLineAdded

		int pos = 0;
		while ((pos = cmdStr.indexOf(";", pos)) != -1) {

			int doubleQuoteCount = (cmdStr.substring(0, pos) + "END").split("\"").length - 1;
			if (doubleQuoteCount % 2 == 0) {
				cmdStr = cmdStr.substring(0, pos); // Remove da linha o
				// comentário.
				break;
			}
			pos++;
		}
		if (cmdStr.isEmpty()) {
			return;
		}
		if (cmdStr.indexOf("OnLineAdded^VARAlertaLocalLinha") > -1
				|| cmdStr.indexOf("SetRefresh^VARAlertaLocalLinha") > -1
				|| cmdStr.indexOf("OnBeforePrimaryKey^VARAlertaLocalLinha") > -1
				|| cmdStr.indexOf("UpdatePrimaryKeyFormat^VARAlertaLocalLinha") > -1
				|| cmdStr.indexOf("OnBeforeClick^VARAlertaLocalLinha") > -1
				|| cmdStr.indexOf("OnClick^VARAlertaLocalLinha") > -1
				|| cmdStr.indexOf("GetRemovalCode^VARAlertaLocalLinha") > -1
				|| cmdStr.contains("SourceControl.Exporter")) {
			// No action.
			return;
		}
		// DO
		if (cmdStr.toLowerCase().startsWith("do ") || cmdStr.toLowerCase().startsWith("d ")) {
			cmdStr = cmdStr.replaceFirst("DO |Do |dO |do |D |d ", "");
			String cmdStr2 = null;
			if (cmdStr.toLowerCase().indexOf(" do ") >= 0) {
				int posStr2 = cmdStr.toLowerCase().indexOf(" do ");
				cmdStr2 = cmdStr.substring(posStr2 + 1);
				cmdStr = cmdStr.substring(0, posStr2);
			}
			Object[] cmdParam = m$.parseCall(cmdStr);

			if (cmdStr.equals("Output^COMViewFilterColumn(enumType,.objOutput)")) {
				Do("COMViewFilterColumn.Output", m$.var("enumType").get(), m$.var("objOutput"));
			} else if (cmdStr.equals("OnBeforeFormat^WWW00411(YKEY,YFELD,YART,YLFN,.pstrBGOverride)")) {
				return;
			} else if (cmdStr.equals("OnBeforeFormat^WWW0011(YKEY,YFELD,YART,YLFN,.pstrBGOverride)")) {
				return;
			}

			else if (cmdStr.startsWith("##class")) {
				if (cmdParam[1].toString().startsWith("SourceControl.")) {
					// SourceControl: TODO
				} else {
					Do(cmdParam[1].toString() + cmdParam[2].toString(),
							Arrays.copyOfRange(cmdParam, 3, cmdParam.length));
				}
			} else {

				Do(cmdParam[0].toString().trim(), Arrays.copyOfRange(cmdParam, 1, cmdParam.length));

			}
			if (cmdStr2 != null) {
				Xecute(cmdStr2);
			}

		} else if (cmdStr.equals("do AfterDataFields^COMViewFilter(\"MEDPatient,,\",1)")) {
			Do("COMViewFilter.AfterDataFields", "MEDPatient,,", 1);
		} else if (cmdStr.equals("d IsFirmable^INReq(YM,YFORM,$g(YKEY),$g(YFELD))")) {
			Do("INReq.IsFirmable", m$.var("YM").get(), m$.var("YFORM").get(), mFunction.$get(m$.var("YKEY")),
					mFunction.$get(m$.var("YFELD")));
		} else if (cmdStr.equals("do ##class(SourceControl.Exporter).TagNMArtifactByNameKey(YDATEI,YKEY)")) {
			// Do("COMViewFilter.AfterDataFields","MEDPatient,,",1);m$.var("YVAR").get()
		} else if (cmdStr.startsWith("DO ") || cmdStr.startsWith("do ") || cmdStr.startsWith("D ")
				|| cmdStr.startsWith("d ")) {
			Do(String.valueOf(command).replaceFirst("DO |do |D |d ", ""));

			// SET
		} else if (cmdStr.equals("set strResult=##class(alSOH.iStockHistory).ItemHasTransactions(YKEY)")) {
			// set
			// strResult=##class(alSOH.iStockHistory).ItemHasTransactions(YKEY)
			// TODO REVISAR IMPLEMENTAÇÃO
		} else if (cmdStr.equals("set idCompany=$order(^WWW121D(0,id1,id2,idCompany))")) {
			m$.var("idCompany").set(m$.Fnc.$order(m$.indirectVar("^WWW121D(0,id1,id2,idCompany)")));
		} else if (mFunctionUtil.isMatcher(cmdStr, "set idClass = $order(^CacheTempFATSearch(0,", ",idClass))")) {
			String var = mFunctionUtil.matcher(cmdStr, "set idClass = $order(^CacheTempFATSearch(0,", ",idClass))")[0];
			m$.var("idClass").set(m$.Fnc.$order(m$.indirectVar("^CacheTempFATSearch(0," + var + ",idClass)")));
		} else if (cmdStr.equals("set idCompany=$order(^WWW122D(0,id1,id2,idCompany))")) {
			m$.var("idCompany").set(m$.Fnc.$order(m$.indirectVar("^WWW122D(0,id1,id2,idCompany)")));
		} else if (cmdStr.equals("set idClass = $order(^WWW004(0,\"!!!!!!!!!!!!\",idClass))")) {
			m$.var("idCompany").set(m$.Fnc.$order(m$.indirectVar("^WWW122D(0,id1,id2,idCompany)")));
		} else if (mFunctionUtil.isMatcher(cmdStr, "SET YOPTION=")) {
			String var = mFunctionUtil.matcher(cmdStr, "SET YOPTION=")[0];
			m$.var("YOPTION").set(var);
		} else if (mFunctionUtil.isMatcher(cmdStr, "s YQ=", "  //SR16842")) {
			String var = mFunctionUtil.matcher(cmdStr, "s YQ=", "  //SR16842")[0];
			m$.var("YQ").set(var);

		} else if (mFunctionUtil.isMatcher(cmdStr, "set strCode=$$GetRemovalCode^WWW120(\"WWW120\",", ")")) {
			String var = mFunctionUtil.matcher(cmdStr, "set strCode=$$GetRemovalCode^WWW120(\"WWW120\",", ")")[0];
			// m$.var("strCode").set(m$.fnc$("MEDPatient.GetRemovalCode","MEDPatient",var));//TODO
			// REVISAR GetRemovalCode NÃO EXISTE

		} else if (cmdStr.startsWith("SET ") || cmdStr.startsWith("set ") || cmdStr.startsWith("S ")
				|| cmdStr.startsWith("s ")) {
			String cmdSetStr = cmdStr.replaceFirst("SET |set |S |s ", "");
			String[] setParam = mFunctionUtil.matcher(cmdSetStr, "=");
			String setTarget = setParam[0].trim();
			String setSource = setParam[1].trim();
			try {
				if (setSource.startsWith("$$")) {
					{
						Object[] fncParam = m$.parseCall(setSource.substring(2));
						String fncName = m$.defineMethodName(fncParam[0].toString());
						m$.indirectVar(setTarget).set(
								m$.fnc$(fncName, Arrays.copyOfRange(fncParam, 1, fncParam.length)));
					}
				} else if (setSource.startsWith("##class")) {
					Object[] fncParam = m$.parseCall(setSource);
					m$.indirectVar(setTarget).set(
							m$.fnc$(fncParam[1].toString() + fncParam[2].toString(),
									Arrays.copyOfRange(fncParam, 3, fncParam.length)));
				} else if (setSource.startsWith("$") && !setSource.startsWith("$get")
						&& !setSource.startsWith("$piece") && !setSource.startsWith("$g")
						&& !setSource.startsWith("$p")) {

					if (setSource.equals("$piece(YKEY,\",\",2)")) {
						m$.indirectVar(setTarget).set(mFunction.$piece(m$.var("YKEY").get(), ",", 2));
					} else {

						throw new UnsupportedOperationException(
								"Implementation required for Xecute with command SET: '" + cmdStr + "'");
					}
				} else {
					m$.indirectVar(setTarget).set(m$.parseVarValue(setSource));
				}
			} catch (NullPointerException e) {
				throw new NullPointerException("Fail to execute Xecute. Null object found with command SET: '" + cmdStr
						+ "'");
			}

		} else if (cmdStr.equals("S YINHALT=$$GetPackUOM^INART(YKEY)")) {
			m$.var("YINHALT").set(m$.fnc$("INART.GetPackUOM", m$.var("YKEY").get()));
		} else if (cmdStr.equals("S YINHALT=$$GetHomeLocation^INReq(YBED)")) {
			m$.var("YINHALT").set(m$.fnc$("INReq.GetHomeLocation", m$.var("YBED").get()));
		} else if (cmdStr.equals("SET %TXT(1)=$$BeforeSave^WWWFORMValidation(YINHALT,YVAR)")) {
			m$.var("%TXT", 1).set(
					m$.fnc$("WWWFORMValidation.BeforeSave", m$.var("YINHALT").get(), m$.var("YVAR").get()));
		} else if (cmdStr.equals("SET %TXT(1)=$$CallBack^COMViewUtils(YINHALT,YVAR)")) {
			m$.var("%TXT", 1).set(m$.fnc$("COMViewUtils.CallBack", m$.var("YINHALT").get(), m$.var("YVAR").get()));
		} else if (cmdStr.equals("SET %TXT(1)=$$End^WWWEND(YINHALT,YVAR)")) {
			m$.var("%TXT", 1).set(m$.fnc$("WWWEND.End", m$.var("YINHALT").get(), m$.var("YVAR").get()));
		} else if (cmdStr.equals("set strStatus=$$OnBeforeDataAccess^WWW101(YFORM,YKEY,YFELD)")) {
			m$.var("strStatus").set(
					m$.fnc$("WWW101.OnBeforeDataAccess", m$.var("YFORM").get(), m$.var("YKEY").get(), m$.var("YFELD")
							.get()));
		} else if (cmdStr.equals("set strStatus=$$OnBeforeDataAccess1^VARWWW101(YFORM,YKEY,YFELD)")) {
			m$.var("strStatus").set(
					m$.fnc$("VARWWW101.OnBeforeDataAccess1", m$.var("YFORM").get(), m$.var("YKEY").get(),
							m$.var("YFELD").get()));
		} else if (cmdStr.equals("set strStatus=$$OnBeforeDataAccess^VARMEDPrescription(.YKEY,YFELD,YFORM)")) {
			m$.var("strStatus").set(
					m$.fnc$("VARMEDPrescription.OnBeforeDataAccess", m$.var("YKEY"), m$.var("YFELD").get(),
							m$.var("YFORM").get()));
		} else if (cmdStr.equals("set strStatus=$$OnBeforeDataAccess^WWW100()")) {
			m$.var("strStatus").set(m$.fnc$("WWW100.OnBeforeDataAccess"));
		} else if (cmdStr.equals("set strStatus=$$OnAfterSave^VARMEDPatient(YKEY,YFELD)")) {
			m$.var("strStatus").set(m$.fnc$("VARMEDPatient.OnAfterSave", m$.var("YKEY").get(), m$.var("YFELD").get()));
		} else if (cmdStr.equals("set strStatus=$$IsUsable^INLIEF(YKEY,YFORM)")) {
			m$.var("strStatus").set(m$.fnc$("INLIEF.IsUsable", m$.var("YKEY").get(), m$.var("YFORM").get()));
		} else if (cmdStr.equals("set strStatus=$$OnBeforeDataAccess^INReqSourceSupplier(YKEY,YUSER)")) {
			m$.var("strStatus").set(
					m$.fnc$("INReqSourceSupplier.OnBeforeDataAccess", m$.var("YKEY").get(), m$.var("YUSER").get()));
		} else if (cmdStr.equals("set strStatus=$$IsUsable^WWW001(YKEY,YFORM)")) {
			m$.var("strStatus").set(m$.fnc$("WWW001.IsUsable", m$.var("YKEY").get(), m$.var("YFORM").get()));
		} else if (cmdStr.equals("set strStatus=$$OnBeforeDataAccess^WWW001(YKEY,YFORM)")) {
			m$.var("strStatus").set(m$.fnc$("WWW001.OnBeforeDataAccess", m$.var("YKEY").get(), m$.var("YFORM").get()));
		} else if (cmdStr.equals("set strResult=$$HideCustoms^INVORG()")) {
			m$.var("strResult").set(m$.fnc$("INVORG.HideCustoms"));
		} else if (cmdStr.equals("set strResult='$$ValidItem^INART(YKEY)")) {
			m$.var("strResult").set(m$.fnc$("INART.ValidItem", m$.var("YKEY").get()));
		} else if (cmdStr.equals("set strResult=$$IsProgramOutOfDateRange^INReq(YFELD)")) {
			m$.var("strResult").set(m$.fnc$("INReq.IsProgramOutOfDateRange", m$.var("YFELD").get()));
		} else if (mFunctionUtil.isMatcher(cmdStr, "set strCode=$$GetRemovalCode^", "(\"", "\",\"", "\")")) {
			// set strCode=$$GetRemovalCode^WWW003("WWW003","INReq,6")
			String[] params = mFunctionUtil.matcher(cmdStr, "set strCode=$$GetRemovalCode^", "(\"", "\",\"", "\")");
			m$.var("strCode").set(m$.fnc$(params[0] + ".GetRemovalCode", params[0], params[2]));
		} else if (cmdStr.equals("set strCode=$$GetRemovalCode^WWW001(\"WWW001\",\"MEDPatient\")")) {
			m$.var("strCode").set(m$.fnc$("WWW001.GetRemovalCode", "WWW001", "MEDPatient"));
		} else if (cmdStr.equals("set strValue=$$GetDescription^WWWStatus(\"INReq\",\"1\",$g(SPRACHE))")) {
			m$.var("strValue")
					.set(m$.fnc$("WWWStatus.GetDescription", "INReq", "1", mFunction.$get(m$.var("SPRACHE"))));
		} else if (mFunctionUtil.isMatcher(cmdStr, "set objStoredData=$get(", ")")) {
			String var = mFunctionUtil.matcher(cmdStr, "set objStoredData=$get(", ")")[0];
			m$.var("objStoredData").set(mFunction.$get(m$.indirectVar(var)));
		} else if (mFunctionUtil.isMatcher(cmdStr, "set strCode=$$GetRemovalCode^MEDPatient(\"MEDPatient\",", ")")) {
			String var = mFunctionUtil.matcher(cmdStr, "set strCode=$$GetRemovalCode^MEDPatient(\"MEDPatient\",", ")")[0];
			m$.var("strCode").set(var);// TODO
			// REVISAR GetRemovalCode NÃO EXISTE
		} else if (mFunctionUtil.isMatcher(cmdStr, "S YONCHANGE=")) {
			String var = mFunctionUtil.matcher(cmdStr, "S YONCHANGE=")[0];
			m$.var("YONCHANGE").set(var);
		} else if (cmdStr.equals("set ^CacheTempEvent(YUCI,\"VARAlertaLocalLinha\",\"Format\") = 1")) {
			m$.var("^CacheTempEvent", m$.var("YUCI").get(), "VARAlertaLocalLinha", "Format").set(1);
		} else if (cmdStr.startsWith("SET %TXT(1)=$$")) {
			throw new UnsupportedOperationException("Implementation required for Xecute with command SET %TXT(1)=$$: '"
					+ cmdStr + "'");

			// IF
		} else if (cmdStr.equals("if (YPARA = \"\") set YPARA = YAUSWAHL")) {
			if (mOperation.Equal(m$.var("YPARA").get(), "")) {
				m$.var("YPARA").set(m$.var("YAUSWAHL").get());
			}
		} else if (mFunctionUtil.isMatcher(cmdStr, "IF $G(YSEITE)=0 SET YSEITE=")) {
			String var = mFunctionUtil.matcher(cmdStr, "IF $G(YSEITE)=0 SET YSEITE=")[0];
			if (mOperation.Equal(mFunction.$get(m$.var("YSEITE")), 0)) {
				m$.var("YSEITE").set(var);
			}
		} else if (mFunctionUtil.isMatcher(cmdStr, "if $$IsInUse^INART(YKEY) set YHID = ")) {
			String var = mFunctionUtil.matcher(cmdStr, "if $$IsInUse^INART(YKEY) set YHID = ")[0];
			if (mOperation.Logical(m$.fnc$("INART.IsInUse", m$.var("YKEY").get()))) {
				m$.var("YHID").set(var);
			}
		} else if (mFunctionUtil.isMatcher(cmdStr, "IF $$^INARTVERKHID(1)=1 SET YHID=")) {
			String var = mFunctionUtil.matcher(cmdStr, "IF $$^INARTVERKHID(1)=1 SET YHID=")[0];
			if (mOperation.Equal(m$.fnc$("INARTVERKHID.main", 1), 1)) {
				m$.var("YHID").set(var);
			}
		} else if (mFunctionUtil.isMatcher(cmdStr, "IF $$^INARTVERKHID(", ")=1 SET YHID=")) {
			String[] varArray = mFunctionUtil.matcher(cmdStr, "IF $$^INARTVERKHID(", ")=1 SET YHID=");
			if (mOperation.Equal(m$.fnc$("INARTVERKHID.main", varArray[0]), 1)) {
				m$.var("YHID").set(varArray[1]);
			}
		} else if (mFunctionUtil.isMatcher(cmdStr,
				"if $piece(YFELD,Y,8)=\"\" set YQ=\"1 Must have a relation database\"")) {
			// String[] varArray = mFncUtil.matcher(cmdStr,
			// "IF $$^INARTVERKHID(",")=1 SET YHID=");
			if (mOperation.Equal(mFunction.$piece(m$.var("YFELD").get(), m$.var("Y").get(), 8), "")) {
				m$.var("YQ").set("1 Must have a relation database");
			}
		} else if (mFunctionUtil.isMatcher(cmdStr, "if $piece(YFELD,Y,8)=\"\" set YQ=\"1 Must have relation class\"")) {
			// String[] varArray = mFncUtil.matcher(cmdStr,
			// "IF $$^INARTVERKHID(",")=1 SET YHID=");
			if (mOperation.Equal(mFunction.$piece(m$.var("YFELD").get(), m$.var("Y").get(), 8), "")) {
				m$.var("YQ").set("1 Must have a relation class");
			}
		} else if (mFunctionUtil.isMatcher(cmdStr, "I $P(YFELD,Y,138)>1 W $$^INARTPE(YKEY)")) {
			// String var = mFncUtil.matcher(cmdStr,
			// "I $P(YFELD,Y,138)>1 W $$^INARTPE(YKEY)")[0];
			if (mOperation.Greater(mFunction.$piece(m$.var("YFELD").get(), m$.var("Y").get(), 138), 1)) {
				m$.Cmd.Write(m$.fnc$("INARTPE.main", m$.var("YKEY").get()));
			}
			// WRITE
		} else if (cmdStr.equals("W:$P($G(YFELD),Y,40)=6 \"²\"  W:$P($G(YFELD),Y,40)=12 \"³\"")) {
			// Do("COMViewFilter.AfterDataFields","MEDPatient,,",1);m$.var("YVAR").get()
			// TODO REVISAR IMPLEMENTAÇÃO
		} else if (mFunctionUtil.isMatcher(cmdStr, "W $$^WWWTEXT(", ")")) {
			String var = mFunctionUtil.matcher(cmdStr, "W $$^WWWTEXT(", ")")[0];
			m$.Cmd.Write(m$.fnc$("WWWTEXT.main", var));
		} else if (cmdStr.startsWith("w")) {
			String str = "";
			String strCmd = "write";
			if (!cmdStr.startsWith("write")) {
				strCmd = "w";
			}
			str = cmdStr.replaceFirst(strCmd, "");
			m$.Cmd.Write(str);
		} else if (cmdStr.startsWith("W")) {
			String str = "";
			String strCmd = "WRITE";
			if (!cmdStr.startsWith("WRITE")) {
				strCmd = "W";
			}
			str = cmdStr.replaceFirst(strCmd, "");
			m$.Cmd.Write(str);
			// Do("COMViewFilter.AfterDataFields","MEDPatient,,",1);m$.var("YVAR").get()
			// TODO REVISAR IMPLEMENTAÇÃO
		}
		// USE
		else if (cmdStr.startsWith("USE ") || cmdStr.startsWith("use ") || cmdStr.startsWith("U ")
				|| cmdStr.startsWith("u ")) {
			// Use: No action.

		} else if (cmdStr.equals("IF +$P($G(^WWW012V(0,0,1)),Y,115)'=0 DO ^WWW012V")) {
			if (m$op.NotEqual(
					m$op.Positive(m$.Fnc.$piece(m$.Fnc.$get(m$.var("^WWW012V", 0, 0, 1)), m$.var("Y").get(), 115)), 0)) {
				m$.Cmd.Do("WWW012V");
			}
		} else if (cmdStr.equals("I $G(%(YQUERY,\"YCHART\"))'=\"\" DO CHART1^WWWMENU4")) {
			if (m$op.NotEqual(m$.Fnc.$get(m$.var("%YQUERY", "YCHART")), "")) {
				m$.Cmd.Do("CHART1^WWWMENU4");
			}
		} else {
			throw new UnsupportedOperationException("Implementation required for Xecute with command: '" + cmdStr + "'");
		}

	}

	public void zWrite(mVar var) {
		Object obj = String.valueOf("");
		for (;;) {
			ArrayList<Object> subL = new ArrayList<Object>(Arrays.asList(var.getSubs()));
			subL.add(obj);
			boolean wrLocal = false;
			if (Arrays.equals(var.getSubs(), m$.var("").getSubs())) {
				wrLocal = true;
			}
			mVar varAux = wrLocal ? m$.var("") : m$.var(subL.toArray());
			obj = m$.Fnc.$order(varAux);
			if (wrLocal && obj.toString().startsWith("^")) {
				continue;
			}

			if (String.valueOf(obj).isEmpty()) {
				break;
			}
			// ArrayList<Object> subDest = new
			// ArrayList<Object>(Arrays.asList(dest.getSubs()));
			// subDest.add(obj);

			ArrayList<Object> subOrig = new ArrayList<Object>(Arrays.asList(var.getSubs()));
			subOrig.add(obj);
			zWrite(m$.var(subOrig.toArray()));
			// merge(var(subDest.toArray()), var(subOrig.toArray()));
		}
		if (var.getValue() != null) {
			System.out.println(Arrays.toString(var.getSubs()) + "=" + var.getValue());
		}
	}
}
