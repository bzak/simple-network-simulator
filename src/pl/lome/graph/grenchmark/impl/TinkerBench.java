package pl.lome.graph.grenchmark.impl;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;

/**
 * @author Blazej Zak (http://twitter.com/blazejzak)
 */
public class TinkerBench extends Grenchmark {
	protected Graph graph;

	public TinkerBench() {}
	
	public TinkerBench(Graph graph) {
		this.graph = graph;
	}
	
	@Override
	protected Graph initGraph() {
		if (graph == null) graph = new TinkerGraph();
		return graph;
	}

	@Override
	protected void cleanupGraph() {

	}

}
