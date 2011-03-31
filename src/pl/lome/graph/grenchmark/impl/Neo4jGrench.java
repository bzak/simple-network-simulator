package pl.lome.graph.grenchmark.impl;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.TransactionalGraph.Conclusion;
import com.tinkerpop.blueprints.pgm.TransactionalGraph.Mode;
import com.tinkerpop.blueprints.pgm.impls.neo4j.Neo4jGraph;

public class Neo4jGrench extends Grenchmark {

	@Override
	protected Graph initGraph() {
		Neo4jGraph neo = new Neo4jGraph("/tmp/Neo4jGrench");
		neo.setTransactionMode(Mode.MANUAL);
		return neo;
	}

	@Override
	protected void cleanupGraph() {
		Neo4jGraph neo = (Neo4jGraph) graph;

		neo.startTransaction();
		neo.clear();
		neo.stopTransaction(Conclusion.SUCCESS);
		neo.shutdown();
	}

}
