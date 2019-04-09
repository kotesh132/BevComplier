package com.p4.ds;

import com.p4.ids.IDisjointSet;
import com.p4.ids.IDisjointSetNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DisjointSet<T> implements IDisjointSet<T>
{

	private Map<T, IDisjointSetNode<T>> m_objectToNode = new LinkedHashMap<T, IDisjointSetNode<T>>();

	public DisjointSet()
	{

	}

	public DisjointSet(Collection<T> objects)
	{
		addAll(objects.iterator());
	}

	public IDisjointSetNode<T> add(T object)
	{
		return m_objectToNode.computeIfAbsent(object, DisjointSetNode::new);
	}

	public List<IDisjointSetNode<T>> addAll(Iterator<T> itr)
	{
		List<IDisjointSetNode<T>> nodes = new ArrayList<IDisjointSetNode<T>>();
		while (itr.hasNext()) {
			nodes.add(add(itr.next()));
		}
		return nodes;
	}

	@Override
	public void addEquivalenceSet(Collection<T> objects) {

		addAll(objects.iterator());
		IDisjointSetNode<T> root = null;
		Iterator<T> itr = objects.iterator();
		while (itr.hasNext()) {
			if (root == null) {
				root = findRoot(itr.next());
			} else {
				union(root.getObject(), itr.next());
			}
		}
	}

	public IDisjointSetNode<T> findRoot(T object)
	{
		IDisjointSetNode<T> node = m_objectToNode.get(object);
		assert node != null : "Expected node to be present already";
		IDisjointSetNode<T> ultimateParent = node;
		while (ultimateParent.getParent() != null) {
			ultimateParent = ultimateParent.getParent();
		}
		//Path compression
		IDisjointSetNode<T> pathNode = node;
		while (pathNode != ultimateParent) {
			IDisjointSetNode<T> pathNodeParent = pathNode.getParent();
			pathNode.setParent(ultimateParent);
			pathNode = pathNodeParent;
		}
		return ultimateParent;
	}

	public boolean union(T object1, T object2)
	{
		IDisjointSetNode<T> root1 = findRoot(object1);
		IDisjointSetNode<T> root2 = findRoot(object2);
		if (root1 == root2) {
			return true;
		}
		if (root2.getRank() < root1.getRank()) {
			root2.setParent(root1);
		}
		else if (root2.getRank() > root1.getRank()) {
			root1.setParent(root2);
		}
		else {
			root1.setParent(root2);
			root2.setRank(root2.getRank() + 1);
		}
		return false;
	}

	public boolean isEquivalent(T object1, T object2)
	{
		return findRoot(object1) == findRoot(object2);
	}

	public Collection<Set<T>> getEquivalenceSets()
	{
		Map<IDisjointSetNode<T>, Set<T>> equivalanceSets = new LinkedHashMap<IDisjointSetNode<T>, Set<T>>();
		for (T object : m_objectToNode.keySet()) {
			IDisjointSetNode<T> representative = findRoot(object);

			Set<T> equivalanceSet = equivalanceSets.get(representative);
			if (equivalanceSet == null) {
				equivalanceSet = new LinkedHashSet<T>();
				equivalanceSets.put(representative, equivalanceSet);
			}
			equivalanceSet.add(object);
		}

		return equivalanceSets.values();
	}

	public boolean isSingleSet()
	{
		IDisjointSetNode<T> previousRepresentative = null;
		for (T object : m_objectToNode.keySet()) {
			IDisjointSetNode<T> currentRepresentative = findRoot(object);
			if (previousRepresentative != null) {
				if (previousRepresentative != currentRepresentative) {
					return false;
				}
			}
			else {
				previousRepresentative = currentRepresentative;
			}
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		return m_objectToNode.keySet().isEmpty();
	}

	public boolean contains(T object)
	{
		return m_objectToNode.containsKey(object);
	}

	public void clear()
	{
		m_objectToNode.clear();
	}
}