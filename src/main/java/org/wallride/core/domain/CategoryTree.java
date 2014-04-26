package org.wallride.core.domain;

import java.io.Serializable;
import java.util.*;

public class CategoryTree implements Serializable {

	private HashMap<Long, Node> nodeIdMap = new LinkedHashMap<>();
	private HashMap<String, Node> nodeCodeMap = new LinkedHashMap<>();

	private HashMap<Long, Category> categoryIdMap = new LinkedHashMap<>();
	private HashMap<String, Category> categoryCodeMap = new LinkedHashMap<>();

	private ArrayList<Node> rootNodes = new ArrayList<>();

	public CategoryTree(Collection<Category> categories) {
		categories = new TreeSet<>(categories);
		for (Category category : categories) {
			categoryIdMap.put(category.getId(), category);
			categoryCodeMap.put(category.getCode(), category);
		}

		Iterator<Category> i = categories.iterator();
		while (i.hasNext()) {
			Category category = i.next();
			if (category.getParent() == null) {
				Node node = new Node(category);
				rootNodes.add(node);
				nodeIdMap.put(category.getId(), node);
				nodeCodeMap.put(category.getCode(), node);
				i.remove();
			}
		}

		for (Node node : rootNodes) {
			createChildren(node, categories);
		}
	}

	private void createChildren(Node parent, Collection<Category> categories) {
		ArrayList<Node> children = new ArrayList<>();
		Iterator<Category> i = categories.iterator();
		while (i.hasNext()) {
			Category category = i.next();
			Node node = new Node(category);
			if (parent.getCategory().equals(category.getParent())) {
				children.add(node);
				i.remove();
//				createChildren(node, categories);
			}
		}
		parent.children = children;

		for (Node node : children) {
			createChildren(node, categories);
		}
	}

	public List<Node> getRootNodes() {
		return rootNodes;
	}

	public Node getNodeById(Long id) {
		return nodeIdMap.get(id);
	}

	public Node getNodeByCode(String code) {
		return nodeCodeMap.get(code);
	}

	public Collection<Category> getCategories() {
		return categoryIdMap.values();
	}

	public Category getCategoryById(long id) {
		return categoryIdMap.get(id);
	}

	public Category getCategoryByCode(String code) {
		return categoryCodeMap.get(code);
	}

	public boolean isEmpty() {
		return categoryIdMap.isEmpty();
	}

	public static class Node implements Serializable {

		private Category category;

		private ArrayList<Node> children = new ArrayList<>();

		private Node(Category category) {
			this.category = category;
		}

		public Category getCategory() {
			return category;
		}

		public List<Node> getChildren() {
			return children;
		}

		public boolean contains(Category category) {
			if (getCategory().equals(category)) {
				return true;
			}
			for (Node node : getChildren()) {
				if (node.contains(category)) {
					return true;
				}
			}
			return false;
		}
	}
}
