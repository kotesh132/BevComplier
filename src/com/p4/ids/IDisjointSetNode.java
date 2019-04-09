package com.p4.ids;

public interface IDisjointSetNode<T>
{

	/**
	 * Gets the parent of this node
	 *
	 * @return parent of this node
	 */
	IDisjointSetNode<T> getParent();

	/**
	 * Gets the object this node refers to
	 *
	 * @return object this node refers to
	 */
	T getObject();

	/**
	 * Sets the specified node to be the parent of this node.
	 *
	 * @param parent node that is to be made the parent
	 */
	void setParent(IDisjointSetNode<T> parent);

	/**
	 * Gets the depth of the tree. Or in other words the number of children in the tree rooted at this node.
	 *
	 * @return number of children in the tree rooted at this node
	 */
	int getRank();

	/**
	 * Sets the depth of the tree to be the specified value. Or in other words sets the number of children in the tree
	 * rooted at this node to the specified value.
	 *
	 * @param size number of children in the tree rooted at this node
	 */
	void setRank(int size);
}
