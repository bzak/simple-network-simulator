package pl.lome.graph.grenchmark.step;

import java.util.ArrayList;
import java.util.Iterator;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.gremlin.GremlinScriptEngine;

public class CompositeStep extends Step {
	private ArrayList<Step> steps = new ArrayList<Step>();
	
	public void add(Step step) {
		steps.add(step);
	}
	
	@Override
	public void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception {
		for (Step step : steps) {
			step.execute(engine, graph);
		}
	}

}
