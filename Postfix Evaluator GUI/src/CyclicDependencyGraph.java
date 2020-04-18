import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

class CyclicDependencyGraph {

	private int vertices;

	private final List<List<Integer>> adjacencyList;
	private HashMap<Integer, String> mappingIndexToCell = new HashMap<Integer, String>();
	Set<String> cellInCyclicDependency = new HashSet<String>();
	String[] columnNames;
	int index = 0;

	CyclicDependencyGraph(String[] columnNames) {
		this.columnNames = columnNames;
		for (String cell : columnNames) {
			mappingIndexToCell.put(index, cell);
			++index;
		}
		vertices = columnNames.length;
		adjacencyList = new ArrayList<>(vertices);
		for (int i = 0; i < vertices; i++)
			adjacencyList.add(new LinkedList<>());
	}

	private boolean isCyclicUtilRecursiveHelper(int i, boolean[] visited, boolean[] recursionStack) {

		// Mark the current node as visited and
		// part of recursion stack
		if (recursionStack[i])
			return true;

		if (visited[i])
			return false;

		visited[i] = true;

		recursionStack[i] = true;
		List<Integer> children = adjacencyList.get(i);

		for (Integer child : children)
			if (isCyclicUtilRecursiveHelper(child, visited, recursionStack)) {
				cellInCyclicDependency.add(mappingIndexToCell.get(i));
				return true;
			}
		recursionStack[i] = false;

		return false;
	}

	public void addEdge(int source, int dest) {
		adjacencyList.get(source).add(dest);
	}

	// Returns true if the graph contains a cycle, else false.
	public boolean isCyclic() {

		// Mark all the vertices as not visited and not part of recursion stack
		boolean[] visited = new boolean[vertices];
		boolean[] recStack = new boolean[vertices];

		// Call the recursive helper function to detect cycle in different DFS trees
		for (int i = 0; i < vertices; i++)
			if (isCyclicUtilRecursiveHelper(i, visited, recStack)) {
				return true;
			}

		return false;
	}

	public Set<String> getNodesInCycle() {
		return cellInCyclicDependency;
	}
}