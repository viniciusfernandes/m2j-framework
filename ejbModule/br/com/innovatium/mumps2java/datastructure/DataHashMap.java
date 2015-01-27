package br.com.innovatium.mumps2java.datastructure;

public class DataHashMap {

  static final int DEFAULT_INITIAL_CAPACITY = 16;

  static final float DEFAULT_LOAD_FACTOR = 0.75f;

  static final int MAXIMUM_CAPACITY = 1 << 30;

  static int indexFor(int h, int length) {
    return h & (length-1);
  }

  final float loadFactor;

  int size;

  DataHashMapEntry[] table;
	
  int threshold;
	
	/**
	 * Constructs an empty <tt>HashMap</tt> with the default initial capacity
	 * (16) and the default load factor (0.75).
	 */
	public DataHashMap() {
	    this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructs an empty <tt>HashMap</tt> with the specified initial
	 * capacity and the default load factor (0.75).
	 *
	 * @param  initialCapacity the initial capacity.
	 * @throws IllegalArgumentException if the initial capacity is negative.
	 */
	public DataHashMap(int initialCapacity) {
	    this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}

  public DataHashMap(int initialCapacity, float loadFactor) {
    if (initialCapacity < 0)
        throw new IllegalArgumentException("Illegal initial capacity: " +
                                           initialCapacity);
    if (initialCapacity > MAXIMUM_CAPACITY)
        initialCapacity = MAXIMUM_CAPACITY;
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
        throw new IllegalArgumentException("Illegal load factor: " +
                                           loadFactor);

    // Find a power of 2 >= initialCapacity
    int capacity = 1;
    while (capacity < initialCapacity)
        capacity <<= 1;

    this.loadFactor = loadFactor;
    this.threshold = (int)Math.min(capacity * loadFactor, MAXIMUM_CAPACITY + 1);
    this.table = new DataHashMapEntry[capacity];
	}

  public Object get(Object key) {
    int hash;
  	if (key == null) {
    	hash = 0;
    }
    else {
    	hash = key.hashCode();
    }
    int i = indexFor(hash, table.length);
    DataHashMapEntry ei = table[i];
    for (DataHashMapEntry e = ei; e != null; ei = e, e = e.next) {
        Object k;
        if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
            return e.value;
        }
    }
    return null;
  }

  public Object put(Object key, Object value) {
    int hash;
  	if (key == null) {
    	hash = 0;
    }
    else {
    	hash = key.hashCode();
    }
    int i = indexFor(hash, table.length);
    DataHashMapEntry ei = table[i];
    for (DataHashMapEntry e = ei; e != null; ei = e, e = e.next) {
        Object k;
        if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
            Object oldValue = e.value;
            e.value = value;
            return oldValue;
        }
    }
    this.size++;
    DataHashMapEntry e = new DataHashMapEntry();
    e.key = key;
    e.value = value;
    e.hash = hash;
    if (ei == null) {
    	table[i] = e;
    }
    else {
    	ei.next = e;
    }
    return null;
  }

  public Object remove(Object key) {
    int hash;
  	if (key == null) {
    	hash = 0;
    }
    else {
    	hash = key.hashCode();
    }
    int i = indexFor(hash, table.length);
    DataHashMapEntry prev = table[i];
    DataHashMapEntry e = prev;
    while (e != null) {
        DataHashMapEntry next = e.next;
        Object k;
        if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))) {
            size--;
            if (prev == e)
                table[i] = next;
            else
                prev.next = next;
            e.next = null;
            return e.value;
        }
        prev = e;
        e = next;
    }
    return null;
  }

}
