package pl.lome.socialsym;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.gremlin.GremlinScriptEngine;

import pl.lome.graph.grenchmark.step.Step;

public class Confounding2NodesStep extends Step {
	private double weight;
	private int repeats;
	private Random rand;
	
	public Confounding2NodesStep(double weight, int repeats) {
		this.weight = weight;
		this.repeats = repeats;
		this.rand = new Random();
	}
	
	@Override
	public void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception {
		
		// prepare roulette
		Roulette vertexRoulette = new Roulette();
		for (Vertex v : graph.getVertices()) {
			vertexRoulette.Update(v.getId(), 1);		
		}
		for (int i = 0; i < repeats; i++) {
			confound2nodes(graph, vertexRoulette);
		}		
	}

	private void confound2nodes(Graph graph, Roulette vertexRoulette) {
		Vertex randomV = graph.getVertex(vertexRoulette.RandomSlot());
		
		HashMap<String, Vertex> neighbours = new HashMap<String, Vertex>();
		Roulette neighbourRoulette = new Roulette();
		
		for (Edge edge : randomV.getOutEdges()) {
			if (edge.getLabel().equalsIgnoreCase("close")) {
				Vertex v = edge.getInVertex();
				neighbourRoulette.Update(v.getId(), 1);
				neighbours.put(v.getId().toString(), v);
			}
		}
		for (Edge edge : randomV.getInEdges()) {
			if (edge.getLabel().equalsIgnoreCase("close")) {
				Vertex v = edge.getOutVertex();
				neighbourRoulette.Update(v.getId(), 1);
				neighbours.put(v.getId().toString(), v);
			}
		}
				
		if (neighbours.size() > 0) {			
			//Vertex conV = (Vertex) neighbours.values().toArray()[rand.nextInt(neighbours.size())]; //neighbours.get(neighbourRoulette.RandomSlot());
			Vertex conV = graph.getVertex(neighbourRoulette.RandomSlot());
			new Attitude(randomV).infect(weight);
			new Attitude(conV).infect(weight);
		}
	}

}
