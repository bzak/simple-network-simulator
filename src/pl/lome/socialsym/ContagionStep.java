package pl.lome.socialsym;

import java.util.ArrayList;
import java.util.Random;

import org.antlr.grammar.v3.ANTLRv3Parser.notSet_return;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.gremlin.GremlinScriptEngine;
import com.tinkerpop.gremlin.compiler.GremlinParser.return_statement_return;

import pl.lome.graph.grenchmark.step.Step;

public class ContagionStep extends Step {
	private int nodesToInfect;
	private Random rand;
	
	public ContagionStep(int nodesToInfect) {
		this.nodesToInfect = nodesToInfect;
		this.rand = new Random();
	}
	
	@Override
	public void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception {		
		ArrayList<Vertex> infected = new ArrayList<Vertex>(); 
		for (Vertex v : graph.getVertices()) {
			Object value = v.getProperty("value");
			if (value != null && Double.parseDouble(value.toString()) > 0) {
				infected.add(v);
			}
		}
		
		int counter = 0;
		while (counter < nodesToInfect) {
			Vertex v1 = infected.get(rand.nextInt(infected.size()));			
			counter += infuence(v1, graph);
		}
	}

	private int infuence(Vertex v, Graph graph) {
		// copy edges to temporary variable
		ArrayList<Edge> edges = new ArrayList<Edge>(); 		
		for (Edge e : v.getOutEdges()) {
			if (e.getLabel().equalsIgnoreCase("friend")) {
				edges.add(e);
			}
		}
		
		Edge e = edges.get(rand.nextInt(edges.size()));
		Vertex target = e.getInVertex();
		if (!isConnected(v, target)) {
			graph.addEdge(null, v, target, "contagion");
			infect(target);
			return 1;
		}
		return 0;
	}

	private void infect(Vertex v) {
		v.setProperty("value", 1);
	}

	private boolean isConnected(Vertex v1, Vertex v2) {
		for (Edge edge : v1.getOutEdges()) {
			if (edge.getLabel().equalsIgnoreCase("contagion") && edge.getInVertex() == v2) {
				return true;
			}
		}
		return false;
	}
}
