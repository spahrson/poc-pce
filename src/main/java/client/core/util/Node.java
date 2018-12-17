package client.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Node to build a tree structure. Every node has one parent (except root) and can have childrens.
 * 
 * PoC - PCE
 * @author Ramon Spahr (ramon.spahr@gmail.com)
 *
 * @param <T> data object type
 */
public class Node<T> {

	private List<Node<T>> children = new ArrayList<Node<T>>();
	private Node<T> parent = null;
	private T data = null;

	public Node(T data) {
		this.data = data;
	}

	public Node(T data, Node<T> parent) {
		this.data = data;
		this.parent = parent;
	}

	public Node() {
	}

	public List<? extends Node<T>> getChildren() {
		return children;
	}

	public void setParent(Node<T> parent) {
		this.parent = parent;
		if(!parent.getChildren().contains(this)) 
			parent.addChild(this);
	}

	public void addChild(T data) {
		Node<T> child = new Node<T>(data);
		child.setParent(this);
		this.children.add(child);
	}

	public void addChild(Node<T> child) {
		this.children.add(child);
		if(child.getParent()!=this)
			child.setParent(this);
	}

	public Node<T> getParent() {
		return this.parent;
	}

	public T getData() {
		return this.data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
	
}
