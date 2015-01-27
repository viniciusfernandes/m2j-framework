package mLibrary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import util.DataStructureUtil;
import util.mFunctionUtil;

public final class mListBuild {
	public static mListBuild getList(String val) {
		List<Object> lo = new ArrayList<Object>();
		if (val.isEmpty()) {
			return null;
		}
		while (true) {
			int len = 0;
			if (mFunctionUtil.integerConverter(mFunction.$ascii(mFunction.$extract(val, 1, 1))) == 0
					& mFunctionUtil.integerConverter(mFunction.$ascii(mFunction.$extract(val, 4, 4))) == 1) {
				len = mFunctionUtil.integerConverter(mFunction.$ascii(mFunction.$extract(val, 2, 2)))
						+ (mFunctionUtil.integerConverter(mFunction.$ascii(mFunction
								.$extract(val, 3, 3))) * 256) + 3;
				lo.add(mFunction.$extract(val, 5, len));
			} else if (mFunctionUtil.integerConverter(mFunction.$ascii(mFunction.$extract(val, 1, 1))) == 1) {
				len = mFunctionUtil.integerConverter(mFunction.$ascii(mFunction.$extract(val, 1, 1)));
				lo.add("");
			} else if (mFunctionUtil.integerConverter(mFunction.$ascii(mFunction.$extract(val, 2, 2))) == 1) {
				len = mFunctionUtil.integerConverter(mFunction.$ascii(mFunction.$extract(val, 1, 1)));
				lo.add(mFunction.$extract(val, 3, len));
			} else {
				break;
			}
			if (mFunctionUtil.integerConverter(mFunction.$length(val)) < len) {
				break;
			}
			val = mFunction.$extract(val, len + 1,null);
			if (val.isEmpty()) {
				break;
			}
		}
		if (val.isEmpty()) {
			return mListBuild.add(lo.toArray());
		}
		return null;

	}

	public static String getString(mListBuild val) {
		if (val == null) {
			return null;
		}
		
		StringBuilder listAsString = new StringBuilder();
		List list = val.list;
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if(obj==null){
				listAsString.append(mFunction.$char(0));
				continue;
			}
			String array_element = obj.toString();
			
			int len = array_element.length();
			if(len>253){
				listAsString.append(mFunction.$char(0));
				listAsString.append(mFunction.$char((len+1)%256));
				listAsString.append(mFunction.$char((len+1)/256));
				listAsString.append(mFunction.$char(1));
				listAsString.append(array_element);
			}else{
				listAsString.append(mFunction.$char(len+2));
				listAsString.append(mFunction.$char(1));
				listAsString.append(array_element);
			}			
		}
		return listAsString.toString();

	};

	public static boolean isList(String val) {
		return getList(val)!=null;
	}

	static mListBuild add(Object... elements) {
		mListBuild l = new mListBuild();
		if (elements == null) {
			return l;
		}
		l.list.addAll(Arrays.asList(elements));
		return l;
	}

	static mListBuild concat(mListBuild... lists) {
		mListBuild l = new mListBuild();
		if (lists != null && lists.length > 0) {
			for (mListBuild listObject : lists) {
				l.list.addAll(listObject.list);
			}
		}
		return l;
	}

	static int find(Object listObj, Object obj, Object startAfter) {
		if (listObj instanceof mListBuild) {

			int init = mFunctionUtil.integerConverter(startAfter);
			if (init == -1) {
				return 0;
			} else if (init <= -2) {
				throw new IllegalArgumentException(
						"Start less than -1 is not allowed");
			}

			List<Object> list = ((mListBuild) listObj).list;
			for (int i = init; i < list.size(); i++) {
				Object item = list.get(i);
				item = mFunctionUtil.toString(item);
				obj = mFunctionUtil.toString(obj);
				if (item.equals(obj)) {
					return i + 1;
				}
			}
		}
		return 0;
	}

	static mListBuild getInstance() {
		return new mListBuild();
	}

	static mListBuild listFromString(String string, String delimiter) {
		mListBuild l = new mListBuild();
		if (string.isEmpty()) {
			return l;
		}
		l.list.addAll(Arrays.asList(string.split(Pattern.quote(delimiter))));
		return l;
	}

	private final List<Object> list = new LinkedList<Object>();

	private mListBuild() {
	}

	public Object element(int position) {
		if (position == 0) {
			return "";
		}
		if (position < 0) {
			position = list.size() - 1;
		} else {
			position = position - 1;
		}
		return this.list.get(position);
	}

	public int find(Object obj, Object start) {
		return find(this, obj, start);
	}
	
	public Object firstElement() {
		return element(1);
	}
	public String getString() {
		return getString(this);
	}
	public int length() {
		int length = list.size();
		if (length == 1) {
			if ("".equals(list.get(0))) {
				return 0;
			}
		}
		return length;
	}
	public String toString() {
		int lastDelimiter = list.size() - 1;
		int count = 0;
		StringBuilder string = new StringBuilder();
		for (Object o : list) {
			string.append(o);
			if (count < lastDelimiter) {
				string.append(DataStructureUtil.DELIMITER);
			}
			count++;
		}
		return string.toString();
	}
	mListBuild sublist(int init, int end) {
		final mListBuild l = new mListBuild();
		if (init < 0) {
			init = 0;
		}

		if (end > this.list.size()) {
			end = this.list.size();
		}

		if (init > end) {
			init = end;
		}

		l.list.addAll(this.list.subList(init, end));
		return l;
	}

}
