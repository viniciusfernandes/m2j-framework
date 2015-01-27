package ORM;

import java.util.ArrayList;
import java.util.List;

/**
 * Coleção de entidades persistentes de um relacionamento do tipo 1:N
 * 
 * @author Innovatium Systems (mosselaar)
 */
public class mEntityList {
	private final mEntity entityOwner;
	private final String relationProperty;
	private List<mEntity> list;
	private List<mEntity> removedList;

	mEntityList(mEntity entityOwner, List<mEntity> list, String relationProperty) {
		this.entityOwner = entityOwner;
		this.list = list;
		if (this.list == null) {
			this.list = new ArrayList<mEntity>();
		}
		this.removedList = new ArrayList<>();
		this.relationProperty = relationProperty;
	}

	public void add(int index, mEntity entity) {
		list.add(index, entity);
		entity.setEntityObj(relationProperty, entityOwner);
	}

	public void add(mEntity entity) {
		list.add(entity);
		entity.setEntityObj(relationProperty, entityOwner);
	}

	public mEntity get(int index) {
		return list.get(index);
	}

	public List<mEntity> getEntities() {
		return list;
	}

	public void remove(int index) {
		remove(list.get(index));
	}

	public void remove(mEntity entity) {
		list.remove(entity);
		removedList.add(entity);
		entity.setEntityObj(relationProperty, null);
	}

	public void save() {
		for (mEntity entity : list) {
			entity.save();
		}
		for (mEntity entity : removedList) {
			entity.save();
		}
		removedList = new ArrayList<>();
	}

	public void set(int index, mEntity entity) {
		if (list.size() > index) {
			list.remove(index);
		}
		list.add(index, entity);
	}

	public int size() {
		return list.size();
	}

}
