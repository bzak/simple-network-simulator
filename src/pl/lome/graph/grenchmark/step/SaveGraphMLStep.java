package pl.lome.graph.grenchmark.step;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.gremlin.GremlinScriptEngine;

public class SaveGraphMLStep extends Step {

	private String filename;
	
	public SaveGraphMLStep(String filename ) {
		this.filename = filename;
	}
	
	@Override
	public void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception {
		engine.eval("g:save('"+filename+"')");
	}

}
