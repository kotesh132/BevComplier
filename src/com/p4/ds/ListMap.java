package com.p4.ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A type of multimap.  Allows duplicate keys by storing values in lists.  Values are added and removed instead of put.
 * get() returns THE list that is stored as the real value in the map.
 * <p>
 * Uses {@link ArrayList}.  To change the {@link List} implementation, override {@link #createCollection()}.
 *f
 */
public class ListMap<K, V> implements Map<K, List<V>>
{

    private Map<K, List<V>> m_map;
    private Class<? extends List<V>> m_collectionClass;
    private static final ListMap<Object, Object> EMPTY_LIST_MAP = new EmptyListMap();

    /**
     * Constructor for ListMap.
     */
    public ListMap()
    {
        // this double-cast actually defeats the compiler into letting us make this desired assigment
        m_collectionClass = (Class<? extends List<V>>) ((Class<? extends Collection>) ArrayList.class);
        m_map = new HashMap<K, List<V>>();
    }

    /**
     * Constructor for ListMap.  Creates Lists with the given initial capacity.
     */
    public ListMap(int capacity)
    {
        m_collectionClass = (Class<? extends List<V>>) ((Class<? extends Collection>) ArrayList.class);
        m_map = new HashMap<K, List<V>>(capacity);
    }

    public ListMap(Class collectionClass)
    {
        if (!List.class.isAssignableFrom(collectionClass)) {
            throw new RuntimeException("Collection class doesn't extend java.util.List");
        }
        m_collectionClass = (Class<? extends List<V>>) collectionClass;
        m_map = new HashMap<K, List<V>>();
    }

    public ListMap(Class<? extends Map> mapClass, Class<? extends List> collectionClass)
    {
        if (!List.class.isAssignableFrom(collectionClass)) {
            throw new IllegalArgumentException("Collection class doesn't extend java.util.List");
        }
        m_collectionClass = (Class<? extends List<V>>) collectionClass;
        m_map = createMap((Class<? extends Map<K, List<V>>>) mapClass);
    }

    protected Map<K, List<V>> createMap(Class<? extends Map<K, List<V>>> mapClass)
    {
        Map<K, List<V>> m;
        try {
            m = mapClass.newInstance();
        }
        catch (InstantiationException e) {
            throw new IllegalStateException("Couldn't create ListMap", e);
        }
        catch (IllegalAccessException e) {
            throw new IllegalStateException("Couldn't create ListMap", e);
        }
        return m;
    }

    /**
     * Factory method for the List implementation. This may be overriden by subclasses.
     *
     * @return A new List
     */
    protected List<V> createCollection()
    {
        List<V> c;
        try {
            c = m_collectionClass.newInstance();
        }
        catch (InstantiationException e) {
            throw new IllegalStateException("Couldn't create Collection", e);
        }
        catch (IllegalAccessException e) {
            throw new IllegalStateException("Couldn't create Collection", e);
        }
        return c;
    }

    protected Map<K, List<V>> getMap()
    {
        return m_map;
    }

    /**
     * Add the object 'value' to the list indexed by 'key'
     *
     * @param key index to list
     * @param value new value to be added to the indexed list
     *
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     */
    public boolean add(K key, V value)
    {
        return getList(key).add(value);
    }

    /**
     * Add the specified value if it has not already been added for the specified key Warning: It will have poor
     * performance if the key has many values
     *
     * @param key
     * @param value
     */
    public void addUnique(K key, V value)
    {
        List<V> collection = pull(key);
        if (collection == null || !collection.contains(value)) {
            add(key, value);
        }
    }

    /**
     * Add all the items in values to the list indexed by 'key'
     *
     * @param key
     * @param values
     */
    public void addAll(K key, Collection<V> values)
    {
        getList(key).addAll(values);
    }

    /**
     * Add all the items in values to the list indexed by 'key'
     *
     * @param pos position in list to add items
     * @param key
     * @param values
     */
    public void addAll(int pos, K key, Collection<V> values)
    {
        getList(key).addAll(pos, values);
    }

    public void addAll(ListMap<K, V> listMap)
    {
        for (K key : listMap.keySet()) {
            List<V> list = listMap.pull(key);
            if (list != null) {
                getList(key).addAll(list);
            }
        }
    }

    public void addAll(Map<K, List<V>> listMap)
    {
        for (K key : listMap.keySet()) {
            List<V> list = listMap.get(key);
            if (list != null) {
                getList(key).addAll(list);
            }
        }
    }

    public boolean removeFromMap(K key, V value)
    {
        List<V> col = pull(key);
        if (col != null) {
            boolean b = col.remove(value);
            if (col.isEmpty()) {
                m_map.remove(key);
            }
            return b;
        }
        return false;
    }

    /**
     * @see java.util.Map#remove(java.lang.Object)
     */
    public List<V> remove(Object key)
    {
        return m_map.remove(key);
    }

    public List<V> removeList(K key)
    {
        return m_map.remove(key);
    }

    public Set<Map.Entry<K, List<V>>> entrySet()
    {
        return m_map.entrySet();
    }

    public Set<K> keySet()
    {
        return m_map.keySet();
    }

    /**
     * Returns a {@link Collection} containing {@link List Lists}.
     */
    public Collection<List<V>> valueSet()
    {
        return m_map.values();
    }

    /**
     * Returns the list associated with the key.  Creates the list if it doesn't exist.
     *
     * @param key
     *
     * @return the List associated with key
     *
     * @see #get(java.lang.Object)
     */
    public List<V> getList(K key)
    {
        List<V> c = m_map.get(key);
        if (c == null) {
            c = createCollection();
            m_map.put(key, c);
        }
        return c;
    }

    /**
     * Returns the List associated with the key.  Creates the collection if it doesn't exist.
     *
     * @param key
     *
     * @return the List associated with key
     *
     * @see #getList
     * @see java.util.Map#get(java.lang.Object)
     */
    public List<V> get(Object key)
    {
        return getList((K) key);
    }

    /**
     * Pulls the list out of the map without creating it.  May return null.
     *
     * @param key
     *
     * @return the list associated with key, or null if there is none.
     */
    public List<V> pull(K key)
    {
        return m_map.get(key);
    }

    public final List<V> pullReadOnlySafeList(K key)
    {
        List<V> c = pull(key);
        if (c == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(c);
    }

    /**
     * Returns <code>true</code> if there is an entry for the given key.
     *
     * @param key
     *
     * @return true if there is an entry for this key
     */
    public boolean contains(K key)
    {
        return m_map.containsKey(key);
    }

    /**
     * Create a new List for the specified key, if it's not already used
     *
     * @param key
     *
     * @return the newly created List or null if the key is already used.
     */
    public List<V> create(K key)
    {
        List<V> c = null;
        if (!m_map.containsKey(key)) {
            c = createCollection();
            m_map.put(key, c);
        }
        return c;
    }

    /**
     * Empty the map (all Lists will be dropped from the map)
     */
    public void clear()
    {
        m_map.clear();
    }

    /**
     * Returns true if there is an entry (a collection) for the given key and that collection contains value
     *
     * @param key
     * @param value
     *
     * @return true if key and value was found
     */
    public boolean contains(K key, V value)
    {
        List<? extends V> col = pull(key);
        if (col != null) {
            return col.contains(value);
        }
        return false;
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified key.
     *
     * @param key key whose presence in this map is to be tested.
     *
     * @return <tt>true</tt> if this map contains a mapping for the specified key.
     */
    public boolean containsKey(Object key)
    {
        return m_map.containsKey(key);
    }

    public boolean containsValue(Object value)
    {
        if (m_map.containsValue(value)) {
            return true;
        }
        for (Collection<V> col : values()) {
            if (col.contains(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return true if there are no keys in the map.
     */
    public boolean isEmpty()
    {
        return m_map.isEmpty();
    }

    /**
     * Tests if the list indicated by 'key' is empty.
     *
     * @param key
     *
     * @return true if there is no such key in the map or if the list there is empty
     */
    public boolean isEmpty(K key)
    {
        List<V> l = pull(key);
        return (l == null || l.isEmpty());
    }

    /**
     * Returns the number of keys in this.
     *
     * @return
     */
    public int size()
    {
        return m_map.size();
    }

    /**
     * Returns the number of values in the list indicated by key
     *
     * @param key key whose mapped list we want the size of (sorry, need to phrase that better)
     *
     * @return size of the mapped list, or 0 if there is no such key.
     */
    public int size(K key)
    {
        List<V> l = pull(key);
        if (l == null) {
            return 0;
        }
        else {
            return l.size();
        }
    }

    /**
     * Returns a {@link Collection} of the value {@link List Lists} in this Map.
     */
    public Collection<List<V>> values()
    {
        return m_map.values();
    }

    /**
     * Set an entire List to be associated with this Key
     *
     * @param key
     * @param value
     *
     * @return previous value associated with specified key, or <tt>null</tt> if there was no mapping for key.
     *
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public List<V> put(K key, List<V> value)
    {
        return m_map.put(key, value);
    }

    /**
     * Copies all of the mappings from the specified map to this map. The effect of this call is equivalent to that of
     * calling {@link #put(Object, Object) put(k, v)} on this map once for each mapping from key <tt>k</tt> to value
     * <tt>v</tt> in the specified map.  The behavior of this operation is unspecified if the specified map is modified
     * while the operation is in progress.
     *
     * @param map Mappings to be stored in this map.
     *
     * @throws UnsupportedOperationException if the <tt>putAll</tt> method is not supported by this map.
     * @throws ClassCastException if the class of a key or value in the specified map prevents it from being stored in
     * this map.
     * @throws IllegalArgumentException some aspect of a key or value in the specified map prevents it from being stored
     * in this map.
     * @throws NullPointerException the specified map is <tt>null</tt>, or if this map does not permit <tt>null</tt>
     * keys or values, and the specified map contains <tt>null</tt> keys or values.
     * @see java.util.Map#putAll(java.util.Map)
     */

    public void putAll(Map<? extends K, ? extends List<V>> map)
    {
        for (K key : map.keySet()) {
            put(key, map.get(key));
        }
    }

    /**
     * Return all value items from all List
     *
     * @return A new list containing all items from all Lists in the ListMap.
     */
    public List<V> items()
    {
        List<V> items = createCollection();
        for (Collection<V> col : values()) {
            items.addAll(col);
        }
        return items;
    }

    /**
     * Return the key that would be used to access the List containing 'value'
     *
     * @param value
     *
     * @return null if the value is not found is not found in any key
     */
    public K findKey(V value)
    {
        for (K key : m_map.keySet()) {
            if (getList(key).contains(value)) {
                return key;
            }
        }
        return null;
    }

    public void reverseLists()
    {
        for (List<V> value : values()) {
            Collections.reverse(value);
        }
    }

    public static <K, V> ListMap<K, V> emptyListMap()
    {
        return (ListMap<K, V>) EMPTY_LIST_MAP;
    }

    private static class EmptyListMap extends ListMap<Object, Object>
    {

        private EmptyListMap()
        {
        }

        public int size()
        {
            return 0;
        }

        public boolean isEmpty()
        {
            return true;
        }

        public boolean containsKey(Object key)
        {
            return false;
        }

        public boolean containsValue(Object value)
        {
            return false;
        }

        public List<Object> get(Object key)
        {
            return Collections.emptyList();
        }

        public List<Object> put(Object key, List<Object> value)
        {
            throw new UnsupportedOperationException();
        }

        public List<Object> remove(Object key)
        {
            return null;
        }

        public void putAll(Map<? extends Object, ? extends List<Object>> map)
        {
            throw new UnsupportedOperationException();
        }

        public void clear()
        {
            // do nothing
        }

        public Set<Object> keySet()
        {
            return Collections.emptySet();
        }

        public Collection<List<Object>> values()
        {
            return Collections.emptySet();
        }

        public Set<Map.Entry<Object, List<Object>>> entrySet()
        {
            return Collections.emptySet();
        }

        public boolean add(Object key, Object value)
        {
            throw new UnsupportedOperationException();
        }

        public void addUnique(Object key, Object value)
        {
            throw new UnsupportedOperationException();
        }

        public void addAll(Object key, Collection<Object> values)
        {
            throw new UnsupportedOperationException();
        }

        public void addAll(int pos, Object key, Collection<Object> values)
        {
            throw new UnsupportedOperationException();
        }

        public void addAll(ListMap<Object, Object> listMap)
        {
            throw new UnsupportedOperationException();
        }

        public void addAll(Map<Object, List<Object>> listMap)
        {
            throw new UnsupportedOperationException();
        }

        public boolean removeFromMap(Object key, Object value)
        {
            return false;
        }

        public List<Object> removeList(Object key)
        {
            return null;
        }

        public Collection<List<Object>> valueSet()
        {
            return Collections.emptySet();
        }

        public List<Object> getList(Object key)
        {
            return Collections.emptyList();
        }

        public List<Object> pull(Object key)
        {
            return Collections.emptyList();
        }

        public boolean contains(Object key)
        {
            return false;
        }

        public List<Object> create(Object key)
        {
            throw new UnsupportedOperationException();
        }

        public boolean contains(Object key, Object value)
        {
            return false;
        }

        public boolean isEmpty(Object key)
        {
            return true;
        }

        public int size(Object key)
        {
            return 0;
        }

        public List<Object> items()
        {
            return Collections.emptyList();
        }

        public Object findKey(Object value)
        {
            return null;
        }
    }
}
