package mSystem;

import java.util.Calendar;

import util.mFunctionUtil;
import br.com.innovatium.mumps2java.todo.TODO;

public class SQL {

	@TODO
	public Object DATEADD(String string, Object object, Object object2) {
		throw new UnsupportedOperationException();	
	}

	@TODO
	public Object DATEDIFF(String string, Object object, Object $zdate) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(mFunctionUtil.dateMumpsToJava(object).longValue());
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(mFunctionUtil.dateMumpsToJava($zdate).longValue());
		if(string.equals("year")){
			return cal2.get(Calendar.YEAR)-cal1.get(Calendar.YEAR);
		}else{
			throw new UnsupportedOperationException();
		}
			
	}

	public void PurgeForTable(Object object) {
		throw new UnsupportedOperationException();
		
	}
	@TODO
	public Object SQLCODE(Object object) {
		throw new UnsupportedOperationException();	
	}
	public void TuneTable(Object object, int i, int j, Object object2, int k) {
		throw new UnsupportedOperationException();
		
	}
	public Object YEAR(Object $zdate) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
