package mSystem;

import mLibrary.mClass;
import mLibrary.mContext;
import mLibrary.mFunction;
import mLibrary.mOperation;
import mLibrary.mVar;
import mLibrary.m$util;
import mLibrary.m$op;

public class Status {

	public static void DecomposeStatus(Object pSC, mVar arrErr) {
		String SC = m$util.toString(pSC);
		arrErr.set(0);
		int index = 2, msize = 0, psize = 0;
		StringBuilder pvalue = new StringBuilder();
		boolean first = false;
		while (true) {
			if ((psize == 0) && (pvalue.length() > 0)) {
				if (pvalue.codePointAt(0) == 4) {
					arrErr.set(m$op.Add(arrErr.get(),1));
					arrErr.s$(arrErr.get()).set("ERROR #"+m$util.toString(pvalue.codePointAt(1)+pvalue.codePointAt(2)*256));
					first = true;
				}
				else if (pvalue.codePointAt(0) == 1) {
					arrErr.s$(arrErr.get()).set(m$op.Concat(arrErr.s$(arrErr.get()).get(),((first)?": ":" - ")+pvalue.substring(1)));
				}
			}
			if (index >= SC.length()) {
				break;
			}
			if (msize <= 0) {
				if (SC.codePointAt(index) != 0) {
					msize = SC.codePointAt(index);
				}
				else {
					msize = SC.codePointAt(index+1)+(SC.codePointAt(index+2)*256)+1;
					index = index+2;
				}
				msize = msize-1;
			}
			else if (psize <= 0) {
				if (SC.codePointAt(index) != 0) {
					psize = SC.codePointAt(index);
				}
				else {
					psize = SC.codePointAt(index+1)+(SC.codePointAt(index+2)*256)+1;
					index = index+2;
					msize = msize-2;
				}
				psize = psize-1;
				msize = msize-1;
				pvalue = new StringBuilder();
			}
			else {
				pvalue.append(SC.charAt(index));
				psize = psize-1;
				msize = msize-1;
			}
			index++;
		}
	}

	public Status(mContext m$) {
	}

	public Object AppendStatus(Object pSC1, Object pSC2) {
		String SC1 = m$util.toString(pSC1);
		String SC2 = m$util.toString(pSC2);
		if ((SC1.length() > 2) && SC2.length() > 2) {
			return "0 "+SC1.substring(2)+SC2.substring(2);
		}
		else if (SC1.length() > 2) {
			return SC1;
		}
		else if (SC2.length() > 2) {
			return SC2;
		}
		else if ((m$util.toBoolean(this.IsOK(SC1))) && (m$util.toBoolean(this.IsOK(SC2)))) {
			return 1;
		}
		else {
			return 0;
		}
	}

	public Object Error(Object code, Object... args) {
		int errorcode = m$util.toInt(code);
		StringBuilder SC = new StringBuilder();
		SC.append(Character.toChars(1));
		SC.append(Character.toChars(4)).append(Character.toChars(4)).append(Character.toChars(errorcode%256)).append(Character.toChars(errorcode/256%256));
		for (int i=0;i<9;i++) {
			String arg = "";
			if (i < args.length) {
				arg = m$util.toString(args[i]);
			}
			else if (i == 8) {
				arg = "zLabel+999^Routine:DEFAULT";
			}
			if (arg.isEmpty()) {
				SC.append(Character.toChars(1));
			}
			else {
				int l = arg.length()+2;
				if (l < 256) {
					SC.append(Character.toChars(l)).append(Character.toChars(1)).append(arg);
				}
				else {
					l = l-1;
					SC.append(Character.toChars(0)).append(Character.toChars(l%256)).append(Character.toChars(l/256%256)).append(Character.toChars(1)).append(arg);
				}
			}
		}
		StringBuilder SCarg = SC;
		SC = new StringBuilder();
		int l = SCarg.length()+1;
		if (l < 256) {
			SC.append("0 ").append(Character.toChars(1)).append(SCarg);
		}
		else {
			l = l-1;
			SC.append("0 ").append(Character.toChars(0)).append(Character.toChars(l%256)).append(Character.toChars(l/256%256)).append(SCarg);
		}
		return SC.toString();
	}

	public Object IsOK(Object object) {
		if (object instanceof mClass) {
			return true;
		}
		return mOperation.NotEqual(m$util.toInt(object),0);
	}

	public Object IsError(Object object) {
		if (object instanceof mClass) {
			return false;
		}
		return mOperation.Equal(m$util.toInt(object),0);
	}

}
