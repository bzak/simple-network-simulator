package pl.lome.socialsym;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.gremlin.GremlinScriptEngine;

import pl.lome.graph.grenchmark.step.Step;

public class ClearValuesStep extends Step {
	private String attributeName;
	public ClearValuesStep() {
		attributeName = "value";
	}
	public ClearValuesStep(String attributeName) {
		this.attributeName = attributeName;
	}
	
	@Override
	public void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception {
		for (Vertex v : graph.getVertices()) {
			new Attitude(v, attributeName).initialValue();		
		}
	}

}
