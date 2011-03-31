package pl.lome.socialsym;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.gremlin.GremlinScriptEngine;

import pl.lome.gephi.GephiStreaming;
import pl.lome.graph.grenchmark.step.Step;

public class StreamGraphStep extends Step {

	public void execute(Graph graph) {
		GephiStreaming gs = new GephiStreaming();
		for (Vertex vertex : graph.getVertices()) {
			gs.AddNode(vertex);
		}
		for (Edge edge : graph.getEdges()) {
			gs.AddEdge(edge);
		}		
	}
	
	@Override
	public void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception {
		execute(graph);
	}

}
