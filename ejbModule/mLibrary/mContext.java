package mLibrary;

import $CSP.Request;
import $CSP.Response;
import $CSP.Session;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import mLibrary.exceptions.CommandException;
import mLibrary.exceptions.MethodNotFoundException;
import mSystem.$System;
import util.DataStructureUtil;
import util.mFunctionUtil;
import ORM.mTransactionManager;
import br.com.innovatium.mumps2java.dataaccess.MetadataDAO;
import br.com.innovatium.mumps2java.dataaccess.NoSQLMetadataDAOImpl;
import br.com.innovatium.mumps2java.dataaccess.SQLMetadataDAOImpl;
import br.com.innovatium.mumps2java.dataaccess.ServiceLocator;
import br.com.innovatium.mumps2java.dataaccess.ServiceLocatorException;
import br.com.innovatium.mumps2java.message.MessagePublisher;
import br.com.innovatium.mumps2java.message.MessagePublisherImpl;
import br.com.innovatium.mumps2java.todo.REMOVE;
import br.com.innovatium.mumps2java.todo.REVIEW;
import br.com.innovatium.mumps2java.todo.TODO;
import dataaccess.mDataAccess;
import dataaccess.mDataAccessLocal;
import dataaccess.mDataAccessMemory;
import dataaccess.mDataAccessPublic;
import dataaccess.mDataGlobalAccess;
import dataaccess.mVariables;

public class mContext implements Serializable {
	public static mContext createComAcessoABancoNoSQL() {
		return createComAcessoABancoNoSQL(true, null);
	}

	public static mContext createComAcessoABancoNoSQL(boolean useCache, HttpServletResponse response) {
		mContext m$ = new mContext();
		m$.init(response);
		try {
			MetadataDAO dao = ServiceLocator.locate(NoSQLMetadataDAOImpl.class);

			m$.mDataGlobal = new mDataGlobalAccess(useCache, m$.mVariables, dao);
			m$.transactionManager = new mTransactionManager();
			return m$;
		} catch (ServiceLocatorException e) {
			throw new IllegalStateException("Falha ao tentar localizar o servico de comunicacao com o banco de dados");
		}
	}

	public static mContext createComAcessoABancoNoSQL(HttpServletResponse response) {
		return createComAcessoABancoNoSQL(true, response);
	}

	public static mContext createComAcessoABancoSQL() {
		return createComAcessoABancoSQL(true, null);
	}

	public static mContext createComAcessoABancoSQL(boolean useCache) {
		return createComAcessoABancoSQL(useCache, null);
	}

	public static mContext createComAcessoABancoSQL(boolean useCache, Object response) {
		mContext m$ = new mContext();
		m$.init(response);
		try {
			MetadataDAO dao = ServiceLocator.locate(SQLMetadataDAOImpl.class);

			m$.mDataGlobal = new mDataGlobalAccess(useCache, m$.mVariables, dao);
			m$.transactionManager = new mTransactionManager();
			return m$;
		} catch (ServiceLocatorException e) {
			throw new IllegalStateException("Falha ao tentar localizar o servico de comunicacao com o banco de dados");
		}
	}

	public static mContext createComAcessoABancoSQL(Object response) {
		return createComAcessoABancoSQL(true, response);
	}

	public static mContext createSemAcesso() {
		mContext m$ = new mContext();
		m$.init(null);
		m$.mDataGlobal = new mDataAccessMemory(m$.mVariables, DataStructureUtil.GLOBAL);
		return m$;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 300466606302313649L;

	public mCommand Cmd;
	public mFunction Fnc;

	private int countDispatch;
	private int countNewVarExec;

	private String ioActual;

	private String ioDefault = "response";
	private Map<String, Object> ioMap = new TreeMap<String, Object>();
	private mDataAccess mDataGlobal;
	private mDataAccess mDataLocal;
	private mDataAccess mDataPublic;
	private Map<String, Method> methodMap;
	private Request mReq;
	private Response mRes;
	private Session mSes;
	private mVariables mVariables;
	private BufferedReader reader;
	private Map<String, Class<?>> stackedClasses = new HashMap<String, Class<?>>(30);
	private $System system;

	private int tLevel = 0;

	private mTransactionManager transactionManager;

	private Writer writer;

	private mContext() {
	}

	public String cacheDump() {
		return mDataGlobal.dump();
	}

	public Object call(Object... args) {
		return fnc$(args);
	}

	public void Close(Object io) {
		this.Cmd.Close(io);
	}

	/*
	 * Estudar uma estrategia para executar o metodo quando nao temos declarado
	 * o nome da classe a qual ele pertence, por exmplo: 1) Com nome definido:
	 * WWWConsys.main 2) Sem nome definido: calcular. Nesse caso estamos supondo
	 * que esse metodo pertence ao ultimo mClass em execucao na pilha.
	 */
	@TODO
	public String defineMethodName(mClass objClass, String methodName) {
		if (methodName != null && !methodName.contains(".")) {
			if (methodName.contains("^")) {
				methodName = defineMethodName(methodName);
			} else if (objClass != null) {
				methodName = objClass.getClass().getCanonicalName().concat(".").concat(methodName);
			} else {
				Throwable thr = new Throwable();
				thr.fillInStackTrace();
				StackTraceElement[] ste = thr.getStackTrace();
				String className = null;
				Class<?> clazz = null;
				for (int i = 0; i < ste.length; i++) {
					className = ste[i].getClassName();
					clazz = stackedClasses.get(className);
					if (clazz == null) {
						try {
							clazz = Class.forName(className);
						} catch (ClassNotFoundException e) {
							throw new IllegalArgumentException("Can not execute the method " + methodName
									+ " because there is no one classe implementing it.", e);
						}
						stackedClasses.put(className, clazz);
					}
					if (mClass.class.isAssignableFrom(clazz)) {
						methodName = className.concat(".").concat(methodName);
						break;
					}
				}

			}
		}
		return methodName;
	}

	public String defineMethodName(String methodName) {
		if (methodName == null) {
			return null;
		}
		final String[] method = methodName.split("\\^");
		if (method.length > 1) {
			return method[1] + "." + (method[0].isEmpty() ? "main" : method[0]);
		} else if (method.length == 1) {
			return method[0] + ".main";
		} else {
			return null;
		}
	}

	public Object dispatch(boolean returnRequired, boolean isJobExec, mClass objClass, String methodName,
			Object... parameters) {
		Object result = null;
		Object obj = null;
		int countNewVarTemp = countNewVarExec;

		countNewVarExec = 0;
		countDispatch++;
		String $ztrap = var("$ztrap").getSuppressedNullValue().toString();
		newVar(var("$ztrap"));
		var("$ztrap").set($ztrap);

		try {
			if (objClass != null) {
				String methodObjName = methodName.replace(objClass.getClass().getCanonicalName() + ".", "");
				while (methodObjName.indexOf('.') >= 0) {
					objClass = (mClass) this.prop(objClass, methodObjName.substring(0, methodObjName.indexOf('.')))
							.get();
					methodObjName = methodObjName.substring(methodObjName.indexOf('.') + 1);
					methodName = objClass.getClass().getCanonicalName() + "." + methodObjName;
				}
			}
			Method m = getMethod(methodName);
			if (objClass != null) {
				obj = objClass;
			} else if (!Modifier.isStatic(m.getModifiers())) {
				obj = Class.forName(methodName.substring(0, methodName.lastIndexOf("."))).newInstance();

				if (obj instanceof mClass) {
					// This was done because in the job threads we have sharing
					// memory mContext, so, to get isolation we must create a
					// new context and into this the thread can create variables
					// in such way that will not conflict with mContext of the
					// request.
					// ((mClass) obj).init(new mContext(true,
					// getResponse().getOriginalResponse()));
					// ((mClass) obj).init(new mContext(true, null));
					((mClass) obj).init(this);

				}
			}
			if (m.getParameterTypes() != null && m.getParameterTypes().length > 0 && m.getParameterTypes()[0].isArray()) {
				parameters = new Object[] { parameters };
			}
			if (m.getReturnType().equals(Void.TYPE)) {
				m.invoke(obj, parameters);
			} else {
				result = m.invoke(obj, parameters);
			}
			if (returnRequired && result == null) {
				throw new CommandException("NULL result to required return. Fail to execute method: " + methodName);
			}
		} catch (MethodNotFoundException e) {// TODO REVISAR TRATAMENTO DE ERROS
			// PARA DIFERENCIAR CLASSES E
			// ROTINAS.
			result = zTrapException((mClass) obj, returnRequired, "<NOLINE>" + methodName, e);
			/*
			 * if (!method.isEmpty()) { try { if(returnRequired){ result =
			 * fnc$(defineMethodName(method)); }else{
			 * Do(defineMethodName(method)); } } catch (Exception e2) {
			 * 
			 * countDispatch--;
			 * 
			 * throw new IllegalStateException(
			 * "Fail to execute method from $ztrap: " + method, e); } }
			 */
		} catch (ClassNotFoundException e) {
			result = zTrapException((mClass) obj, returnRequired, "<NOROUTINE>" + methodName, e);
		} catch (CommandException e) {
			result = zTrapException((mClass) obj, returnRequired, "<COMMAND>" + e.getClass().getCanonicalName()
					+ " in " + methodName + " > " + e.getMessage(), e);
		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			result = zTrapException((mClass) obj, returnRequired, "<Java Exception>" + t.getClass().getCanonicalName()
					+ " in " + methodName + " > " + t.getMessage(), t);
		} catch (Throwable e) {
			countDispatch--;
			throw new IllegalStateException("Fail to execute method: " + methodName + " and its parameters: "
					+ Arrays.deepToString(parameters), e);
		}

		restoreVarBlock(1);
		restoreVar(countNewVarExec);
		countNewVarExec = countNewVarTemp;

		countDispatch--;

		return result;
	}

	public Object dispatch(boolean returnRequired, mClass objClass, String methodName, Object... parameters) {
		return dispatch(returnRequired, false, objClass, methodName, parameters);
	}

	public void Do(mClass objClass, String methodName, Object... parameters) {
		this.Cmd.Do(objClass, methodName, parameters);
	}

	public void Do(Object methodName) {
		this.Cmd.Do(methodName);
	}

	public void Do(String methodName) {
		this.Cmd.Do(methodName);
	}

	public void Do(String methodName, Object... parameters) {
		this.Cmd.Do(methodName, parameters);
	}

	public String dumpLocal() {
		return mDataLocal.dump();
	}

	public mVar extractVar(mVar var) {
		return new mExtractVar(var);
	}

	/*
	 * @Override protected void finalize() throws Throwable {
	 * removeIO(this.ioActual); super.finalize(); }
	 */

	public mVar extractVar(mVar var, Object from) {
		return new mExtractVar(var, from);
	}

	public mVar extractVar(mVar var, Object from, Object to) {
		return new mExtractVar(var, from, to);
	}

	private Map<mDataAccess, Object[]> filteringVariableTypes(mVar... variables) {
		Map<mDataAccess, Object[]> map = new HashMap<mDataAccess, Object[]>();
		List<String> locals = new ArrayList<String>();
		List<String> publics = new ArrayList<String>();
		List<String> globals = new ArrayList<String>();

		String name = null;
		for (mVar var : variables) {
			name = var.getSubs()[0] == null ? null : var.getSubs()[0].toString();
			if (DataStructureUtil.isPublic(name)) {
				publics.add(name);
			} else if (DataStructureUtil.isGlobal(name)) {
				globals.add(name);
			} else {
				locals.add(name);
			}
		}

		map.put(this.mDataLocal, locals.toArray());
		return map;
	}

	public Object fnc$(Object... args) {
		Object[] parameters = null;
		String methodName = "";
		mClass objClassArg = null;
		if (args != null && args.length > 0) {
			int initParam = 1;
			if (args[0] instanceof String) {
				methodName = (String) args[0];
			} else if (args[0] instanceof mClass) {
				objClassArg = (mClass) args[0];
				methodName = (String) args[1];
				initParam = 2;
			}

			if (args.length > initParam) {
				if (args[initParam] != null && args[initParam].getClass().isArray()) {
					parameters = (Object[]) args[initParam];
				} else {
					parameters = Arrays.copyOfRange(args, initParam, args.length);
				}
			}
		}

		methodName = defineMethodName(objClassArg, methodName);
		if ((parameters == null) || (parameters.length == 0)) {
			return dispatch(true, objClassArg, methodName);
		} else {
			return dispatch(true, objClassArg, methodName, parameters);
		}
	}

	public mFunction getFunction() {
		return Fnc;
	}

	public Object getIO() {
		return this.ioActual;
	}

	private mDataAccess getMDataAccess(Object[] subs) {
		if (DataStructureUtil.isPublic(subs)) {
			return mDataPublic;
		} else if (DataStructureUtil.isGlobal(subs)) {
			return mDataGlobal;
		} else {
			return mDataLocal;
		}
	}

	public mDataAccess getmDataGlobal() {
		return mDataGlobal;
	}

	public mDataAccess getmDataLocal() {
		return mDataLocal;
	}

	public mDataAccess getmDataPublic() {
		return mDataPublic;
	}

	private Method getMethod(String methodName) throws NoSuchMethodException, ClassNotFoundException {
		if (methodMap == null) {
			methodMap = new HashMap<String, Method>(20);
		}

		Method m = methodMap.get(methodName);
		if (m == null) {
			int lastIndex = methodName.lastIndexOf(".");
			final String clazz = methodName.substring(0, lastIndex);
			final String method = methodName.substring(lastIndex + 1);

			try {
				Class<?> clazz1 = Class.forName(clazz);

				while (clazz1 != null) {

					Method[] methods = Class.forName(clazz).getMethods();
					// Method[] methods = Macros.class.getMethods();
					for (Method met : methods) {
						if (method.equals(met.getName())) {
							m = met;
							break;
						}

					}
					if (m != null) {
						break;
					}
					clazz1 = clazz1.getSuperclass();

				}
			} catch (ClassNotFoundException e) {
				throw e;
			} catch (Exception e) {
				throw new IllegalArgumentException("Fail to find method name: " + methodName, e);
			}
			methodMap.put(methodName, m);
		}

		if (m == null) {
			throw new MethodNotFoundException("The method " + methodName + " does not exist");
		}

		return m;
	}

	@REMOVE
	private Object getMSaveMethod(Object[] subs) {
		if (subs.length < 1) {
			return null;
		}
		if (!(subs[0] instanceof String)) {
			return null;
		}
		// 0/4/null - Dados Persistentes (Relational)
		// 90 - Metadados (Relational/Non Relational)
		// 91 - Parâmetros (Relational/Non Relational)
		// 92 - Auxiliar (Non Relational)
		// 99 - Transitório (sem persistência)
		return this.var("^WWWGLOBALSAVEPROC", subs[0]).getValue();
	}

	public BufferedReader getReader() {
		if (this.reader == null) {
			Object io = ioMap.get(getIO());
			if (io instanceof Object[]) {
				io = ((Object[]) io)[0];
			}
			if (io instanceof File) {
				File res = (File) io;
				try {
					this.reader = new BufferedReader(new FileReader(res));
				} catch (FileNotFoundException e) {
					throw new IllegalArgumentException("The reader io not found.");
				}
			}
			if (io instanceof URL) {
				URL res = (URL) io;
				try {
					this.reader = new BufferedReader(new StringReader(String.valueOf(res.getContent())));
				} catch (FileNotFoundException e) {
					throw new IllegalArgumentException("The reader io not found.");
				} catch (IOException e) {
					throw new IllegalArgumentException("The reader io not found.");
				}
			}
		}
		return reader;
	}

	public Request getRequest() {
		return mReq;
	}

	public Response getResponse() {
		return mRes;
	}

	public Session getSession() {
		return mSes;
	}

	public $System getSystem() {
		return system;
	}

	public Object getTLevel() {
		return this.tLevel;
	}

	@REVIEW(description = "escrita de arquivos em modo append não posiciona ao fim do arquivo.")
	public Writer getWriter() {
		if (this.writer == null) {
			Object io = ioMap.get(getIO());
			String parameters = "";
			if (io instanceof Object[]) {
				Object[] tmp = ((Object[]) io);
				io = tmp[0];
				parameters = tmp[1].toString().toLowerCase();
			}
			if (io instanceof HttpServletResponse) {
				HttpServletResponse res = (HttpServletResponse) io;
				try {
					this.writer = res.getWriter();
				} catch (IOException e) {
					throw new IllegalArgumentException("The writer strategy must not be empty.");
				}
			} else if (io instanceof StringWriter) {
				this.writer = (StringWriter) io;
			} else if (io instanceof File) {
				File res = (File) io;
				try {
					if (parameters.contains("n")) {
						// Parameter to create new file
						res.getParentFile().mkdirs();
						res.delete();
						res.setExecutable(true);
						res.setReadable(true);
						res.setWritable(true);
						res.createNewFile();
					}
					this.writer = new BufferedWriter(new FileWriter(res, true));
				} catch (IOException e) {
					throw new IllegalArgumentException("The writer strategy must not be empty.", e);
				}
			}
		}
		return writer;
	}

	public Object Goto(Object label) {
		return this.Cmd.Goto(label);
	}

	public void Hang(Object obj) {
		this.Cmd.Hang(obj);
	}

	public boolean hasPublicVariables() {
		return !mDataPublic.isEmpty();
	}

	public mVar indirectVar(Object val) {
		return var(parseVarSubs(val.toString()));
	}

	private void init(Object response) {
		this.Fnc = new mFunction(this);
		this.Cmd = new mCommand(this);
		this.system = new $System(this);
		this.mVariables = new mVariables();

		this.mDataPublic = new mDataAccessPublic(mVariables);
		this.mDataLocal = new mDataAccessLocal(mVariables);
		/*
		 * Object io = new StringWriter(); ioMap.put(this.ioDefault, io);
		 */
		ioMap.put(this.ioDefault, response);
		useIO(this.ioDefault);
		if (mRes == null && response != null) {
			mRes = new Response((HttpServletResponse) response);
		}
	}

	public void Job(String methodName) {
		Job(methodName, (Object[]) null);
	}

	public void Job(String methodName, Object... parameters) {
		try {
			MessagePublisher publisher = ServiceLocator.locate(MessagePublisherImpl.class);
			publisher.newJob(methodName, parameters);
		} catch (ServiceLocatorException e) {
			throw new IllegalStateException("Fail lookup the service to publish a new job emulating cache's job", e);
		}
	}

	public mVar lastVar() {
		return var(mDataGlobal.getCurrentSubs());
	}

	public mVar lastVar(Object... subs) {
		Object[] concat = mFunctionUtil.concatSinceLastSubscript(mDataGlobal.getCurrentSubs(), subs);
		return var(concat);
	}

	public void Lock(mVar var, int i) {
		this.Cmd.Lock(var, i);

	}

	public void Lock(String string, String string2, String string3) {
		this.Cmd.Lock(string, string2, string3);
	}

	public void LockInc(mVar var) {
		this.Cmd.LockInc(var);

	}

	public void LockInc(mVar var, int i) {
		this.Cmd.LockInc(var, i);
	}

	@TODO
	public void merge(mVar dest, mVar orig) {

		Object obj = String.valueOf("");
		for (;;) {
			ArrayList<Object> subL = new ArrayList<Object>(Arrays.asList(orig.getSubs()));
			subL.add(obj);

			obj = Fnc.$order(var(subL.toArray()));
			if (String.valueOf(obj).isEmpty()) {
				break;
			}
			ArrayList<Object> subDest = new ArrayList<Object>(Arrays.asList(dest.getSubs()));
			subDest.add(obj);

			ArrayList<Object> subOrig = new ArrayList<Object>(Arrays.asList(orig.getSubs()));
			subOrig.add(obj);
			merge(var(subDest.toArray()), var(subOrig.toArray()));
		}
		// trata cabeça da variável, pois os descendentes já foram tratados.
		Object valOrig = orig.getValue();
		if (valOrig != null) {
			dest.set(valOrig);
		}
		// dest.merge(orig);
	}

	public void Merge(mVar target, mVar source) {
		this.Cmd.Merge(target, source);

	}

	@REVIEW(author = "vinicius", date = "25/08/2014", description = "Revisar se teremos um empilhamento de diferentes tipos de variaveis em uma unica chamada, para simplificarmos o codigo")
	public void newVar(mVar... vars) {
		if (vars.length == 0) {
			newVarExcept();
		} else {
			Map<mDataAccess, Object[]> maps = filteringVariableTypes(vars);
			Set<Entry<mDataAccess, Object[]>> set = maps.entrySet();
			for (Entry<mDataAccess, Object[]> entry : set) {
				entry.getKey().stacking(entry.getValue());
			}
			countNewVarExec++;
		}
	}

	@REVIEW(author = "vinicius", date = "25/08/2014", description = "Revisar se teremos um empilhamento de diferentes tipos de variaveis em uma unica chamada, para simplificarmos o codigo")
	public void newVarBlock(int blockIndex, mVar... vars) {
		Map<mDataAccess, Object[]> maps = filteringVariableTypes(vars);
		Set<Entry<mDataAccess, Object[]>> set = maps.entrySet();

		for (Entry<mDataAccess, Object[]> entry : set) {
			entry.getKey().stackingBlock(blockIndex, countDispatch, entry.getValue());
		}
	}

	@REVIEW(author = "vinicius", date = "25/08/2014", description = "Revisar se teremos um empilhamento de diferentes tipos de variaveis em uma unica chamada, para simplificarmos o codigo")
	public void newVarExcept(mVar... vars) {
		Map<mDataAccess, Object[]> maps = filteringVariableTypes(vars);
		Set<Entry<mDataAccess, Object[]>> set = maps.entrySet();
		for (Entry<mDataAccess, Object[]> entry : set) {
			entry.getKey().stackingExcept(entry.getValue());
		}
		countNewVarExec++;
	}

	@REVIEW(author = "vinicius", date = "25/08/2014", description = "Revisar se teremos um empilhamento de diferentes tipos de variaveis em uma unica chamada, para simplificarmos o codigo")
	public void newVarExceptBlock(int blockIndex, mVar... vars) {
		Map<mDataAccess, Object[]> maps = filteringVariableTypes(vars);
		Set<Entry<mDataAccess, Object[]>> set = maps.entrySet();

		for (Entry<mDataAccess, Object[]> entry : set) {
			entry.getKey().stackingExceptBlock(blockIndex, countDispatch, entry.getValue());
		}
	}

	/**
	 * Inicialização de parâmetros em métodos instanciados contemplando
	 * passagem de variáveis por referência e por valor (initialization
	 * parameters in methods instantiated contemplating passing variables by
	 * reference and by value)
	 * 
	 * @param _p
	 *            array of parameters
	 * @param index
	 *            parameter index
	 * @param name
	 *            parameter name
	 * @return parameter variable
	 */
	public mVar newVarParamRef(Object[] _p, Integer index, String name) {
		return newVarParamRef(_p, index, name, null);
	}

	/**
	 * Inicialização de parâmetros em métodos instanciados contemplando
	 * passagem de variáveis por referência e por valor (initialization
	 * parameters in methods instantiated contemplating passing variables by
	 * reference and by value)
	 * 
	 * @param _p
	 *            array of parameters
	 * @param index
	 *            parameter index
	 * @param name
	 *            parameter name
	 * @param valueDefault
	 *            parameter default value
	 * @return parameter variable
	 */
	public mVar newVarParamRef(Object[] _p, Integer index, String name, Object valueDefault) {
		return simulatingVariableThroughReference(name,
				(((_p != null) && (_p.length >= index)) ? _p[index - 1] : null), valueDefault, true);
	}

	@Deprecated
	public mVar newVarRef(String name, Object variable) {
		return newVarRef(name, variable, null);
	}

	@Deprecated
	public mVar newVarRef(String name, Object variable, Object valueDefault) {
		return simulatingVariableThroughReference(name, variable, valueDefault, true);
	}

	public void Open(Object fileName) {
		this.Cmd.Open(fileName);
	}

	public void Open(Object devicename, Object parameters, Object timeout) {
		this.Cmd.Open(devicename, parameters, timeout);
	}

	public void Open(Object object, String string) {
		this.Cmd.Open(object, string, 0);
	}

	public void Open(Object devicename, String parameters, int timeout) {
		this.Cmd.Open(devicename, parameters, timeout);
	}

	public void Open(Object object, String string, Object concat, int i) {
		this.Cmd.Open(object, string, concat, i);
	}

	public mParameter param(mClass instanceMClas, String parameterName) {
		mParameter parameter;
		try {
			parameter = new mParameter(instanceMClas.getClass().getField(parameterName));
		} catch (NoSuchFieldException e) {
			parameter = null;
		} catch (SecurityException e) {
			parameter = null;
		}
		return parameter;
	}

	public Object[] parseCall(String _content) {
		return parseExp(_content, true);
	}

	private Object[] parseExp(String _content, Boolean _call) {
		final List<Object> _result = new ArrayList<Object>();
		int _level = 0, y = 0, x = 0;
		boolean _isstring = false;
		for (x = 0; x < _content.length(); x++) {
			if (_content.charAt(x) == '"') {
				_isstring = (_isstring) ? false : true;
			}
			if (_isstring) {
				continue;
			}
			if (_content.charAt(x) == '(') {
				if (_level == 0 && x > y) {
					_result.add(_content.substring(y, x));
					y = x + 1;
				}
				_level++;
			} else if (_content.charAt(x) == ')') {
				_level--;
				if (_level == 0) {
					if (x > y) {
						if ((_result.size() > 0) && (_result.get(0).toString().equalsIgnoreCase("$g"))) {
							_result.add(parseVar(_content.substring(y, x)));
						} else if ((_call) && (_result.size() == 1)
								&& (_result.get(0).toString().equalsIgnoreCase("##class"))) {
							_result.add(_content.substring(y, x));
						} else {
							_result.add(parseVarValue(_content.substring(y, x)));
						}
					}
					y = x + 1;
				}
			} else if (_content.charAt(x) == ',') {
				if (_level == 1) {
					if (x > y) {
						if ((_result.size() > 0) && (_result.get(0).toString().toLowerCase().startsWith("$g"))) {
							_result.add(parseVar(_content.substring(y, x)));
						} else if ((_call) && (_result.size() == 1)
								&& (_result.get(0).toString().toLowerCase().startsWith("##class"))) {
							_result.add(_content.substring(y, x));
						} else {
							_result.add(parseVarValue(_content.substring(y, x)));
						}
					} else if (x == y) {
						_result.add(null);
					}
					y = x + 1;
				}
			}
		}
		if (x > y) {
			_result.add(_content.substring(y, x));
		}
		if ((_result.size() > 1) && (_result.get(0).toString().toLowerCase().startsWith("$g"))) {
			Object _resultGet;
			if (_result.size() > 2) {
				_resultGet = mFunction.$get(_result.get(1), _result.get(2));
			} else {
				_resultGet = mFunction.$get(_result.get(1));
			}
			_result.clear();
			_result.add(_resultGet);
		}
		return _result.toArray();
	}

	private Object parseVar(String _content) {
		return parseVar(_content, false);
	}

	private Object parseVar(String _content, Boolean _value) {
		Object _result;
		if (_content == null) {
			_result = "";
		} else if (_content.length() == 0) {
			_result = _content;
		} else if (_content.charAt(0) == '"') {
			_result = _content.replaceAll("\"(.*)\"", "$1").replaceAll("\"\"", "\"");
		} else if (Character.isDigit(_content.charAt(0))) {
			_result = _content;
		} else if (_content.trim().matches("[\\+\\-]?[\\d\\.]+")) {
			_result = _content.trim().replaceAll("([\\+\\-]?[\\d\\.]+)(.*)", "$1");
		} else {
			if (_value) {
				if (_content.toLowerCase().startsWith("$get") || _content.toLowerCase().startsWith("$g(")) {
					Object[] _resultSubs = parseVarSubs(_content);
					_result = ((_resultSubs.length > 0) ? _resultSubs[0] : null);
				} else if (_content.toLowerCase().startsWith("$piece") || _content.toLowerCase().startsWith("$p(")) {
					Object[] _resultSubs = parseVarSubs(_content);

					if (_resultSubs.length <= 3) {
						_result = mFunction.$piece(_resultSubs[1], _resultSubs[2]);
					} else if (_resultSubs.length <= 4) {
						_result = mFunction.$piece(_resultSubs[1], _resultSubs[2], _resultSubs[3]);
					} else {
						_result = mFunction.$piece(_resultSubs[1], _resultSubs[2], _resultSubs[3], _resultSubs[4]);
					}
				} else if (_content.startsWith(".")) {
					_result = var(parseVarSubs(_content.substring(1)));
				} else {
					_result = var(parseVarSubs(_content)).getValue();
				}
			} else {
				_result = var(parseVarSubs(_content));
			}
		}
		return ((_result == null) ? "" : _result);
	}

	private Object[] parseVarSubs(String _content) {
		return parseExp(_content, false);
	}

	public Object parseVarValue(String _content) {
		return parseVar(_content, true);
	}

	public mVar pieceVar(mVar var, Object del) {
		return new mPieceVar(var, del, 1, 0);
	}

	public mVar pieceVar(mVar var, Object del, Object ipos) {
		return new mPieceVar(var, del, ipos, null);
	}

	public mVar pieceVar(mVar var, Object del, Object ipos, Object epos) {
		// TODO Auto-generated method stub
		return new mPieceVar(var, del, ipos, epos);
	}

	public void populateParameter(Map<String, String[]> map) {
		Set<Entry<String, String[]>> results = map.entrySet();
		for (Entry<String, String[]> result : results) {
			mDataPublic.subs("%request.Data", result.getKey()).set(result.getValue()[0]);
		}
	}

	public mProperty prop(Object object, String string) {
		if (object != null) {
			return new mProperty(object, string);
		}
		return null;
	}

	public void putIO(String deviceName, Object io) {
		ioMap.put(deviceName, io);
	}

	public void Read(Object... parameters) {
		this.Cmd.Read(parameters);
	}

	public void release() {
		TRollback();
	}

	public void removeIO(String deviceName) {
		Object io = ioMap.remove(deviceName);
		if (io instanceof Object[]) {
			Object[] tmp = ((Object[]) io);
			io = tmp[0];
		}
		if (io instanceof File) {
			File res = (File) io;
			try {
				BufferedReader br = new BufferedReader(new FileReader(res));
				br.close();
				System.gc();
			} catch (IOException e) {
				throw new IllegalArgumentException("The writer strategy must not be empty.", e);
			}
		}

		useIO(this.ioDefault);
	}

	public void restoreVar(int indexNewVar) {
		if (indexNewVar <= 0) {
			return;
		}
		while (indexNewVar-- > 0) {
			mDataLocal.unstacking();
		}
	}

	public void restoreVarBlock(int blockIndex) {
		mDataLocal.unstackingBlock(blockIndex, countDispatch);
	}

	public void setRequest(Request mReq) {
		mReq.setContext(this);
		this.mReq = mReq;

	}

	public void setSession(Session mSes) {
		this.mSes = mSes;
	}

	/**
	 * Inicialização de parâmetros em métodos estáticos contemplando
	 * passagem de variáveis por referência e por valor (initialization
	 * parameters contemplating passing variables by reference and by value)
	 * 
	 * @param name
	 *            parameter name
	 * @param variable
	 *            variable passed by reference or by value
	 * @param valueDefault
	 *            parameter default value
	 * @param stackingNeeded
	 *            stacking variable condition
	 * @return parameter variable
	 */
	private mVar simulatingVariableThroughReference(String name, Object variable, Object valueDefault,
			boolean stackingNeeded) {
		if (variable instanceof mVar) {
			mVar result = (mVar) variable;
			if (result.getValue() == null) {
				result.set(valueDefault);
			}
			return (mVar) result;
		} else {
			mVar var = var(name);

			if (stackingNeeded) {
				newVar(var);
			}

			if (variable != null) {
				var.set(variable);
			} else if (valueDefault != null) {
				var.set(valueDefault);
			} else if (!stackingNeeded) {
				var.set("");
			}
			return var;
		}
	}

	public void SQL() {
		this.Cmd.SQL();
	}

	public void TCommit() {
		// REVIEW Para compatibilidade com o Cach� o COMMIT deveria ser
		// realizado
		// somente quando o tLevel fosse <= 1
		transactionManager.commit();
		this.tLevel = this.tLevel > 0 ? this.tLevel - 1 : 0;
	}

	public void TRollback() {
		transactionManager.rollback();
		this.tLevel = 0;
	}

	public void TStart() {
		transactionManager.start();
		this.tLevel = this.tLevel + 1;
	}

	public void Unlock(mVar var) {
		this.Cmd.Unlock(var);
	}

	public void Unlock(mVar var, String str) {
		this.Cmd.Unlock(var, str);
	}

	public void Unlock(String string) {
		this.Cmd.Unlock(string);
	}

	public void Use(Object io) {
		this.Cmd.Use(io);
	}

	public void useIO(String deviceName) {
		this.writer = null;
		this.reader = null;
		this.ioActual = deviceName;
	}

	public mVar var(Object... subs) {
		final boolean isEmpty = subs.length >= 1 && "".equals(subs[0]);
		if (isEmpty) {
			return new mVar(subs, mDataLocal);
		}
		if (subs == null || subs.length == 0) {
			return null;
		}
		final String varName = mFunctionUtil.castString(subs[0]);
		if (Character.isDigit(varName.charAt(0))) {
			return null;
		}
		// variáveis especiais são 'case insentive'
		if (varName.startsWith("$")) {
			subs[0] = varName.toLowerCase();
		}
		return new mVar(subs, getMDataAccess(subs));
	}

	/**
	 * Inicialização de parâmetros em métodos estáticos contemplando
	 * passagem de variáveis por referência e por valor (initialization
	 * parameters for static methods contemplating passing variables by
	 * reference and by value)
	 * 
	 * @param _p
	 *            array of parameters
	 * @param index
	 *            parameter index
	 * @param name
	 *            parameter name
	 * @return parameter variable
	 */
	public mVar varParamRef(Object[] _p, Integer index, String name) {
		return varParamRef(_p, index, name, null);
	}

	/**
	 * Inicialização de parâmetros em métodos estáticos contemplando
	 * passagem de variáveis por referência e por valor (initialization
	 * parameters for static methods contemplating passing variables by
	 * reference and by value)
	 * 
	 * @param _p
	 *            array of parameters
	 * @param index
	 *            parameter index
	 * @param name
	 *            parameter name
	 * @param valueDefault
	 *            parameter default value
	 * @return parameter variable
	 */
	public mVar varParamRef(Object[] _p, Integer index, String name, Object valueDefault) {
		return simulatingVariableThroughReference(name,
				(((_p != null) && (_p.length >= index)) ? _p[index - 1] : null), valueDefault, false);
	}

	@Deprecated
	public mVar varRef(String name, Object ref) {
		return varRef(name, ref, null);
	}

	@Deprecated
	public mVar varRef(String name, Object ref, Object valueDefault) {
		return simulatingVariableThroughReference(name, ref, valueDefault, false);
	}

	public void Write(Object... string) {
		this.Cmd.Write(string);
	}

	public void WriteHtml(Object... string) {
		this.Cmd.WriteHtml(string);
	}

	public void WriteJS(Object... string) {
		this.Cmd.WriteJS(string);
	}

	public void writeResponse() {
		/*
		 * useIO("response"); try {
		 * mRes.getOriginalResponse().getWriter().write(((StringWriter)
		 * this.getWriter()).getBuffer().toString()); } catch (IOException e) {
		 * e.printStackTrace(); }
		 */
	}

	public void Xecute(Object command) {
		this.Cmd.Xecute(command);
	}

	/*
	 * private Integer increaseBlock(Integer blockIndex) { Integer[] key =
	 * {blockIndex, countDispatch}; mapBlockIndex.put(key, countDispatch);
	 * return -1; }
	 * 
	 * private Integer decreaseBlock(int blockIndex) { Integer count =
	 * mapBlockIndex.get(blockIndex); int temp = -1; if (count == null) { count
	 * = 1; }
	 * 
	 * temp = count; mapBlockIndex.put(blockIndex, --count); return temp; }
	 */

	public Object zTrapException(mClass clazz, boolean returnRequired, String zerrorMsg, Throwable t) {
		var("$ZERROR").set(zerrorMsg);
		String method = var("$ztrap").getSuppressedNullValue().toString();
		if (t != null) {
			if (!zerrorMsg.contains("<NOLINE>") && !zerrorMsg.contains("<COMMAND>")) {
				t.printStackTrace();
			} else {
				if (method.isEmpty()) {
					t.printStackTrace();
				} else {
					System.err.println(t.getClass().getCanonicalName() + ": " + t.getMessage() + " in zTrapException");
				}
			}
		}
		if (!method.isEmpty()) {
			var("$ztrap").set("");
			if (method.contains(".") || method.contains("^")) {
				method = defineMethodName(method);
			} else {
				method = defineMethodName(clazz, method);
			}
			if (returnRequired) {
				return fnc$(method);
			} else {
				Do(method);
			}
		}
		return null;
	}

	public void zWrite(mVar var) {
		this.Cmd.zWrite(var);
	}
}
