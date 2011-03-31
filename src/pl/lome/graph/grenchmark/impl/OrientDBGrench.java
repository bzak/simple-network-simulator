package pl.lome.graph.grenchmark.impl;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.TransactionalGraph.Conclusion;
import com.tinkerpop.blueprints.pgm.TransactionalGraph.Mode;
import com.tinkerpop.blueprints.pgm.impls.orientdb.OrientGraph;


public class OrientDBGrench extends Grenchmark {

	@Override
	protected Graph initGraph() {
		OrientGraph orient = new OrientGraph("local:/tmp/OrientDBGrench");
		orient.setTransactionMode(Mode.MANUAL);
		return orient;
	}

	@Override
	protected void cleanupGraph() {
		OrientGraph orient = (OrientGraph) graph;

		orient.startTransaction();
		orient.clear();
		orient.stopTransaction(Conclusion.SUCCESS);
		orient.shutdown();
	}

}
