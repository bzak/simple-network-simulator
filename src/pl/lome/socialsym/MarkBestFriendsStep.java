package pl.lome.socialsym;

import java.util.ArrayList;
import java.util.Random;

import pl.lome.graph.grenchmark.step.Step;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.gremlin.GremlinScriptEngine;

public class MarkBestFriendsStep extends Step {
	private Random rand;
	public static double closeProb;
	public static double mutualProb;

	/**
	 * 
	 * @param closeProb
	 *            - % of frends that is close friends
	 */
	public MarkBestFriendsStep(double closeProb, double mutualProb) {
		this.rand = new Random();
		this.closeProb = closeProb;
		this.mutualProb = mutualProb;
	}

	public void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception {
		ArrayList<Edge> closeRelations = new ArrayList<Edge>();
		
		for (Edge e : graph.getEdges()) {
			if (e.getLabel() != null
					&& (e.getLabel().equalsIgnoreCase("friend") || e.getLabel()
							.equalsIgnoreCase("homophily"))) {
				if (rand.nextDouble() < closeProb) {
					closeRelations.add(e);
				}
			}
		}

		for (Edge edge : closeRelations) {
			if (!isConnected(edge.getInVertex(), edge.getOutVertex())) {
				graph.addEdge(null, edge.getOutVertex(), edge.getInVertex(), "close");	
				// create mutual edge
				if (rand.nextDouble() < mutualProb) {
					graph.addEdge(null, edge.getInVertex(), edge.getOutVertex(), "close");
				}
			}
		}
	}

	private static boolean isConnected(Vertex source, Vertex target) {
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
		return toRelation || fromRelation;
	}

	public static void homophilicBond(Graph graph, Vertex v1, Vertex v2) {
		Random rand = new Random();
		if (rand.nextDouble() < closeProb && !isConnected(v1,v2)) {
			if (rand.nextDouble() < mutualProb) {
				graph.addEdge(null, v1, v2, "close");
				graph.addEdge(null, v2, v1, "close");
			}
			else {
				graph.addEdge(null, v1, v2, "close");
			}
		} 
	}
	
	// @Override
	// public void execute(GremlinScriptEngine engine, Graph graph)
	// throws Exception {
	// ArrayList<Edge> closeRelations= new ArrayList<Edge>();
	//
	// for (Edge e : graph.getEdges()) {
	// if (e.getLabel() != null &&
	// (e.getLabel().equalsIgnoreCase("friend") ||
	// e.getLabel().equalsIgnoreCase("homophily"))) {
	// if (rand.nextDouble() < probability) {
	// closeRelations.add(e);
	// }
	// }
	// }
	//
	// for (Edge edge : closeRelations) {
	// graph.addEdge(null, edge.getOutVertex(), edge.getInVertex(), "close");
	// }
	// }

}
