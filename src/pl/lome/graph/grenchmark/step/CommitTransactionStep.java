package pl.lome.graph.grenchmark.step;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.TransactionalGraph.Conclusion;
import com.tinkerpop.blueprints.pgm.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.pgm.impls.orientdb.OrientGraph;
import com.tinkerpop.gremlin.GremlinScriptEngine;


public class CommitTransactionStep extends Step {

	@Override
	public void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception {
		if (graph instanceof Neo4jGraph) {
			((Neo4jGraph) graph).stopTransaction(Conclusion.SUCCESS);
		}
		if (graph instanceof OrientGraph) {
			((OrientGraph) graph).stopTransaction(Conclusion.SUCCESS);
		}
	}

}
