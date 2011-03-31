package pl.lome.graph.grenchmark.step;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.gremlin.GremlinScriptEngine;

/**
 * @author Blazej Zak (http://twitter.com/blazejzak)
 */
public abstract class Step {

	public abstract void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception;

	// / tell you're still alive
	protected void heartbeat() {
		// System.out.print('.');
	}
}
