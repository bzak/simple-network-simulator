package pl.lome.socialsym;

import java.util.ArrayList;
import java.util.Random;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.gremlin.GremlinScriptEngine;

import pl.lome.graph.grenchmark.step.Step;

public class BestFriendsContagion extends Step {
	private int nodesToInfect;
	private double weight;
	private double mutualWeight;
	private Random rand;
	private String attributeName;
	
	public BestFriendsContagion(int nodesToInfect, double weight, double mutualWeight) {
		this.nodesToInfect = nodesToInfect;
		this.weight = weight;
		this.mutualWeight= mutualWeight;
		this.rand = new Random();
		this.attributeName = "value";
	}
	
	public BestFriendsContagion(int nodesToInfect, double weight, double mutualWeight, String attributeName) {
		this.nodesToInfect = nodesToInfect;
		this.weight = weight;
		this.mutualWeight= mutualWeight;
		this.rand = new Random();
		this.attributeName = attributeName;
	}
	
	@Override
	public void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception {
		
		// find all "close" relations
		ArrayList<Edge> edges = new ArrayList<Edge>();		
		for (Edge e : graph.getEdges()) {
			if (e.getLabel().equalsIgnoreCase("close")) {
				edges.add(e);
			}
		}
		
		// influence "close" friends
		for (int i = 0; i < nodesToInfect; i++) {
			Edge e = edges.get(rand.nextInt(edges.size()));
			infuence(e.getInVertex(), e.getOutVertex(), graph);			
		}
	}

	private void infuence(Vertex influencer, Vertex influenced, Graph graph) {
		influenced = graph.getVertex(influenced.getId());
		influencer = graph.getVertex(influencer.getId());
		// double the influence for mutual ties (???)
		// Connected p. 109
		if (isMutual(influencer, influenced)) {
			new Attitude(influencer, attributeName).influence(influenced, mutualWeight);								
		}
		else {
			new Attitude(influencer, attributeName).influence(influenced, weight);		
		}
	}			

	private boolean isMutual(Vertex source, Vertex target) {
		boolean toRelation = false, fromRelation = false;
		for (Edge edge : source.getOutEdges()) {
			if (edge.getLabel().equalsIgnoreCase("close") && edge.getInVertex() == target) {
				toRelation = true;
			}
		}
		for (Edge edge : target.getOutEdges()) {
			if (edge.getLabel().equalsIgnoreCase("close") && edge.getInVertex() == source) {
				fromRelation = true;
			}
		}
		return toRelation && fromRelation;
	}
}
