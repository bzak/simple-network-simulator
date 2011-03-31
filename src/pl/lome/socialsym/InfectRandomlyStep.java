package pl.lome.socialsym;

import java.util.ArrayList;
import java.util.Random;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.gremlin.GremlinScriptEngine;

import pl.lome.graph.grenchmark.step.Step;

public class InfectRandomlyStep extends Step {
	int populationSize;
	double weight;
	Random rand;
	String attributeName; 
	
	/**
	 * @param nodesToInfect - how many nodes shall be infected
	 * @param randomness - 0..1 
	 * 1 purely random, 
	 * 0 fully confounded
	 */
	public InfectRandomlyStep(int nodesToInfect, double weight) {
		this.populationSize = nodesToInfect;
		this.weight = weight;	
		this.rand = new Random();			
		this.attributeName = "value";
	}
	
	public InfectRandomlyStep(int nodesToInfect, double weight, String attributeName) {
		this.populationSize = nodesToInfect;
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
			infect(randomV);
			counter ++;
		}
	}

	private void infect(Vertex v) {
		new Attitude(v, attributeName).infect(weight);
	}

}
