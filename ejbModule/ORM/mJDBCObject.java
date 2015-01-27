package ORM;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import mLibrary.mContext;
import mLibrary.exceptions.mPersistenceException;
import util.SQLUtil;
import util.mFunctionUtil;
import br.com.innovatium.mumps2java.dataaccess.RelationalDataDAO;
import br.com.innovatium.mumps2java.dataaccess.RelationalDataDAOImpl;
import br.com.innovatium.mumps2java.dataaccess.ServiceLocator;
import br.com.innovatium.mumps2java.dataaccess.ServiceLocatorException;
import br.com.innovatium.mumps2java.dataaccess.exception.SQLExecutionException;
import dataaccess.mDataAccess;

public class mJDBCObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6664501569060028118L;
	public static void closeStatement(ResultSet result){
		try {
			result.getStatement().close();
		} catch (SQLException e) {
			throw new IllegalArgumentException("Fail to close result set", e);
		}		
	}

	public static Date convertDateTimeToJava(String val) {
		if (val == null) {
			return null;
		}
		if (val.isEmpty()) {
			return null;
		}
		return new Date(mFunctionUtil.dateTimeMumpsToJava(val).longValue());
	}

	public static String convertDateTimeToMumps(Object val) {
		if (val == null) {
			return "";
		}
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		cal.setTime(((Date) val));
		Calendar cal2 = Calendar.getInstance(mFunctionUtil.getTimeZone());
		cal2.clear();
		cal2.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
		return mFunctionUtil.dateTimeJavaToMumps(cal2.getTimeInMillis());
	}

	public static Date convertDateToJava(String val) {
		if (val == null) {
			return null;
		}
		if (val.isEmpty()) {
			return null;
		}
		return new Date(mFunctionUtil.dateMumpsToJava(val).longValue());
	}

	public static String convertDateToMumps(Object val) {
		if (val == null) {
			return "";
		}
		Double res = mFunctionUtil.dateJavaToMumps(((Date) val).getTime());
		if (res == null) {
			return "";
		}

		return (String.valueOf(res.longValue()));
	}

	public static Timestamp convertTimestampToJava(String val) {
		if (val == null) {
			return null;
		}
		if (val.isEmpty()) {
			return null;
		}
		return new Timestamp(mFunctionUtil.dateMumpsToJava(val).longValue());
	}

	public static String getRecordFields(mContext m$, String className) {
		String classCDef = (String) m$.var("^WWWCLASSCDEF", className, "def")
				.getValue();
		if ((classCDef == null) || (classCDef.isEmpty())) {
			throw new IllegalStateException(
					"There is no compiled definition to this class: "
							+ className);
		}

		String classCDefCol = (String) m$.var("^WWWCLASSCDEF", className,
				"coldef").getValue();
		if ((classCDefCol == null) || (classCDefCol.isEmpty())) {
			throw new IllegalStateException(
					"There is no compiled definition of columns to this class: "
							+ className);
		}
		String[] classCDefColMap = classCDefCol.split(";", -1);

		String[] fieldCDefMap;
		StringBuilder resultFields = new StringBuilder();
		String fieldName;
		for (int i = 0; i < classCDefColMap.length; i++) {
			fieldCDefMap = mFunctionUtil.splitDelimiter(classCDefColMap[i]);

			if (fieldCDefMap.length >= 2) {
				fieldName = "rf_" + fieldCDefMap[1];
				if (fieldName.length() > 30) {
					fieldName = fieldName.substring(0, 30);
				}
				if(fieldCDefMap.length > 2 && fieldCDefMap[2].equals("3")){
					int offset = 1;
					int amount = 3600;/*
					resultFields.append("DBMS_LOB.SUBSTR(");
					resultFields.append(className+"."+fieldCDefMap[1]);
					resultFields.append(","+amount+","+offset+") as ");
					resultFields.append(fieldName);
					resultFields.append("$1");					
					resultFields.append(",");
					resultFields.append("case when length("+className+"."+fieldCDefMap[1]+")>"+amount+" then SUBSTR(");
					resultFields.append(className+"."+fieldCDefMap[1]);
					resultFields.append(","+(amount+1)+") end as ");
					resultFields.append(fieldName);
					resultFields.append("$2");	*/					
					for (int j = 0; j < 32767/amount; j++) {
						if(j>0){
							resultFields.append("; ");
						}
						resultFields.append("DBMS_LOB.SUBSTR(");
						resultFields.append(className+"."+fieldCDefMap[1]);
						resultFields.append(","+amount+","+(amount*j+offset)+") as ");
						resultFields.append(fieldName);
						resultFields.append("$"+(j+1));
					}
				}else{				
					resultFields.append(className+"."+fieldCDefMap[1] + " as " + fieldName);
				}				
				
				if (i + 1 < classCDefColMap.length) {
					resultFields.append("; ");
				}
			}
		}

		return resultFields.toString();
	}

	public static String getResultRecord(mContext m$, String className,
			ResultSet result) {
		String classCDef = (String) m$.var("^WWWCLASSCDEF", className, "def")
				.getValue();
		if ((classCDef == null) || (classCDef.isEmpty())) {
			throw new IllegalStateException(
					"There is no compiled definition to this class: "
							+ className);
		}

		String classCDefCol = (String) m$.var("^WWWCLASSCDEF", className,
				"coldef").getValue();
		if ((classCDefCol == null) || (classCDefCol.isEmpty())) {
			throw new IllegalStateException(
					"There is no compiled definition of columns to this class: "
							+ className);
		}
		String[] classCDefColMap = classCDefCol.split(";", -1);

		String[] fieldCDefMap;
		StringBuilder resultRecord = new StringBuilder();
		String fieldName;
		int appendMemoCount = 32767/3600;
		int colIdx = 0;
		for (int i = 0; i < classCDefColMap.length; i++,colIdx++) {
			fieldCDefMap = mFunctionUtil.splitDelimiter(classCDefColMap[i]);

			if (fieldCDefMap.length >= 3) {
				fieldName = "rf_" + fieldCDefMap[1];
				if (fieldName.length() > 30) {
					fieldName = fieldName.substring(0, 30);
				}
				// Date
				if (fieldCDefMap[2].equals("1")) {
					try {
						resultRecord.append(convertDateToMumps(result
								.getDate(fieldName)));
					} catch (SQLException e) {
						throw new IllegalArgumentException(
								"Fail to get result set", e);
					}
				}
				// Timestamp
				else if (fieldCDefMap[2].equals("14")) {
					try {
						resultRecord.append(convertDateTimeToMumps(result
								.getTimestamp(fieldName)));
					} catch (SQLException e) {
						throw new IllegalArgumentException(
								"Fail to get result set", e);
					}
				}
				// Others
				else {
					try {
						
						int appendCount = 1;
						boolean memo = false;
						if (fieldCDefMap[2].equals("3")) {
							appendCount = appendMemoCount;
							memo = true;
						}
						for (int j = 0; j < appendCount; j++) {	
							if(j>0){
								colIdx++;
							}									
							Object obj = result.getObject(fieldName+(memo?"$"+(1+j):""));
							if (obj != null) {
								if (obj.getClass().getName()
										.contains("CLOB")) {										
									try {
										resultRecord.append(SQLUtil
												.CLOBtoString(
												result
														.getClob(fieldName+(memo?"$"+(1+j):""))));
									} catch (Exception e) {
										closeStatement(result);
										throw new mPersistenceException(
												"Fail to parse clob from the table "
														+ className
														+ " and column "
														+ fieldCDefMap[1], e);
									}
								} else {
									resultRecord.append(obj);
								}
							} else {
								resultRecord.append("");
							}
						}																							
					} catch (SQLException e) {
						throw new IllegalArgumentException(
								"Fail to get result set", e);
					}
				}
			}
			// Others
			else if (fieldCDefMap.length >= 2) {
				fieldName = "rf_" + fieldCDefMap[1];
				if (fieldName.length() > 30) {
					fieldName = fieldName.substring(0, 30);
				}
				try {
					resultRecord
							.append(result.getObject(fieldCDefMap[1]) != null ? result
									.getObject(fieldName) : "");
				} catch (SQLException e) {
					throw new IllegalArgumentException(
							"Fail to get result set", e);
				}
			}

			if (i + 1 < classCDefColMap.length) {
				resultRecord.append("~");
			}
		}

		return resultRecord.toString();
	}

	private final RelationalDataDAO dao;
	public mJDBCObject() {
		try {
			dao = ServiceLocator.locate(RelationalDataDAOImpl.class);
		} catch (ServiceLocatorException e) {
			throw new IllegalArgumentException(
					"There is not service to locate to this class "
							+ RelationalDataDAO.class.getName(), e);
		}
	}
	public String deleteRecord(mContext m$, String className, String id) {
		return deleteRecord(m$.getmDataGlobal(), className, id);
	}

	public String deleteRecord(mDataAccess dataAccess, String className,
			String id) {
		String classCDef = (String) dataAccess.get("^WWWCLASSCDEF", className,
				"def");
		if ((classCDef == null) || (classCDef.isEmpty())) {
			throw new IllegalStateException(
					"There is no compiled definition to this class: "
							+ className);
		}
		String[] classCDefMap = mFunctionUtil.splitDelimiter(classCDef);
		String tableSQLName = classCDefMap[1];
		String sharedFile = classCDefMap[4];

		if (sharedFile.equals("1")) {
			if (id.startsWith("0||")) {
				id = id.substring(3);
			}
		}

		try {
			return dao.deleteRecord(tableSQLName, id);
		} catch (SQLExecutionException e) {
			throw new mPersistenceException(
					"Fail to delete record from the table " + className
							+ " and id " + id, e);
		}
	}

	public Boolean existsPK(mContext m$, String className, String valPK) {
		String classCDef = (String) m$.var("^WWWCLASSCDEF", className, "def")
				.getValue();
		if ((classCDef == null) || (classCDef.isEmpty())) {
			return false;
		}
		String tableSQLName = mFunctionUtil.splitDelimiter(classCDef)[1];

		String classCDefPK = (String) m$.var("^WWWCLASSCDEF", className,
				"pkdef").getValue();
		if ((classCDefPK == null) || (classCDefPK.isEmpty())) {
			return false;
			// throw new IllegalStateException(
			// "There is no primary key configuration to this class: "+
			// className);
		}
		String[] classCDefPKMap = classCDefPK.split(";", -1);

		String[] valPKMap = valPK.split(",", -1);

		String[] pkCDefMap;
		String pkNameList = "";
		String pkValueList = "";
		for (int i = 0; i < classCDefPKMap.length; i++) {
			pkCDefMap = mFunctionUtil.splitDelimiter(classCDefPKMap[i]);
			pkNameList = pkNameList.concat(((i > 0) ? "," : "") + pkCDefMap[1]);
			if (i < valPKMap.length) {
				if (valPKMap[i] == null) {
					pkValueList = pkValueList.concat((i > 0) ? "," : "");
				} else if (!valPKMap[i].equals("\"\377\"")) {
					if (valPKMap[i].charAt(0) == '"') {
						pkValueList = pkValueList.concat(((i > 0) ? "," : "")
								+ valPKMap[i].substring(1,
										valPKMap[i].length() - 1));
					} else {
						pkValueList = pkValueList.concat(((i > 0) ? "," : "")
								+ valPKMap[i]);
					}
				}
			}
		}

		String[] namePKMap = pkNameList.split(",", -1);
		String[] valuePKMap = pkValueList.split(",", -1);

		Boolean existsPK;
		try {
			existsPK = dao.existsRecordPK(tableSQLName, namePKMap, valuePKMap);
		} catch (SQLExecutionException e1) {
			throw new mPersistenceException(
					"Fail to search record from the table " + className
							+ " and PK " + valPK.toString(), e1);
		}

		return existsPK;
	}

	public Map<String, String> findRecords(mDataAccess dataAccess,
			String className, String id) {
		Map<String, String> resultMap = new LinkedHashMap<String, String>();
		long timeIni = new Date().getTime();
		String classCDef = (String) dataAccess.get("^WWWCLASSCDEF", className,
				"def");
		if ((classCDef == null) || (classCDef.isEmpty())) {
			throw new IllegalStateException(
					"There is no compiled definition to this class: "
							+ className);
		}
		String[] classCDefMap = mFunctionUtil.splitDelimiter(classCDef);
		String tableSQLName = classCDefMap[1];
		String saveProc = classCDefMap[3];
		if (saveProc.isEmpty()) {
			saveProc = "0";
		}
		String sharedFile = classCDefMap[4];

		String classCDefPK = (String) dataAccess.get("^WWWCLASSCDEF",
				className, "pkdef");
		String[] classCDefPKMap = classCDefPK.split(";", -1);

		if (sharedFile.equals("1")) {
			if (id.startsWith("0||")) {
				id = id.substring(3);
			}
		}

		if (id.endsWith("||1||%")) {
			if ((id.split("\\|\\|", -1).length - 1) > classCDefPKMap.length) {
				id = id.substring(0, id.length() - 4) + "%";
			}
		}

		id = formatId(classCDefPKMap, id);

		String classCDefCol = (String) dataAccess.get("^WWWCLASSCDEF",
				className, "coldef");
		if ((classCDefCol == null) || (classCDefCol.isEmpty())) {
			if ((classCDefPK != null) && (!classCDefPK.isEmpty())) {
				return resultMap;
			}
			throw new IllegalStateException(
					"There is no compiled definition of columns to this class: "
							+ className);
		}
		String[] classCDefColMap = classCDefCol.split(";", -1);

		String[] fieldCDefMap;
		StringBuilder tableSQLFields = new StringBuilder();
		tableSQLFields.append("ID");
		String[][] fieldCDefMapArray = new String[classCDefColMap.length][];
		int appendMemoCount = 1;
		for (int i = 0; i < classCDefColMap.length; i++) {
			fieldCDefMapArray[i] = mFunctionUtil
					.splitDelimiter(classCDefColMap[i]);
			tableSQLFields.append(",");
			fieldCDefMap = classCDefColMap[i].split("~");
			if (fieldCDefMap.length >= 2 && fieldCDefMap[1].trim().length() > 0) {
				if(fieldCDefMap.length > 2 && fieldCDefMap[2].equals("3")){
					int offset = 1;
					int amount = 3600;
					/*appendMemoCount = 32767/amount;
					for (int j = 0; j < appendMemoCount; j++) {
						if(j>0){
							tableSQLFields.append(",");
						}
						tableSQLFields.append("DBMS_LOB.SUBSTR(");
						tableSQLFields.append(fieldCDefMap[1]);
						tableSQLFields.append(","+amount+","+(offset+j*amount)+") as ");
						tableSQLFields.append(fieldCDefMap[1]);
						tableSQLFields.append("$"+(j+1));					
					}*/
					appendMemoCount = 2;
					tableSQLFields.append("DBMS_LOB.SUBSTR(");
					tableSQLFields.append(fieldCDefMap[1]);
					tableSQLFields.append(","+amount+","+offset+") as ");
					tableSQLFields.append(fieldCDefMap[1]);
					tableSQLFields.append("$1");					
					tableSQLFields.append(",");
					tableSQLFields.append("case when length("+fieldCDefMap[1]+")>"+amount+" then SUBSTR(");
					tableSQLFields.append(fieldCDefMap[1]);
					tableSQLFields.append(","+(amount+1)+") end as ");
					tableSQLFields.append(fieldCDefMap[1]);
					tableSQLFields.append("$2");												
				}else{
					tableSQLFields.append(fieldCDefMap[1]);					
				}
			} else {
				tableSQLFields.append("null");
			}
		}
		//
		ResultSet result = null;
		try {
			result = dao.loadRecords(tableSQLName, id,
					tableSQLFields.toString());
		} catch (SQLExecutionException e1) {
			throw new mPersistenceException(
					"Fail to load record from the table " + className
							+ " and id " + id, e1);
		}
		if (result == null) {
			return resultMap;
		}
		//
		StringBuilder resultRecord;
		try {			
			do {
				resultRecord = new StringBuilder();
				int colIdx = 0;
				for (int i = 0; i < fieldCDefMapArray.length; i++,colIdx++) {
					fieldCDefMap = fieldCDefMapArray[i];
					if (fieldCDefMap.length >= 3) {
						// Date
						if (fieldCDefMap[2].equals("1")) {
							Object obj = result.getObject(colIdx + 2);
							if (obj != null) {
								try {
									resultRecord.append(convertDateToMumps(result.getDate(colIdx + 2)));									
								} catch (Exception e) {
									closeStatement(result);
									throw new IllegalArgumentException(
											"Fail to get result set", e);
								}
							}else{
								resultRecord.append("");
							}
						}
						// Timestamp
						else if (fieldCDefMap[2].equals("14")) {
							Object obj = result.getObject(colIdx + 2);
							if (obj != null) {
								try {
									resultRecord.append(convertDateTimeToMumps(result.getTimestamp((colIdx + 2))));									
								} catch (Exception e) {
									closeStatement(result);
									throw new IllegalArgumentException(
											"Fail to get result set", e);
								}
							}else{
								resultRecord.append("");
							}
						}
						// Others
						else {
							try {
								int appendCount = 1;
								if (fieldCDefMap[2].equals("3")) {
									appendCount = appendMemoCount;
								}
								for (int j = 0; j < appendCount; j++) {	
									if(j>0){
										colIdx++;
									}									
									Object obj = result.getObject(colIdx + 2);
									if (obj != null) {
										if (obj.getClass().getName()
												.contains("CLOB")) {										
											try {
												resultRecord.append(dao
														.toString(result
																.getClob(colIdx + 2)));
											} catch (SQLExecutionException e) {
												closeStatement(result);
												throw new mPersistenceException(
														"Fail to parse clob from the table "
																+ className
																+ " and id " + id
																+ " and column "
																+ (colIdx + 1), e);
											}
										} else {
											resultRecord.append(obj);
										}
									} else {
										resultRecord.append("");
									}
								}

							} catch (SQLException e) {
								closeStatement(result);
								throw new IllegalArgumentException(
										"Fail to get result set", e);
							}
						}
					}
					// Others
					/*else {
						try {
							resultRecord
									.append(result.getObject(colIdx + 2) != null ? result
											.getObject(colIdx + 2) : "");
						} catch (SQLException e) {
							closeStatement(result);
							throw new IllegalArgumentException(
									"Fail to get result set", e);
						}
					}*/
					if (i + 1 < classCDefColMap.length) {
						resultRecord.append("~");
					}
				}

				if ((sharedFile.equals("1")) && (saveProc.equals("0"))) {
					resultMap.put("0||" + result.getString(1),
							resultRecord.toString());
				} else {
					resultMap.put(result.getString(1), resultRecord.toString());
				}
			} while (result.next());
		} catch (SQLException e) {
			closeStatement(result);
			throw new IllegalArgumentException(
					"Fail to get next result record", e);
		}
		closeStatement(result);
		System.out.println(" -> Time: "+(new Date().getTime()-timeIni)/1000d);
		return resultMap;
	}

	public String formatId(String[] classCDefPKMap, String id) {
		String[] idMap = id.split("\\|\\|", -1);
		String[] fieldCDefMap;
		StringBuilder resultId = new StringBuilder();
		for (int i = 0; i < idMap.length; i++) {
			if (i > 0) {
				resultId.append("||");
			}
			if (i >= classCDefPKMap.length) {
				resultId.append(idMap[i]);
				continue;
			}
			fieldCDefMap = mFunctionUtil.splitDelimiter(classCDefPKMap[i]);
			if (fieldCDefMap.length > 2) {
				// Date
				if (fieldCDefMap[2].equals("1")) {
					resultId.append((idMap[i].endsWith(",0") ? idMap[i]
							.substring(0, idMap[i].length() - 2) : idMap[i]));
				} else {
					resultId.append(idMap[i]);
				}
			} else {
				resultId.append(idMap[i]);
			}
		}
		return resultId.toString();
	}

	public String getId(Object[] subs) {
		return getId(subs, subs.length);
	}

	public String getId(Object[] subs, int last) {
		String id = "";
		for (int i = 1; i < last; i++) {
			id = (id.isEmpty() ? "" : id + "||")
					+ mFunctionUtil.toString(subs[i]);
		}
		return id;
	}

	public String loadRecord(mContext m$, String className, String id) {
		return loadRecord(m$.getmDataGlobal(), className, id);
	}
	
	public String loadRecord(mDataAccess dataAccess, String className, String id) {
		Map<String, String> resultMap = findRecords(dataAccess, className, id);
		if (resultMap.isEmpty()) {
			return null;
		}
		return resultMap.get(id);
	}	

	public String saveRecord(mContext m$, String className, String id,
			String record) {

		return saveRecord(m$.getmDataGlobal(), className, id, record);
	}

	public String saveRecord(mDataAccess globalAccess, String className,
			String id, String record) {

		String classCDef = (String) globalAccess.get("^WWWCLASSCDEF",
				className, "def");
		if ((classCDef == null) || (classCDef.isEmpty())) {
			throw new IllegalStateException(
					"There is no compiled definition to this class: "
							+ className);
		}
		String[] classCDefMap = mFunctionUtil.splitDelimiter(classCDef);
		String tableSQLName = classCDefMap[1];
		String sharedFile = classCDefMap[4];

		if (sharedFile.equals("1")) {
			if (id.startsWith("0||")) {
				id = id.substring(3);
			}
		}

		String classCDefPK = (String) globalAccess.get("^WWWCLASSCDEF",
				className, "pkdef");
		if ((classCDefPK == null) || (classCDefPK.isEmpty())) {
			throw new IllegalStateException(
					"There is no primary key configuration to this class: "
							+ className);
		}
		String[] classCDefPKMap = classCDefPK.split(";");

		String classCDefCol = (String) globalAccess.get("^WWWCLASSCDEF",
				className, "coldef");
		if ((classCDefCol == null) || (classCDefCol.isEmpty())) {
			throw new IllegalStateException(
					"There is no column configuration to this class: "
							+ className);
		}
		String[] classCDefColMap = classCDefCol.split(";");

		id = formatId(classCDefPKMap, id);

		boolean exists;
		try {
			exists = dao.existsRecord(tableSQLName, id);
		} catch (SQLExecutionException e) {
			throw new mPersistenceException(
					"Fail to verify existence of the record from the table "
							+ className + " and id " + id, e);
		}

		String tableSQLFields = "";
		Object[] tableSQLValues;
		if (!exists) {
			tableSQLValues = new Object[classCDefPKMap.length
					+ classCDefColMap.length];
		} else {
			tableSQLValues = new Object[classCDefColMap.length];
		}
		String[] fieldCDefMap;

		int count = 0;

		if (!exists) {
			String[] idMap = id.split("\\|\\|");
			//
			for (int i = 0; i < classCDefPKMap.length; i++) {
				fieldCDefMap = mFunctionUtil.splitDelimiter(classCDefPKMap[i]);
				if (fieldCDefMap.length > 2) {
					tableSQLFields = tableSQLFields
							+ (tableSQLFields.isEmpty() ? "" : ",")
							+ fieldCDefMap[1];
					if (i < idMap.length) {
						// Date
						if (fieldCDefMap[2].equals("1")) {
							tableSQLValues[count++] = convertDateTimeToJava(idMap[i]);
						}
						// Timestamp
						else if (fieldCDefMap[2].equals("14")) {
							tableSQLValues[count++] = convertTimestampToJava(idMap[i]);
						}
						// Others
						else {
							tableSQLValues[count++] = idMap[i];
						}
					} else {
						tableSQLValues[count++] = null;
					}
				}
			}
		}
		//
		String[] recordMap = mFunctionUtil.splitDelimiter(record);
		//
		for (int i = 0; i < classCDefColMap.length; i++) {
			fieldCDefMap = mFunctionUtil.splitDelimiter(classCDefColMap[i]);
			if (fieldCDefMap.length > 2) {
				tableSQLFields = tableSQLFields
						+ (tableSQLFields.isEmpty() ? "" : ",")
						+ fieldCDefMap[1];
				if (i < recordMap.length) {
					// Date
					if (fieldCDefMap[2].equals("1")) {
						tableSQLValues[count++] = convertDateTimeToJava(recordMap[i]);
					}
					// Timestamp
					else if (fieldCDefMap[2].equals("14")) {
						tableSQLValues[count++] = convertTimestampToJava(recordMap[i]);
					}
					// Others
					else {
						tableSQLValues[count++] = recordMap[i];
					}
				} else {
					tableSQLValues[count++] = null;
				}
			}
		}
		//
		if (!exists) {
			try {
				return dao.insertRecord(tableSQLName, tableSQLFields,
						tableSQLValues);
			} catch (SQLExecutionException e) {
				throw new mPersistenceException(
						"Fail to insert record into the table " + className
								+ " and id " + id, e);
			}
		} else {
			try {
				return dao.updateRecord(tableSQLName, id, tableSQLFields,
						tableSQLValues);
			} catch (SQLExecutionException e) {
				throw new mPersistenceException(
						"Fail to update record from the table " + className
								+ " and id " + id, e);
			}
		}
	}

	public String searchPK(mContext m$, String className, String searchPK,
			int direction) {
		String classCDef = (String) m$.var("^WWWCLASSCDEF", className, "def")
				.getValue();
		if ((classCDef == null) || (classCDef.isEmpty())) {
			return "";
		}
		String tableSQLName = mFunctionUtil.splitDelimiter(classCDef)[1];
		String altSaveProc = mFunctionUtil.splitDelimiter(classCDef)[3];

		String classCDefPK = (String) m$.var("^WWWCLASSCDEF", className,
				"pkdef").getValue();
		if ((classCDefPK == null) || (classCDefPK.isEmpty())) {
			return "";
			// throw new IllegalStateException(
			// "There is no primary key configuration to this class: "+
			// className);
		}
		String[] classCDefPKMap = classCDefPK.split(";", -1);

		String[] searchPKMap = searchPK.split(",", -1);

		String[] pkCDefMap;
		String pkNameList = "";
		String pkValueList = "";
		for (int i = 0; i < classCDefPKMap.length; i++) {
			pkCDefMap = mFunctionUtil.splitDelimiter(classCDefPKMap[i]);
			pkNameList = pkNameList.concat(((i > 0) ? "," : "") + pkCDefMap[1]);
			if (i < searchPKMap.length) {
				if (searchPKMap[i] == null) {
					pkValueList = pkValueList.concat((i > 0) ? "," : "");
				} else if (!searchPKMap[i].equals("\"\377\"")) {
					if ((searchPKMap[i].length() > 0)
							&& (searchPKMap[i].charAt(0) == '"')) {
						pkValueList = pkValueList.concat(((i > 0) ? "," : "")
								+ searchPKMap[i].substring(1,
										searchPKMap[i].length() - 1));
					} else {
						pkValueList = pkValueList.concat(((i > 0) ? "," : "")
								+ searchPKMap[i]);
					}
				}
			}
		}

		String[] namePKMap = pkNameList.split(",", -1);
		String[] valuePKMap = pkValueList.split(",", -1);

		String[] resultPK;
		try {
			resultPK = dao.searchRecordPK(tableSQLName, direction, namePKMap,
					valuePKMap);
		} catch (SQLExecutionException e1) {
			throw new mPersistenceException(
					"Fail to search record from the table " + className
							+ " and PK " + searchPK.toString(), e1);
		}

		if ((resultPK == null) || (resultPK.length == 0)) {
			return "";
		}

		searchPK = "";
		int j = 0;
		for (int i = 0; i < classCDefPKMap.length; i++) {
			if ((i < searchPKMap.length) && (i < classCDefPKMap.length - 1)
					&& (!searchPKMap[i].equals("\"\377\""))) {
				searchPK = searchPK.concat(((i > 0) ? "," : "")
						+ ((searchPKMap[i] == null) ? "" : searchPKMap[i]));
			} else {
				if (j < resultPK.length) {
					if ((resultPK[j].charAt(0) < '0')
							|| (resultPK[j].charAt(0) > '9')) {
						searchPK = searchPK.concat(((i > 0) ? "," : "") + "\""
								+ resultPK[j++])
								+ "\"";
					} else {
						searchPK = searchPK.concat(((i > 0) ? "," : "")
								+ resultPK[j++]);
					}
				} else {
					searchPK = searchPK.concat(((i > 0) ? "," : "") + "\"\"");
				}
			}
		}

		if (!altSaveProc.equals("4")) {
			searchPK = searchPK.concat(",1");
		}

		return searchPK;
	}
}
