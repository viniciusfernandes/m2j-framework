package br.com.innovatium.mumps2java.datastructure;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import br.com.innovatium.mumps2java.todo.REVIEW;

class StackVariables {
	private final class MapStackLevel {
		private final Map<Integer, Map<Integer, Deque<Node>>> mapStackLevel;

		public MapStackLevel() {
			mapStackLevel = new HashMap<>();
		}

		public String dump(Integer methodStackLevel) {
			StringBuilder dump = new StringBuilder();
			Map<Integer, Deque<Node>> map = this.mapStackLevel.get(methodStackLevel);
			Set<Entry<Integer, Deque<Node>>> entries = map.entrySet();
			for (Entry<Integer, Deque<Node>> entry : entries) {
				for (Node node : entry.getValue()) {
					dump.append("[call=").append(methodStackLevel).append(",level=").append(entry.getKey()).append("] ")
							.append(node).append("\n");
				}
			}
			return dump.toString();
		}

		public int getLastLevel(int methodStackLevel) {
			Map<Integer, Deque<Node>> map = mapStackLevel.get(methodStackLevel);

			if (map == null) {
				return 0;
			}

			Object[] array = map.keySet().toArray();
			return array == null || array.length <= 0 ? 0 : (int) array[array.length - 1];
		}

		@REVIEW(author = "vinicius", description = "Melhorar o metodo de mapeamento dos indices para evitar os IFs a todo momento em que buscamos uma pilha")
		public Deque<Node> getStack(int stackLevel, int methodStackLevel) {
			Map<Integer, Deque<Node>> map = mapStackLevel.get(methodStackLevel);
			if (map == null) {
				return null;
			}
			return map.get(stackLevel);
		}

		@REVIEW(author = "vinicius", description = "Melhorar o metodo de mapeamento dos indices para evitar os IFs a todo momento em que buscamos uma pilha")
		public void put(int stackLevel, int methodStackLevel, Deque<Node> stack) {
			Map<Integer, Deque<Node>> map = mapStackLevel.get(methodStackLevel);

			if (map == null) {
				map = new HashMap<>();
				mapStackLevel.put(methodStackLevel, map);
			}
			map.put(stackLevel, stack);
		}

		public Collection<Node> remove(int stackLevel, int methodStackLevel) {

			Map<Integer, Deque<Node>> map = mapStackLevel.get(methodStackLevel);
			if (map == null) {
				return null;
			}

			Deque<Node> stack = map.remove(stackLevel);
			mapStackLevel.remove(stackLevel);
			return stack;
		}
	}

	final MapStackLevel mapStackLevel;

	public StackVariables() {
		mapStackLevel = new MapStackLevel();
	}

	public String dumpStackLevel() {
		return mapStackLevel.dump(0);
	}

	public int getLastLevel(int methodStackLevel) {
		return mapStackLevel.getLastLevel(methodStackLevel);
	}

	public Collection<Node> getStack(Integer stackLevel, Integer methodStackLevel) {
		return mapStackLevel.getStack(stackLevel, methodStackLevel);
	}

	public Collection<Node> pull(Integer stackLevel) {
		return pull(stackLevel, 0);
	}

	public Collection<Node> pull(Integer stackLevel, Integer methodStackLevel) {
		return mapStackLevel.remove(stackLevel, methodStackLevel);
	}

	public Node push(Node node) {
		if (node == null) {
			return null;
		}

		Integer stackLevel = node.getStackLevel();
		Integer methodStackLevel = node.getMethodStackLevel();
		if (stackLevel == null || methodStackLevel == null) {
			throw new IllegalArgumentException("The inclusion of empty stack and method stack levels node is not allowed.");
		}
		Deque<Node> queue = mapStackLevel.getStack(stackLevel, methodStackLevel);

		if (queue == null) {
			queue = new ArrayDeque<Node>();
			mapStackLevel.put(stackLevel, methodStackLevel, queue);
		}
		queue.addFirst(node);
		return node;
	}

	public Collection<Node> removeStack(Integer stackLevel, Integer methodStackLevel) {
		return mapStackLevel.remove(stackLevel, methodStackLevel);
	}
}
