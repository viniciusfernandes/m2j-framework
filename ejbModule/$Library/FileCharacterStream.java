package $Library;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import mLibrary.mClass;
import mLibrary.mContext;
import mLibrary.mVar;

public class FileCharacterStream extends mClass {
	public Boolean AtEnd = true;
	BufferedReader bufferedReader;
	StringBuffer data;
	String Filename = "C:\\Alphalinc\\TEMP\\";

	public Object $New(Object... args) {
		Filename = Filename + m$.var("YFORM").getValue()
				+ Math.random() + ".stream";
		data = new StringBuffer();
		return this;
	}

	public Object OutputToDevice(Object... args) {
		m$.Cmd.Write(data.toString());
		return true;
	}

	public Object Read(Object... args) {
		Object variable = null;
		Object scan = null;
		try {

			if (bufferedReader == null) {
				bufferedReader = new BufferedReader(new FileReader(Filename));
			}

			scan = bufferedReader.readLine();
			if (scan == null) {
				AtEnd = true;
				return scan;
			}
			if (variable instanceof mVar) {
				mVar var = (mVar) variable;
				if (scan == null) {
					var.set("");
				} else {
					var.set(scan);
				}
			}
		} catch (Exception e) {
			Logger.getLogger(mClass.class.getName()).log(Level.SEVERE, null, e);
		} finally {

		}
		return scan;

	}

	public void SaveStream() {
		m$.Cmd.Open(Filename);
		m$.Cmd.Use(Filename);
		m$.Cmd.Write(data);
		try {
			m$.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		m$.Cmd.Close(Filename);
	}

	public Object Write(Object string) {
		if (!m$.getIO().toString().equals(Filename)) {
			m$.Open(Filename,"n");
			AtEnd = false;
		}
		data.append(string);
		return true;
	}

	public Object WriteLine(Object... args) {
		String argStr = (args != null && args.length > 0) ? "" + args[0] : "";
		data.append(argStr + "\n");
		return true;
	}
}
