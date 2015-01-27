package $Library;

import java.util.ArrayList;

import mLibrary.m$op;
import mLibrary.m$util;

public class $List<T> extends ListOfObjects<T> {
	protected Object $object = null;

	public $List() {
		super();
	}

	public $List(Object object) {
		this.$initializeObject(object);
	}

	public void $initializeObject(Object object) {
		if (object instanceof Persistent) {
			this.$object = object;
		}
		else {
			this.$object = null;
		}
		this.$list = new ArrayList<Object>();
		this.$modified = false;
	}

	public void $SetModified(Object value) {
		if (this.$object != null) {
			((Persistent)this.$object).$SetModified(value);
		}
	}
	
}
