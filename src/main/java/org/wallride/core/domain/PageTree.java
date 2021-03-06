/*
 * Copyright 2014 Tagbangers, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wallride.core.domain;

import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;

public class PageTree implements Serializable {

	private HashMap<Long, Node> nodeIdMap = new LinkedHashMap<>();

	private HashMap<String, Node> nodeCodeMap = new LinkedHashMap<>();

	private HashMap<Long, Page> pageIdMap = new LinkedHashMap<>();

	private HashMap<String, Page> pageCodeMap = new LinkedHashMap<>();

	private ArrayList<Node> rootNodes = new ArrayList<>();

	public PageTree(Collection<Page> pages) {
		pages = new TreeSet<>(pages);
		for (Page page : pages) {
			pageIdMap.put(page.getId(), page);
			pageCodeMap.put(page.getCode(), page);
		}

		Iterator<Page> i = pages.iterator();
		while (i.hasNext()) {
			Page page = i.next();
			if (page.getParent() == null) {
				Node node = new Node(page);
				rootNodes.add(node);
				nodeIdMap.put(page.getId(), node);
				nodeCodeMap.put(page.getCode(), node);
				i.remove();
			}
		}

		for (Node node : rootNodes) {
			createChildren(node, pages);
		}
	}

	private void createChildren(Node parent, Collection<Page> pages) {
		List<Node> children = new ArrayList<>();
		Iterator<Page> i = pages.iterator();
		while (i.hasNext()) {
			Page page = i.next();
			Node node = new Node(page);
			node.parent = parent;
			nodeIdMap.put(page.getId(), node);
			nodeCodeMap.put(page.getCode(), node);
			if (parent.getPage().equals(page.getParent())) {
				children.add(node);
				i.remove();
//				createChildren(node, categories);
			}
		}
		parent.children = children;

		for (Node node : children) {
			createChildren(node, pages);
		}
	}

	public List<Node> getRootNodes() {
		return rootNodes;
	}

	public Map<Page, String> getPaths(String code) {
		List<Page> parents = getParentPagesByCode(code);
		if (CollectionUtils.isEmpty(parents)) {
			return null;
		}
		Map<Page, String> paths = new LinkedHashMap<>();
		StringBuilder path = new StringBuilder();
		for (Page p : parents) {
			if (path != null) {
				path.append("/");
			}
			path.append(p.getCode());
			paths.put(p, path.toString());
		}
		return paths;
	}

	public Node getRootNodeByCode(String code) {
		Node node = getNodeByCode(code);
		if (node != null) {
			while (node.getParent() != null) {
				node = node.getParent();
			}
		}
		return node;
	}

	private List<Page> getParentPagesByCode(String code) {
		Page page = getPageByCode(code);
		if (page == null) {
			return null;
		}
		List<Page> parents = new LinkedList<>();
		if (page.getParent() != null) {
			while(page.getParent() != null) {
				parents = addParent(page, parents);
				page = page.getParent();
			}
		}
		Collections.reverse(parents);
		return parents;
	}


	private List<Page> addParent(Page page, List<Page> target) {
		target.add(page.getParent());
		return target;
	}

	public Node getNodeById(Long id) {
		return nodeIdMap.get(id);
	}

	public Node getNodeByCode(String code) {
		return nodeCodeMap.get(code);
	}

	public Collection<Page> getPages() {
		return pageIdMap.values();
	}

	public List<Page> getSiblingPagesByCode(String code) {
		return getSiblingPagesByCode(code, false);
	}

	public List<Page> getSiblingPagesByCode(String code, boolean includeSelf) {
		Page page = getPageByCode(code);
		if (page == null) {
			return null;
		}
		List<Page> siblings = new ArrayList<>();
		if (page.getParent() == null) {
			return siblings;
		}
		Node node = getNodeByCode(page.getParent().getCode());
		for (Node child : node.getChildren()) {
			if (includeSelf || !page.equals(child.getPage())) {
				siblings.add(child.getPage());
			}
		}
		return siblings;
	}

	public Page getPageById(long id) {
		return pageIdMap.get(id);
	}

	public Page getPageByCode(String code) {
		return pageCodeMap.get(code);
	}

	public boolean isParentPage(Page target, Page page) {
		List<Page> parents = getParentPagesByCode(page.getCode());
		if (CollectionUtils.isEmpty(parents)) {
			return false;
		}
		return parents.contains(target);
	}

	public boolean isEmpty() {
		return pageIdMap.isEmpty();
	}

	public static class Node implements Serializable {

		private Page page;

		private Node parent;

		private List<Node> children = new ArrayList<>();

		private Node(Page page) {
			this.page = page;
		}

		public Page getPage() {
			return page;
		}

		public Node getParent() {
			return parent;
		}

		public List<Node> getChildren() {
			return children;
		}

		public boolean contains(Page page) {
			if (getPage().equals(page)) {
				return true;
			}
			for (Node node : getChildren()) {
				if (node.contains(page)) {
					return true;
				}
			}
			return false;
		}
	}
}
