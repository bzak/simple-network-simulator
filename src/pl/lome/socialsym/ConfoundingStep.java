package pl.lome.socialsym;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;
import com.tinkerpop.gremlin.GremlinScriptEngine;

import pl.lome.graph.grenchmark.impl.TinkerBench;
import pl.lome.graph.grenchmark.step.Step;

public class ConfoundingStep extends Step {
	int populationSize;
	double randomness;
	double weight;
	Random rand;
	String attributeName; 
	
	/**
	 * @param nodesToInfect - how many nodes shall be infected
	 * @param randomness - 0..1 
	 * 1 purely random, 
	 * 0 fully confounded
	 */
	public ConfoundingStep(int nodesToInfect, double randomness, double weight) {
		this.populationSize = nodesToInfect;
		this.randomness = randomness;
		this.weight = weight;	
		this.rand = new Random();			
		this.attributeName = "value";
	}
	
	public ConfoundingStep(int nodesToInfect, double randomness, double weight, String attributeName) {
		this.populationSize = nodesToInfect;
		this.randomness = randomness;
		this.weight = weight;	
		this.rand = new Random();				 
		this.attributeName = attributeName;
	}
	
	@Override
	public void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception {
		// prepare roulette
		Roulette r = new Roulette();
		for (Vertex v : graph.getVertices()) {
			r.Update(v.getId(), 1);		
		}
		int counter = 0;
		while (counter < populationSize) {
			Vertex randomV = graph.getVertex(r.RandomSlot());			
			counter += Confound(randomV, graph);
		}
	}

	private int Confound(Vertex v, Graph graph) {
		int infected = 1;

		// mark vertex
		infect(v);
		
		// copy edges to temporary variable
		ArrayList<Edge> edges = new ArrayList<Edge>(); 		
		for (Edge e : v.getOutEdges()) {
			if (e.getLabel().equalsIgnoreCase("friend")) {
				edges.add(e);
			}
		}
		
		// select a few confounding edges
		while (rand.nextDouble() > randomness && !edges.isEmpty()) {
			int idx = rand.nextInt(edges.size());			
			Vertex chosen = edges.get(idx).getInVertex();
			if (!isConnected(v, chosen)) {
				infect(chosen);
				infected++;
				edges.remove(idx);			
			}
		}
		return infected;
	}

	private void infect(Vertex v) {
		new Attitude(v, attributeName).infect(weight);
	}

	private boolean isConnected(Vertex v1, Vertex v2) {
		for (Edge edge : v1.getOutEdges()) {
			if (edge.getLabel().equalsIgnoreCase("confounding") && edge.getInVertex() == v2) {
				return true;
			}
		}
		return false;
	}
}
