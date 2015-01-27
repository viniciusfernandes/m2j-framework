package $Library;

import java.util.ArrayList;

import mLibrary.*;

public class ListOfObjects<T> extends mClass {
	ArrayList<Object> $list = null;
	Boolean $modified = null;

	public ListOfObjects() {
		this.$list = new ArrayList<Object>();
		this.$modified = false;
	}

	public Object $IsModified() {
		return this.$modified;
	}
	
	public Object $ObjectModified() {
		return this.$modified;
	}
	
	public void $SetModified() {
		this.$SetModified(true);
	}
	
	public void $SetModified(Object value) {
		this.$modified = m$op.Logical(value);
	}
	
	public Object Clear() {
		try {
			this.$SetModified();
			this.$list = new ArrayList<Object>();
			this.$modified = false;
			return 1;
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	public Object Count() {
		return this.$list.size();
	}
	
	public Object IsDefined(Object key) {
		try {
			if (m$util.toInt(key) > this.$list.size()) {
				return 0;
			}
			if (this.$list.get(m$util.toInt(key)-1) == null) {
				return 0;
			}
			return 1;
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	public Object GetAt(Object key) {
		try {
			return this.$list.get(m$util.toInt(key)-1);
		}
		catch (Exception e) {
			return "";
		}
	}
	
	public Object GetNext(Object key) {
		Object keyval = key;
		if (key instanceof mVar) {
			keyval = ((mVar) key).get();
		}
		int keyint = m$util.toInt(keyval)+1;
		if ((keyint > 0) && (keyint <= $list.size())) {
			if (key instanceof mVar) {
				((mVar) key).set(keyint);
			}
			return this.$list.get(keyint-1);
		}
		else {
			if (key instanceof mVar) {
				((mVar) key).set("");
			}
			return "";
		}
	}
	
	public Object GetPrevious(Object key) {
		Object keyval = key;
		if (key instanceof mVar) {
			keyval = ((mVar) key).get();
		}
		int keyint = m$util.toInt(keyval)-1;
		keyint = ((keyint==-1)?$list.size():keyint);
		if ((keyint > 0) && (keyint <= $list.size())) {
			if (key instanceof mVar) {
				((mVar) key).set(keyint);
			}
			return this.$list.get(keyint-1);
		}
		else {
			if (key instanceof mVar) {
				((mVar) key).set("");
			}
			return "";
		}
	}
	
	public Object Insert(Object obj) {
		try {
			this.$list.add(obj);
			this.$SetModified();
			return 1;
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	public Object InsertAt(Object obj, Object key) {
		try {
			this.$list.add(m$util.toInt(key)-1,obj);
			this.$SetModified();
			return 1;
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	public Object Next(Object key) {
		int keyint = m$util.toInt(key)+1;
		if ((keyint > 0) && (keyint <= $list.size())) {
			return keyint;
		}
		else {
			return "";
		}
	}
	
	public Object Previous(Object key) {
		int keyint = m$util.toInt(key)-1;
		keyint = ((keyint==-1)?$list.size():keyint);
		if ((keyint > 0) && (keyint <= $list.size())) {
			return keyint;
		}
		else {
			return "";
		}
	}

	public Object Remove(Object obj) {
		try {
			if (this.$list.remove(obj)) {
				return obj;
			}
			return "";
		}
		catch (Exception e) {
			return "";
		}
	}

	public Object RemoveAt(Object key) {
		try {
			Object obj = this.$list.remove(m$util.toInt(key)-1);
			this.$SetModified();
			return obj;
		}
		catch (Exception e) {
			return "";
		}
	}

	public Object SetAt(Object obj, Object key) {
		return this.InsertAt(obj,key);
	}
}
