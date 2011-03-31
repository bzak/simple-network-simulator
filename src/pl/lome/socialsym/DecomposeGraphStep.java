package pl.lome.socialsym;

import java.util.ArrayList;
import java.util.Random;

import pl.lome.graph.grenchmark.step.Step;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.gremlin.GremlinScriptEngine;

public class DecomposeGraphStep extends Step {
	private int verticiesToDelete, edgesToDelete;
	private Random rand;
	
	public DecomposeGraphStep(int verticiesToDelete, int edgesToDelete) {
		this.verticiesToDelete = verticiesToDelete;
		this.edgesToDelete = edgesToDelete;
		this.rand = new Random();
	}

	@Override
	public void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception {
		
		ArrayList<Vertex> verticies = new ArrayList<Vertex>(); 
		for (Vertex v : graph.getVertices()) {			
			verticies.add(v);
		}

		for (int i = 0; i<verticiesToDelete && verticies.size() > 0; i++) {
			Vertex v = verticies.get(rand.nextInt(verticies.size()));
			graph.removeVertex(v);
		}
	}

}
