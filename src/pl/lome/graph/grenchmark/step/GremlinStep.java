package pl.lome.graph.grenchmark.step;

import java.io.FileReader;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.gremlin.GremlinScriptEngine;

public class GremlinStep extends Step {
	private String src;

	public GremlinStep(String src) {
		this.src = src;
	}

	@Override
	public void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception {
		engine.eval(new FileReader(src));
	}

}
