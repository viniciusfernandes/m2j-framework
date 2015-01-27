package mSystem;

import mLibrary.mContext;

public class Version {

	public static Object GetNumber() {
		// TODO REVISAR CONFIRMAR RETORNO DO MÉTODO
		return 1;
	}

	public static Object GetOS() {
		throw new UnsupportedOperationException();
	}

	public Version(mContext m$) {
	}

	public Object GetCompBuildOS() {
		return System.getProperty("os.name");

	}

	public Object GetMajor() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public Object Is64Bits() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public Object IsUnicode() {
		if (System.getProperty("sun.io.unicode.encoding").equals("UnicodeLittle")) {
			return 1;
		}
		else {
			return 0;
		}
	}
}
