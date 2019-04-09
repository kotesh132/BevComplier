package com.p4.ds;


import com.p4.ids.IDisjointSetNode;

public class DisjointSetNode<T> implements IDisjointSetNode<T>
{

	private IDisjointSetNode<T> m_parent;
	private T m_object;
	/**
	 * Size of this set
	 */
	private int m_depth = 0;

	public DisjointSetNode(T object)
	{
		m_object = object;
	}

	public IDisjointSetNode<T> getParent()
	{
		return m_parent;
	}

	public T getObject()
	{
		return m_object;
	}

	public void setParent(IDisjointSetNode<T> parent)
	{
		m_parent = parent;
	}

	public int getRank()
	{
		return m_depth;
	}

	public void setRank(int size)
	{
		m_depth = size;
	}
}