package $Library;

import java.lang.reflect.Method;
import java.sql.Clob;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import mLibrary.m$op;
import mLibrary.mClass;
import mLibrary.mContext;
import util.SQLUtil;
import util.mFunctionUtil;
import ORM.mJDBCObject;
import br.com.innovatium.mumps2java.dataaccess.ResultSetDAO;
import br.com.innovatium.mumps2java.dataaccess.ResultSetDAOImpl;
import br.com.innovatium.mumps2java.dataaccess.ServiceLocator;
import br.com.innovatium.mumps2java.dataaccess.ServiceLocatorException;
import br.com.innovatium.mumps2java.datastructure.GlobalCache;

public class ResultSet extends mClass {
	private String currentClause = null; 
	
	private String dataRecord = null;
	private Integer dataRecordFields = 0;
	private Boolean groupByClause = false;
	private Integer maxRecords = null;
	private final GlobalCache metadataCache = GlobalCache.getCache();
	private PreparedStatement prepare;
	private java.sql.ResultSet resultSet;
	private ResultSetDAO resultSetDao;
	private String RuntimeMode;
	private String sql = "";

	public ResultSet() {
		super();
		this.$New();
	}	
	
	public ResultSet(mContext context, Object... args) {
		super(context);
		this.$New(args);
	}

	public ResultSet(Object... args) {
		super();
		this.$New(args);
	}
	public Object $Close() {
		return Close();
	}
	public Object $New(Object... args) {
		try {
			resultSetDao = ServiceLocator.locate(ResultSetDAOImpl.class);
			String initvalue = (args!=null && args.length>=1) && args[0]!=null?args[0].toString():"";
			if(initvalue.contains(":")){
				sql = String.valueOf(m$.call(initvalue.replace(":", ".")));
				this.prepare = resultSetDao.createPreparedStatement(sql);
			}
		} catch (ServiceLocatorException e) {
			throw new IllegalArgumentException(
					"Fail to create data access object", e);
		}
		return this;
	}
	public Object Close() {
		try {
			resultSet.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return 0;
		}
		return 1;
	}		

	/*
	sql = "";
	this.prepare = null;
	String sqlPreffix = "";
	String[] sqlPreffixParams = null;
	if(srcSql.equals("select DISTINCT top 2001 %upper(MEDPatient.ID) AS ID from SQLUser.MEDPatient where MEDPatient.company = 0 ")){
		//this.prepare = "select DISTINCT upper(MEDPatient.ID) AS ID from SQLUser.MEDPatient where MEDPatient.company = 0 AND rownum <= 2001 ORDER BY ID";	
		sql = "select DISTINCT upper(MEDPatient.ID) AS ID_ROW,SQLUser.MEDPatient.* from SQLUser.MEDPatient where MEDPatient.company = 0 AND rownum <= 20 ORDER BY ID_ROW";
	}else if(srcSql.equals("select DISTINCT top 2001 %upper(MEDPatient.ID) AS ID from SQLUser.MEDPatient where MEDPatient.company = 0  order by %upper(+$piece(SQLUser.MEDPatient.DOB,\".\",1)) ")){
		sql = "select DISTINCT upper(MEDPatient.ID) AS ID_ROW,SQLUser.MEDPatient.* from SQLUser.MEDPatient where MEDPatient.company = 0 AND rownum <= 20 ORDER BY DOB";
	}else if(srcSql.equals("select DISTINCT top 2001 %upper(WWW013.ID) AS ID from SQLUser.WWW013 where  $$RemoveMark^COMViewSQL(%upper(SQLUser.WWW013.FREE7),\"0\",\"default\")  is not null AND  $$RemoveMark^COMViewSQL(%upper(SQLUser.WWW013.FREE7),\"0\",\"default\")  not in (\" \",\"\")  order by  $$RemoveMark^COMViewSQL(%upper(SQLUser.WWW013.Name),\"0\",\"default\")  ")){
		sql = "select DISTINCT upper(WWW013.ID) AS ID_ROW, SQLUser.WWW013.* from SQLUser.WWW013 where UPPER(SQLUser.WWW013.FREE7) is not null AND UPPER(SQLUser.WWW013.FREE7) not in(' ')  order by  UPPER(SQLUser.WWW013.Name)";
	}
	else if(mFunctionUtil.isMatcher(srcSql,"select DISTINCT top 2001 %upper(MEDPatient.ID) AS ID from SQLUser.MEDPatient where MEDPatient.company = 0  and P1 %startswith \"","\"  order by %upper(+$piece(SQLUser.MEDPatient.DOB,\".\",1))")/*prepare.matches(regex)startsWith(start = "select DISTINCT top 2001 %upper(MEDPatient.ID) AS ID from SQLUser.MEDPatient where MEDPatient.company = 0  and P1 %startswith \"") && prepare.contains("\"  order by %upper(+$piece(SQLUser.MEDPatient.DOB,\".\",1)) ")* //){
		String param = mFunctionUtil.matcher(srcSql,"select DISTINCT top 2001 %upper(MEDPatient.ID) AS ID from SQLUser.MEDPatient where MEDPatient.company = 0  and P1 %startswith \"","\"  order by %upper(+$piece(SQLUser.MEDPatient.DOB,\".\",1))")[0];
		sql = "select DISTINCT upper(MEDPatient.ID) AS ID_ROW,SQLUser.MEDPatient.* from SQLUser.MEDPatient where MEDPatient.company = 0 AND MEDPatient.name like ?"
				+ " and rownum <= 20 ORDER BY DOB";
		//"select DISTINCT top 2001 %upper(MEDPatient.ID) AS ID from SQLUser.MEDPatient where MEDPatient.company = 0  and P1 %startswith \"rena\"  order by %upper(+$piece(SQLUser.MEDPatient.DOB,\".\",1)) "
		 try {
				this.prepare = resultSetDao.createPreparedStatement(sql);
				this.prepare.setString(1, param+"%");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
	}// Alerta de Reposição
	else if(srcSql.trim().equals("select DISTINCT VARAlertaLocalLinha.ID from SQLUser.VARAlertaLocalLinha where VARAlertaLocalLinha.company = 0  and  $$RemoveMark^COMViewSQL(SQLUser.VARAlertaLocalLinha.Location,\"0\",\"default\")  = 1 order by  $$RemoveMark^COMViewSQL(SQLUser.VARAlertaLocalLinha.Produto,\"0\",\"default\")")){
		sql = "select DISTINCT VARAlertaLocalLinha.ID as ID from SQLUser.VARAlertaLocalLinha where VARAlertaLocalLinha.company = 0  and  SQLUser.VARAlertaLocalLinha.Location = '1' and rownum <= 10 order by  SQLUser.VARAlertaLocalLinha.Produto ";		
	} else if (srcSql.contains("VARAta ")) { // TODO : Revisar Ata.  Alerta de Reposição
		sql = "select '' from SQLUser.VARAlertaLocalLinha Where Id=''"; // objetivo: nenhum resultado.
	}
	else if((sqlPreffixParams = mFunctionUtil.matcher(srcSql,"select DISTINCT "," from "))!=null && sqlPreffixParams.length>0 /*prepare.matches(regex)startsWith(start = "select DISTINCT top 2001 %upper(MEDPatient.ID) AS ID from SQLUser.MEDPatient where MEDPatient.company = 0  and P1 %startswith \"") && prepare.contains("\"  order by %upper(+$piece(SQLUser.MEDPatient.DOB,\".\",1)) ")  * //){ // ,".company = 0 "
		String[] params = null;
		StringBuilder criteriaBuilder = new StringBuilder();
		String selectFields = sqlPreffixParams[0];
		String table = sqlPreffixParams[1].substring(0, sqlPreffixParams[1].indexOf(" "));
		String value = "";
		String field = "";	
		//String[] sqlMumpsSelect = sqlMumps.split(sqlPreffix);top 2001 %upper(",".ID) AS ID
		List<String> valueList = new ArrayList<>();
		if(sqlPreffixParams.length==2){
		String sqlMumpsSuffix = sqlPreffixParams[1];
		Map<String,String> sqlMap = mFunctionUtil.generateSqlSentenceMap(sqlMumpsSuffix);
		// Now create matcher object.
		//Matcher m = Pattern.compile(" where | order by ").matcher(sqlMumpsSuffix).groupCount();
		String whereParam = Objects.toString(sqlMap.get(" where "), "");
		String[] sqlMumpsSuffixArray = whereParam.split(" and ");
		for (int i = 0; i < sqlMumpsSuffixArray.length; i++) {
			String criteriaField = " like ?";
			String sqlMumpsSuffixItem = sqlMumpsSuffixArray[i];
			value = "";
			if(sqlMumpsSuffixItem.isEmpty()){
				continue;
			}

			if(mFunctionUtil.isMatcher(sqlMumpsSuffixItem,"$find(",",",") > 1 ")){
				params = mFunctionUtil.matcherLast(sqlMumpsSuffixItem,"$find(",",",") > 1 ");
				field = mFunctionUtil.convertMumpsSqlFieldToJavaSqlField(params[0]);
				value = "%"+mFunctionUtil.convertMumpsSqlValueToJavaSqlValue(params[1]).toUpperCase()+"%";
				criteriaField = field+criteriaField;
			}else if(mFunctionUtil.isMatcher(sqlMumpsSuffixItem," %startswith "," ")){
				params = mFunctionUtil.matcherLast(sqlMumpsSuffixItem," %startswith "," ");		
				field = mFunctionUtil.convertMumpsSqlFieldToJavaSqlField(params[0]);
				value = mFunctionUtil.convertMumpsSqlValueToJavaSqlValue(params[1])+"%";
				criteriaField = field+criteriaField;
			}else if(mFunctionUtil.isMatcher(sqlMumpsSuffixItem," = ")){
				params = mFunctionUtil.matcher(sqlMumpsSuffixItem," = ");	
				field = mFunctionUtil.convertMumpsSqlFieldToJavaSqlField(params[0]);
				value = mFunctionUtil.convertMumpsSqlValueToJavaSqlValue(params[1]).toUpperCase();	
				criteriaField = field+criteriaField;
			}else if(mFunctionUtil.isMatcher(sqlMumpsSuffixItem,"("," <> "," OR "," IS NULL) ")){
				params = mFunctionUtil.matcher(sqlMumpsSuffixItem,"("," <> "," OR "," IS NULL) ");		
				field = mFunctionUtil.convertMumpsSqlFieldToJavaSqlField(params[0]);
				value = mFunctionUtil.convertMumpsSqlValueToJavaSqlValue(params[1]).toUpperCase();
				criteriaField = "("+field+" <> ? or "+field+" is null)";
			}else if(mFunctionUtil.isMatcher(sqlMumpsSuffixItem," is not null AND "," not in (",") ")){
				params = mFunctionUtil.matcherLast(sqlMumpsSuffixItem," is not null AND "," not in (",") ");		
				field = mFunctionUtil.convertMumpsSqlFieldToJavaSqlField(params[0]);
				value = mFunctionUtil.convertMumpsSqlValueToJavaSqlValue(params[2]).toUpperCase();
				criteriaField = field+" is not null and "+field+" not in (?)";
			}else if(mFunctionUtil.isMatcher(sqlMumpsSuffixItem," is null or "," in (",") ")){
				params = mFunctionUtil.matcherLast(sqlMumpsSuffixItem," is null or "," in (",") ");		
				field = mFunctionUtil.convertMumpsSqlFieldToJavaSqlField(params[1]);
				value = mFunctionUtil.convertMumpsSqlValueToJavaSqlValue(params[2]).toUpperCase();
				criteriaField = "("+field+" is null or substr("+field+",0) in (?))";
			}else if(mFunctionUtil.isMatcher(sqlMumpsSuffixItem,"(+$piece(")){
				params = mFunctionUtil.matcherLast(sqlMumpsSuffixItem,"<");		
				field = mFunctionUtil.convertMumpsSqlFieldToJavaSqlField(params[0]);
				value = mFunctionUtil.convertMumpsSqlValueToJavaSqlValue(params[1]).toUpperCase();
				//%upper(+$piece(SQLUser.WWW013.TerminationOn,".",1)) < 58042
				criteriaField = "("+field+" is null or substr("+field+",0) in (?))";
			}else if(sqlMumpsSuffixItem.equals("(1=1) ")){
				criteriaField = sqlMumpsSuffixItem;
				
			}else{
				throw new UnsupportedOperationException("Criteria not implemented for "+sqlMumpsSuffixItem);
			}				
			criteriaBuilder.append(" and ");
			
			criteriaBuilder.append(criteriaField);
			if(!value.isEmpty()){
				valueList.add(value);
			}
		}
		String orderByParam = Objects.toString(sqlMap.get(" order by "),"");
		String[] orderByItemArray = orderByParam.split(Pattern.quote(", "));
		for (int i = 0; i < orderByItemArray.length; i++) {
			String sqlMumpsSuffixItem = orderByItemArray[i];
			if(sqlMumpsSuffixItem.isEmpty()){
				continue;
			}	
			if(i>0){
				criteriaBuilder.append(", ");
			}else{
				criteriaBuilder.append(" order by ");
			}
			field = mFunctionUtil.convertMumpsSqlFieldToJavaSqlField(sqlMumpsSuffixItem);
			criteriaBuilder.append(field);
		}
	}
		String type = resultSetDao.getDatabaseType();
		sql = "select DISTINCT upper("+table+".ID) AS ID_ROW, "+table+".* from "+table+" where rownum <= 500 "+criteriaBuilder;
		//"select DISTINCT top 2001 %upper(MEDPatient.ID) AS ID from SQLUser.MEDPatient where MEDPatient.company = 0  and P1 %startswith \"rena\"  order by %upper(+$piece(SQLUser.MEDPatient.DOB,\".\",1)) "
		 try {
				this.prepare = resultSetDao.createPreparedStatement(sql);
				for (int i = 0; i < valueList.size(); i++) {
					String valueItem = valueList.get(i).length() >1?valueList.get(i).trim():valueList.get(i);
					this.prepare.setString(i+1, valueItem);						
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
	} 
	// *
	else if(sqlMumps.trim().equals("select DISTINCT VARAlertaLocalLinha.ID from SQLUser.VARAlertaLocalLinha where VARAlertaLocalLinha.company = 0  and  $$RemoveMark^COMViewSQL(SQLUser.VARAlertaLocalLinha.Location,\"0\",\"default\")  = 1 order by  $$RemoveMark^COMViewSQL(SQLUser.VARAlertaLocalLinha.Produto,\"0\",\"default\")")){
		sql = "select DISTINCT VARAlertaLocalLinha.ID from SQLUser.VARAlertaLocalLinha where VARAlertaLocalLinha.company = 0  and  SQLUser.VARAlertaLocalLinha.Location = 1 order by  SQLUser.VARAlertaLocalLinha.Produto";		
	}
	* //
	else {				
		throw new UnsupportedOperationException("Fail of implementation in ResultSet.Prepare("+srcSql+")");
	}
	if(this.prepare == null){
		this.prepare = resultSetDao.createPreparedStatement(sql);
	}
	*/

	public Object Data(Object columnName) {
		Object result;
		try {
			result = resultSet.getObject(mFunctionUtil.toString(columnName));
			Integer columnIndex = null;
			if(result==null){
				for (int i = 0; i < (Integer)GetColumnCount(); i++) {
					if(prepare.getMetaData().getColumnName(i+1).toUpperCase().equals(mFunctionUtil.toString(columnName).toUpperCase())){
						columnIndex = i+1;
						result = resultSet.getObject(columnIndex);
						break;
					}
				}
			}else{
				columnIndex = resultSet.findColumn(mFunctionUtil.toString(columnName));
			}
			//result = resultSet.getObject(mFunctionUtil.toString(columnName));
			result = resultTypeConverter(result,columnIndex);	
		} catch (Exception e) {
			throw new IllegalArgumentException("Column " + columnName
					+ " not found");
		}
		return mFunctionUtil.toString(result);
	}

	public Object Execute(Object... args) {
		try {
			if(sql.contains("?")){
				for (int i=0;i<args.length;i++) {
					if(args[i] instanceof Date){
						this.prepare.setDate(i+1, new java.sql.Date(((Date)args[i]).getTime()) );
					}else{
						this.prepare.setObject(i+1, args[i]);
					}
				}				
			}
			/*
			String p1 = args.length>=1 && args[0]!=null?args[0].toString():"";
			if(!p1.isEmpty()){				
				this.prepare.setString(1, p1);		
			}
			*/			
			prepare.setFetchSize(500);
			resultSet = prepare.executeQuery();			
		} catch (SQLException e) {
			System.err.println("Fail in ResultSet.Execute("+Arrays.toString(args)+"): "+e.getMessage()+" ->SQL:"+sql);
			return 0;
		} catch(NullPointerException e){
			System.err.println("Fail in ResultSet.Execute("+Arrays.toString(args)+"): "+e.getMessage()+" ->SQL:"+sql);
			return 0;
		}
		return 1;
	}

	public Object Get(Object column) {
		if (column instanceof String) {
			return this.GetDataByName(column);
		}
		else {
			return this.GetData(column);
		}
	}

	public Object GetColumnCount() {
		try {
			return (resultSet.getMetaData().getColumnCount()-this.dataRecordFields);
		} catch (SQLException e) {
			return 0;
		}
	}
	
	public Object GetColumnName(Object columnIndex) {
		try {
			return prepare.getMetaData().getColumnName(
					mFunctionUtil.numberConverter(columnIndex).intValue());
		} catch (SQLException e) {
			throw new IllegalArgumentException("Column " + columnIndex
					+ " not found");
		}
	}	
	public Object GetData(Object columnIndex) {
		Object result;
		try {
			result = resultSet.getObject(mFunctionUtil.integerConverter(columnIndex));
			result = resultTypeConverter(result,columnIndex);	
		} catch (Exception e) {
			throw new IllegalArgumentException("Column " + columnIndex
					+ " not found");
		}
		return mFunctionUtil.toString(result);
	}

	public Object GetDataByName(Object columnName) {
		return this.Data(columnName);
	}
	
	public Integer Next(Object... args) {
		try {
			if (!resultSet.next()) {
				return 0;
			}
			if (this.dataRecord != null) {
				String id = resultSet.getString("ID");
		        if (m$op.Logical(m$fn.$piece(m$fn.$get(m$.var("^WWW001",0,this.dataRecord,1)),"~",3))) {
		        	id = "0||"+id;
		        }
		        Object [] subs = (Object [])("^"+this.dataRecord+"||"+id+"||1").split("\\|\\|",-1);
		        //System.out.println(Arrays.toString(subs));
	    		if (!metadataCache.isQueried(subs)) {
	    			String value = mJDBCObject.getResultRecord(m$,this.dataRecord,resultSet);
	    			metadataCache.set(subs, value);
	    			//System.out.println("loadrecord: "+value);
	    			metadataCache.addQueried(subs);
	    		}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return 0;
		}
		return 1;
	}
	
	public Object Prepare(String srcSql) {		
		sql = convertSQL(srcSql);
		try {
			this.prepare = resultSetDao.createPreparedStatement(sql);
			return 1;
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}			
	}
	
	public String RuntimeModeGet(){
		return RuntimeMode;
	}

	public String RuntimeModeSet(String mode){
		RuntimeMode = mode;
		return "1";
	}
	
	public void setDataRecord(String className) {
		this.dataRecord = className;
	}
	
	private String convertSQL(String sql) {
		System.out.println(sql);
		List<Object> result = convertSQLStatement(sql);
		String resultSQL = decomposeSQL(result,0);
		if (resultSetDao.getDatabaseType().equals("ORACLE")) {
			resultSQL += decomposeSQLORACLE(null,null);
			resultSQL = SQLUtil.translateElementToOracleQuery(resultSQL);
		}
		System.out.println(resultSQL);
		return resultSQL;
	}
	
	private void convertSQLElement(List<Object> elements) {
		int size = elements.size();
		if ((size > 0) && (elements.get(size-1) instanceof String)) {
			String statement = elements.get(size-1).toString();
			if (statement.equalsIgnoreCase("select")) {
				this.currentClause = "select";
			}
			else if (statement.equalsIgnoreCase("from")) {
				if ((this.dataRecord != null) && (this.dataRecordFields == 0)) {
					String recordFields = mJDBCObject.getRecordFields(m$,this.dataRecord);
					elements.set(size-1,",");
					elements.add(recordFields.replace(";", ","));
					elements.add("from");
					/*
					String[] cols = m$.var("^WWWCLASSCDEF", this.dataRecord,"coldef").toString().split(";",-1);
					 for (int i = 0; i < cols.length; i++) {
						if (mFunctionUtil.splitDelimiter(cols[i]).length >= 2) {
							this.dataRecordFields++;
						}
					}*/
					this.dataRecordFields = recordFields.split(";",-1).length;
				}
				this.currentClause = "from";
			}
			else if (statement.equalsIgnoreCase("where")) {
				this.currentClause = "where";
			}
			else if (statement.equalsIgnoreCase("group")) {
				this.currentClause = "group";
				this.groupByClause = true;
			}
			else if (statement.equalsIgnoreCase("order")) {
				this.currentClause = "order";
			}
			if (m$op.Greater(m$fn.$length(elements.get(size-1),"."),2)) {
				int plength = util.mFunctionUtil.castInt(m$fn.$length(elements.get(size-1),"."));
				elements.set(size-1,m$fn.$piece(elements.get(size-1),".",plength-1,plength));
			}
		}
		if (resultSetDao.getDatabaseType().equals("ORACLE")) {
			convertSQLElementORACLE(elements);
		}
	}
	
	private void convertSQLElementORACLE(List<Object> elements) {
		int size = elements.size();
		if ((size > 1) && (elements.get(size-2) instanceof String)) {
			String function = elements.get(size-2).toString();
			if (function.equalsIgnoreCase("%upper")) {
				elements.set(size-2,"upper");
			}
			else if (function.equalsIgnoreCase("%lower")) {
				elements.set(size-2,"lower");
			}
			else if (function.equalsIgnoreCase("$piece")) {
				elements.set(size-2,"mFunction_PCK.m$piece");
			}
			else if (function.equalsIgnoreCase("$translate")) {
				elements.set(size-2,"mFunction_PCK.m$translate");
			}
			else if (function.equalsIgnoreCase("$find")) {
				elements.set(size-2,"instr");
			}
			else if (function.equalsIgnoreCase("%startswith")) {
				elements.set(size-2,"like");
				elements.add("||'%'");
			}
			else if (function.equalsIgnoreCase("top")) {
				if (elements.get(size-1) instanceof List) {
					List<Object> subelements = (List<Object>) elements.get(size-1);
					if (subelements.size() > 0) {
						this.maxRecords = util.mFunctionUtil.castInt(subelements.get(0));
					}
				}
				else {
					this.maxRecords = util.mFunctionUtil.castInt(elements.get(size-1));
				}
				elements.remove(size-1);
				elements.remove(size-2);
			}
			else if (function.equalsIgnoreCase("$$RemoveMark^COMViewSQL")) {
				m$.call("COMViewSQL.RemoveMark","abc","0","default");
				elements.set(size-2,"mFunction_PCK.m$translate");
				if (elements.get(size-1) instanceof List) {
					List<Object> subelements = (List<Object>) elements.get(size-1); 
					if (util.mFunctionUtil.toString(m$fn.$get(m$.var("^CacheTempWWWUMLAU",m$.var("YUCI").get(),0))).isEmpty()) {
						for (int i=0;i<subelements.size()-4;i++) {
							elements.add(subelements.get(i));
						}
						elements.remove(size-1);
						elements.remove(size-2);
					}
					else {
						elements.set(size-2,"translate");
						subelements.set(subelements.size()-3,"'"+m$fn.$get(m$.var("^CacheTempWWWUMLAU",m$.var("YUCI").get(),0))+"'");
						subelements.set(subelements.size()-1,"'"+m$fn.$get(m$.var("^CacheTempWWWUMLAU",m$.var("YUCI").get(),1))+"'");
					}
				}
			}
		}
		size = elements.size();
		if ((size > 4) && (elements.get(size-1) instanceof String)  && (elements.get(size-2) instanceof String)) {
			String value = elements.get(size-1).toString();
			String operator = elements.get(size-2).toString();
			if ((Character.isDigit(value.charAt(0))) && ("=,<>,>,<,>=,<=".contains(operator))) {
				if ((elements.get(size-3) instanceof List) && (elements.get(size-4) instanceof String)) {
					elements.set(size-1,"'"+value+"'");
				}
			}
		}
		size = elements.size();
		if ((size > 3) && (elements.get(size-1) instanceof String) && (elements.get(size-2) instanceof String) && (elements.get(size-4) instanceof String)) {
			String value = elements.get(size-1).toString();
			String operator = elements.get(size-2).toString();
			String function = elements.get(size-4).toString();
			if ((operator.equals(">")) && (value.equals("1")) && (function.equalsIgnoreCase("instr"))) {
				elements.set(size-1,"0");
			}
		}
	}

	private List<Object> convertSQLStatement(String sql) {
		List<Object> result = new ArrayList<Object>();
		
		Character quote = null;
		Integer level = 0;
		String element = "", subsql = "";
		Boolean endelement = false;
		Character lastchar = null, nextchar = null;
		
		for (int i=0;i<sql.length();i++) {
			endelement = false;
			nextchar = sql.charAt(i);
			if (quote != null) {
				if (sql.charAt(i) == quote) {
					if ((i+1<sql.length()) && (sql.charAt(i+1) == quote)) {
						if (sql.charAt(i) == '\"') {
							element += sql.charAt(i++);
						}
						else {
							element += sql.charAt(i++);
							element += sql.charAt(i);
						}
					}
					else {
						quote = null;
						if (level == 0) {
							element = '\''+element+'\'';
							endelement = true;
							nextchar = null;
						}
					}
				}
				else {
					if (level == 0) {
						element += sql.charAt(i);
					}
				}
			}
			else if (nextchar == '\"') {
				if (level == 0) {
					endelement = true;
					nextchar = null;
				}
				quote = sql.charAt(i);
			}
			else if (nextchar == '\'') {
				if (level == 0) {
					endelement = true;
					nextchar = null;
				}
				quote = sql.charAt(i);
			}
			else if (nextchar == '(') {
				if (level == 0) {
					endelement = true;
					nextchar = null;
				}
				level++;
			}
			else if (nextchar == ')') {
				level--;
				if (level == 0) {
					result.add(convertSQLStatement(subsql));
					subsql = "";
					endelement = true;
					nextchar = null;
				}
			}
			else if (nextchar == ',') {
				if (level == 0) {
					endelement = true;
					if ((lastchar == ',') || (lastchar == '(')) {
						element = "null";
					}
				}
			}
			else if (" \t\r\n;".contains(String.valueOf(nextchar))) {
				if (level == 0) {
					endelement = true;
					nextchar = null;
				}
			}
			else if ("+-*/><=!".contains(String.valueOf(nextchar))) {
				if (level == 0) {
					if (!"+-*/><=!".contains(String.valueOf(lastchar))) {
						endelement = true;
					}
					else {
						element += nextchar;
					}
				}
			}
			else {
				if (level == 0) {
					if ("+-*/><=!,".contains(String.valueOf(lastchar))) {
						endelement = true;
					}
					else {
						element += nextchar;
					}
				}
			}
			if (endelement) {
				if (!element.isEmpty()) {
					result.add(element);
					element = "";
				}
				convertSQLElement(result);
				if (nextchar !=null) {
					element += nextchar;
				}
			}
			else {
				if (level > 0) {
					subsql += sql.charAt(i);
				}
			}
			lastchar = sql.charAt(i);
		}
		if (!element.isEmpty()) {
			result.add(element);
			convertSQLElement(result);
		}
		return result;
	}
	
	private String decomposeSQL(List<Object> elements, Integer level) {
		String sql = "";
		String element;
		String element_before = "";
		Boolean selectMax = false;
		Boolean beginMax = false;
		Boolean sumarized = false;
		Boolean firstMax = true;
		for (int i=0;i<elements.size();i++) {
			if (elements.get(i) instanceof List) {
				if ((selectMax) && !(beginMax) && !(sumarized)) {
					sql += "max(";
					beginMax = true;
				}
				sql += "("+decomposeSQL((List<Object>) elements.get(i),level+1)+")";
			}
			else {
				element = elements.get(i).toString();
				if ((element.equalsIgnoreCase("select")) || (element.equalsIgnoreCase("distinct"))) {
					selectMax = (((this.groupByClause)&&(level<=0))?true:false);
				}
				else if ((selectMax) && !(beginMax) && !(sumarized) && (element.equalsIgnoreCase("min") || element.equalsIgnoreCase("max") || element.equalsIgnoreCase("avg") || element.equalsIgnoreCase("sum") || element.equalsIgnoreCase("count"))) {
					if (firstMax) {
						sql += " * from ( select";
						firstMax = false;
					}
					sumarized = true;
				}
				else if ((selectMax) && !(beginMax) && (sumarized) && (element.equalsIgnoreCase(",") || element.equalsIgnoreCase("from"))) {
					sumarized = false;
				}
				else if ((selectMax) && !(beginMax) && !(sumarized) && !(element.equalsIgnoreCase(",") || element.equalsIgnoreCase("as") || element.equalsIgnoreCase("from")) && !element_before.equalsIgnoreCase("as")) {
					if (firstMax) {
						sql += " * from ( select";
						firstMax = false;
					}
					if (element.contains(" as ")) {
						element = "max("+element.replace(" as ",") as ").replace(", ",", max(");
					}
					else {
						sql += " max(";
						beginMax = true;
					}
				}
				else if ((selectMax) && (beginMax) && !(sumarized) && (element.equalsIgnoreCase(",") || element.equalsIgnoreCase("as") || element.equalsIgnoreCase("from"))) {
					sql += ")";
					beginMax = false;
					if ((element.equalsIgnoreCase(",") || element.equalsIgnoreCase("from")) && (elements.get(i-1) instanceof String)) {
						String[] elementnames = elements.get(i-1).toString().split("\\.",-1);
						sql += " as "+elementnames[elementnames.length-1];
					}
				}
				if ((selectMax) && element.equalsIgnoreCase("from")) {
					selectMax = false;
				}
				if (!element.isEmpty()) {
					if ((element.charAt(0) != ',') && (element.charAt(0) != '|')) {
						sql += " "+element;
					}
					else {
						sql += element;
					}
				}
				element_before = element;
			}
			if (resultSetDao.getDatabaseType().equals("ORACLE")) {
				sql += decomposeSQLORACLE(elements,i);
			}
		}
		if ((this.groupByClause) && !(firstMax)) {
			sql += " )";
		}
		return sql.trim();
	}

	private String decomposeSQLORACLE(List<Object> elements, Integer i) {
		if (this.maxRecords != null) {
			Integer maxRecords = this.maxRecords;
			if (elements != null) {
				if (elements.get(i) instanceof String) {
					String statement = elements.get(i).toString();
					if ((statement.equalsIgnoreCase("where")) && !(this.groupByClause)) {
						this.maxRecords = null;
						return " rownum <= "+maxRecords.toString()+" and";
					}
				}
				if ((i+1 < elements.size())  && (elements.get(i+1) instanceof String)) {
					String statement = elements.get(i+1).toString();
					if ((statement.equalsIgnoreCase("order")) && !(this.groupByClause)) {
						this.maxRecords = null;
						return " where rownum <= "+maxRecords.toString();
					}
				}
			}
			else {
				this.maxRecords = null;
				return " where rownum <= "+maxRecords.toString();
			}
		}
		return "";
	}
	
	private Object resultTypeConverter(Object result, Object columnIndex ){
		try {
			if(result != null && columnIndex != null){
				Integer _columnIndex = mFunctionUtil.integerConverter(columnIndex);
				
				if (result instanceof Clob){
					result = SQLUtil.CLOBtoString((Clob)result);
				}
				if(prepare.getMetaData().getColumnTypeName(_columnIndex).equalsIgnoreCase("TIMESTAMP")){
					result = resultSet.getTimestamp(_columnIndex);
				}
				if(prepare.getMetaData().getColumnTypeName(_columnIndex).equalsIgnoreCase("DATE")){
					result = resultSet.getDate(_columnIndex);
				}			
				if(result instanceof Timestamp){
					result = mJDBCObject.convertDateTimeToMumps(result);
				}
				if(result instanceof Date){
					result = mJDBCObject.convertDateToMumps(result);
				}			
			}
			
		}  catch (Exception e) {
			throw new IllegalArgumentException("Column " + columnIndex
					+ " can not converter.");
		}
		return result;
	}

}
