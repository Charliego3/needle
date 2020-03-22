package io.github.struct;

public class PrefixTree {


	public class Node {
		private Node node;
		private NodeType nType;
		private String path;

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
		 * 是否以 * 结尾的路由
		 */
		private boolean catchAll;
	}

	public enum NodeType {
		STATIC, // 非根节点的普通字符串节点
		ROOT, // 根节点
		PARAM, // 参数节点, 例如 :id
		CATCH_ALL // 通配符节点, 例如 *anyway
		;
	}
}
