package pl.lome.graph.grenchmark.step;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.gremlin.GremlinScriptEngine;

public class LoadGraphMLStep extends Step {

	private String filename;
	
	public LoadGraphMLStep(String filename ) {
		this.filename = filename;
	}
	
	@Override
	public void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception {
		engine.eval("g:load('"+filename+"')");
	}

}
