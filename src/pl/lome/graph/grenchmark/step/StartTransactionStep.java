package pl.lome.graph.grenchmark.step;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.pgm.impls.orientdb.OrientGraph;
import com.tinkerpop.gremlin.GremlinScriptEngine;


public class StartTransactionStep extends Step {

	@Override
	public void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception {
		if (graph instanceof Neo4jGraph) {
			((Neo4jGraph) graph).startTransaction();
		}
		if (graph instanceof OrientGraph) {
			((OrientGraph) graph).startTransaction();
		}

	}

}
