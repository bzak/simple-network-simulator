package pl.lome.graph.grenchmark.step;

import java.util.HashMap;

import javax.script.ScriptContext;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;
import com.tinkerpop.gremlin.GremlinScriptEngine;

public class MultiplyGraphStep extends Step {
	private String src;
	private int multiplicator;

	public MultiplyGraphStep(String src, int multiplicator) {
		this.src = src;
		this.multiplicator = multiplicator;
	}

	@Override
	public void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception {
		Graph srcGraph = new TinkerGraph();
		engine.getBindings(ScriptContext.ENGINE_SCOPE).put("$_h", srcGraph);

		// load the sample graph
		engine.eval("g:load($_h, '" + src + "')");

		// multiply it n times
		for (int i = 0; i < multiplicator; i++) {
			copyGraph(srcGraph, graph);
			if (i % 1000 == 1)
				heartbeat();
		}
	}

	private static void copyGraph(Graph srcGraph, Graph destGraph) {
		// temporary hash map for storing vertex ids
		HashMap<String, Object> idMap = new HashMap<String, Object>();

		// copy vertices
		for (Vertex v : srcGraph.getVertices()) {
			Vertex dest = destGraph.addVertex(null);

			// copy vertex properties
			for (String key : v.getPropertyKeys()) {
				dest.setProperty(key, v.getProperty(key));
			}
			idMap.put((String) v.getId(), dest.getId());
		}

		// copy edges
		for (Edge e : srcGraph.getEdges()) {
			Vertex inVertex = destGraph.getVertex(idMap.get(e.getInVertex()
					.getId()));
			Vertex outVertex = destGraph.getVertex(idMap.get(e.getOutVertex()
					.getId()));

			Edge dest = destGraph.addEdge(null, outVertex, inVertex,
					e.getLabel());

			// copy edge properties
			for (String key : e.getPropertyKeys()) {
				dest.setProperty(key, e.getProperty(key));
			}
		}
	}

}
