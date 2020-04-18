import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import junit.framework.TestCase;

class CyclicDependencyGraphTest extends TestCase{
	String columns[] = { "$A", "$B", "$C", "$D", "$E", "$F", "$G", "$H", "$I" };
	
	@Test
	void testForCycleTrue() {
		CyclicDependencyGraph graph = new CyclicDependencyGraph(columns);
		Set<String> cellInCyclicDependency = new HashSet<String>();
		cellInCyclicDependency.add("$A");
		cellInCyclicDependency.add("$B");
		cellInCyclicDependency.add("$C");


	    graph.addEdge(0, 1); 
	    graph.addEdge(0, 2); 
	    graph.addEdge(1, 2); 
	    graph.addEdge(2, 0); 
	    graph.addEdge(2, 3); 
	    graph.addEdge(3, 3); 
	    assertEquals(graph.isCyclic(), true);
	    assertEquals(graph.getNodesInCycle(), cellInCyclicDependency);
	}
	
	@Test
	void testForCycleFalse() {
		CyclicDependencyGraph graph1 = new CyclicDependencyGraph(columns);

	    graph1.addEdge(0, 1); 
	    graph1.addEdge(1, 2); 
	    graph1.addEdge(2, 3); 
	    graph1.addEdge(3, 4); 
	    graph1.addEdge(4, 5); 
	    graph1.addEdge(5, 6); 
	    assertEquals(graph1.isCyclic(), false);
	}
}
