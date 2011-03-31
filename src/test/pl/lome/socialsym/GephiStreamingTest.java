package test.pl.lome.socialsym;


import org.junit.Assert;
import org.junit.Test;

import pl.lome.gephi.GephiStreaming;
import pl.lome.socialsym.Attitude;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;

public class GephiStreamingTest {

	@Test
	public void testAddNodeJSON() {
		TinkerGraph g = new TinkerGraph();
		Vertex v = g.addVertex("nodeA");
		
		v.setProperty("value", 0.5);
		GephiStreaming gs = new GephiStreaming();
		String actual = gs.AddNodeJSON(v);
		
		String expected = "{\"an\":{\"nodeA\":{\"value\":0.5}}}";
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testAddEdgeJSON() {
		TinkerGraph g = new TinkerGraph();
		Vertex vA = g.addVertex("nodeA");
		Vertex vB = g.addVertex("nodeB");
		Edge e = g.addEdge("edgeAB", vA, vB, "friends");
		
		GephiStreaming gs = new GephiStreaming();
		String actual = gs.AddEgdeJSON(e);
		
		String expected = "{\"ae\":{\"edgeAB\":{\"source\":\"nodeA\",\"target\":\"nodeB\",\"directed\":true}}}";
		Assert.assertEquals(expected, actual);
	}
}
