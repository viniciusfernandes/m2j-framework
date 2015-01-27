package util;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;

import util.exception.DataConvertionException;

public class SQLUtil {
	public static final int STRING_MAXLENGTH = 3500;

	public static String CLOBtoString(Clob clob) throws DataConvertionException {
		int length = -1;
		try {
			if (clob == null || (length = (int) clob.length()) <= 0) {
				return "";
			}
			return clob.getSubString(1, length);
		} catch (SQLException e) {
			throw new DataConvertionException(
					"Fail convert clob to string. It was not possible to open clob object",
					e);
		}

	}

	public static String CLOBtoStringOld(Clob clob) throws DataConvertionException {
		int length = -1;
		Reader reader = null;
		try {
			if (clob == null || (length = (int) clob.length()) <= 0) {
				return "";
			}
			reader = clob.getCharacterStream();
		} catch (SQLException e) {
			throw new DataConvertionException(
					"Fail convert clob to string. It was not possible to open clob object",
					e);
		}

		char[] buffer = new char[length];
		StringBuilder string = new StringBuilder();
		try {
			while (reader.read(buffer) != -1) {
				string.append(buffer);
			}
		} catch (IOException e) {
			throw new DataConvertionException(
					"Fail convert clob to string. It was not possible to read clob buffer",
					e);
		}
		return string.toString();
	}
	public static boolean isLargeString(String value) {
		return value != null && value.length() > STRING_MAXLENGTH;
	}

	public static String translateElementToOracleQuery(String sqlResult){
		if (mFunctionUtil.isMatcher(sqlResult, "$$getLocalSolicitante^VARPedidoCompra(",") = '","'")) {
			String[] vars = mFunctionUtil.matcherLast(sqlResult, "$$getLocalSolicitante^VARPedidoCompra(",") = '","'");
			sqlResult = sqlResult.replace("$$getLocalSolicitante^VARPedidoCompra("+vars[1]+") = '"+vars[2]+"'", "(select p.localdeentrega from VARPedidoCompra p where p.localdeentrega ="+vars[1]+")='"+vars[2]+"'");
		}
		sqlResult = sqlResult.replace(" WWW0121.UserAccess = 1", " exists (select count(*) from WWW013 WHERE ALLOWEDLOCATIONS like '%;'||WWW0121.Location||';%')");
		//sqlResult = sqlResult.replace("$$getLocalSolicitante^VARPedidoCompra", "F_GETLOCALSOLICITANTEVARPEDIDO");
		sqlResult = sqlResult.replace(".Type ", ".Type_ ");
		sqlResult = sqlResult.replace(".Type)", ".Type_)");
		sqlResult = sqlResult.replace(".Type,", ".Type_,");
		sqlResult = sqlResult.replace("not in(' ', '')", "<> ' '");
		sqlResult = sqlResult.replace(".VARAlertaLocalLinha", ".VARAlertaLocalLinha_VW");
		sqlResult = sqlResult.replace("VARAlertaLocalLinha.", "VARAlertaLocalLinha_VW.");		
		return sqlResult;
		
	}
	private SQLUtil() {
	}
}
