package com.p4.ids;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface IDisjointSet<T>
{

	/**
	 * Makes a set of single element for the specified object
	 *
	 * @param object object to add
	 *
	 * @return node representing the specified object
	 */
	IDisjointSetNode<T> add(T object);

	/**
	 * Makes sets of single element for all objects in the specified iterator
	 *
	 * @param itr iterator of objects to add
	 *
	 * @return List of nodes representing the objects in the specified iterator
	 */
	List<IDisjointSetNode<T>> addAll(Iterator<T> itr);

	/**
	 * Adds all elements of iterator in new equivalenceSet
	 */
	void addEquivalenceSet(Collection<T> objects);

	/**
	 * Finds the root of the set containing a given object.
	 *
	 * @param object object for which its root is required
	 *
	 * @return root of the set containing a given object.
	 */
	IDisjointSetNode<T> findRoot(T object);

	/**
	 * Combines the set that contains <code>object1</code>  with the set that contains <code>object2</code>.
	 *
	 * @param object1 set of this object to which the set of other object will be combined
	 * @param object2 set of this object combines with the set of other object
	 *
	 * @return true if the specified elements are already connected
	 */
	boolean union(T object1, T object2);

	/**
	 * Returns the flag whether the specified objects belong to the same set.
	 *
	 * @param object1 object one
	 * @param object2 object two
	 *
	 * @return true if the specified objects belong to the same set.
	 */
	boolean isEquivalent(T object1, T object2);

	/**
	 * Returns the equivalence sets that exists in this data structure. Or in other words set of disjoint sets.
	 *
	 * @return equivalence sets that exists in this data structure
	 */
	Collection<Set<T>> getEquivalenceSets();

	/**
	 * Returns the flag indicating whether there is single set in this disjoint set data structure.
	 *
	 * @return true if there is single set in this disjoint set data structure.
	 */
	boolean isSingleSet();

	/**
	 * Returns the flag indicating whether this data structure is empty.
	 *
	 * @return true if there are no elements this disjoint set data structure.
	 */
	boolean isEmpty();

	/**
	 * Indicates whether the specified object has been added to the disjoint set.
	 *
	 * @param object object to check containment for
	 *
	 * @return true if the object has been added to the set or false otherwise.
	 */
	boolean contains(T object);

	/**
	 * Clear the contents of this set.
	 */
	void clear();
}
