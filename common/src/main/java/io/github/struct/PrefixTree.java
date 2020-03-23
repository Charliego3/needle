package io.github.struct;

import java.io.Serializable;
import java.util.Map;

public class PrefixTree implements Serializable {

	private Node              node;
	private Map<String, Node> children;

	public PrefixTree(Node node) {
		this.node = node;
	}

	public void addChild(Node node) {
		this.children.put(node.getPath(), node);
	}

	public Node getChild(String path) {
		return children.get(path);
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public static class Node {
//		private Node node;
		private NodeType type;
		private String   path;

		/**
		 * 自节点索引开头字母组合
		 * 自节点含有参数节点时为空
		 */
		private String indices;

		/**
		 * 标识子节点中是否有参数节点
		 */
		private boolean wildChild;

		/**
		 * 是否是通配符节点
		 */
		private boolean catchAll;

		public NodeType getType() {
			return type;
		}

		public void setType(NodeType type) {
			this.type = type;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getIndices() {
			return indices;
		}

		public void setIndices(String indices) {
			this.indices = indices;
		}

		public boolean isWildChild() {
			return wildChild;
		}

		public void setWildChild(boolean wildChild) {
			this.wildChild = wildChild;
		}

		public boolean isCatchAll() {
			return catchAll;
		}

		public void setCatchAll(boolean catchAll) {
			this.catchAll = catchAll;
		}
	}

	public static enum NodeType {
		STATIC, // 非根节点的普通字符串节点
		ROOT, // 根节点
		PARAM, // 参数节点, 例如 :id
		CATCH_ALL // 通配符节点, 例如 *anyway
		;
	}
}
