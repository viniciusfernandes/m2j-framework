package $Library;

import java.util.ArrayList;
import java.util.List;

import ORM.mEntity;
import mLibrary.m$util;

public class RelationshipObject<T> extends $List<T> {
	protected List<mEntity> $entityList;
	
	public RelationshipObject() {
		super();
	}
	
	public RelationshipObject(Object parentobject) {
		super(parentobject);
	}

	public void $setEntityList(List<mEntity> entityList) {
		this.$entityList = entityList;
	}

	public List<mEntity> $getEntityList() {
		return this.$entityList;
	}

	public Object Insert(Object obj) {
		Object status = super.Insert(obj);
		if ((m$util.toInt(status) == 1) && (obj instanceof Persistent) && (this.$entityList != null)) {
			((Persistent)obj).$initializeEntity();
			this.$entityList.add(this.$list.size()-1,((Persistent)obj).$entity);
		}
		return status;
	}
	
	public Object InsertAt(Object obj, Object key) {
		Object status = super.InsertAt(obj,key);
		if ((m$util.toInt(status) == 1) && (obj instanceof Persistent) && (this.$entityList != null)) {
			((Persistent)obj).$initializeEntity();
			this.$entityList.add(m$util.toInt(key)-1,((Persistent)obj).$entity);
		}
		return status;
	}
	
	public Object Remove(Object obj) {
		Object status = super.Remove(obj);
		if ((m$util.toInt(status) == 1) && (obj instanceof Persistent) && (this.$entityList != null)) {
			((Persistent)obj).$initializeEntity();
			this.$entityList.remove(((Persistent)obj).$entity);
		}
		return status;
	}

	public Object RemoveAt(Object key) {
		Object status = super.RemoveAt(key);
		if ((m$util.toInt(status) == 1) && (this.$entityList != null)) {
			this.$entityList.remove(m$util.toInt(key)-1);
		}
		return status;
	}

}
